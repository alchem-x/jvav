package jvav;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jvav.support.UnsafeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

import static java.nio.charset.StandardCharsets.UTF_8;

abstract class Supports {

    static void reply200(@NotNull HttpExchange httpExchange, @Nullable String data) throws IOException {
        try (var responseBody = httpExchange.getResponseBody()) {
            data = Objects.requireNonNullElse(data, "");
            var body = data.getBytes(UTF_8);
            httpExchange.sendResponseHeaders(200, body.length);
            responseBody.write(body);
        }
    }

    static void reply200(@NotNull HttpExchange httpExchange) throws IOException {
        reply200(httpExchange, null);
    }

    static void replyHtml(@NotNull HttpExchange httpExchange, @NotNull String filename) {
        var resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        try (resourceStream) {
            httpExchange.getResponseHeaders().set("content-type", "text/html");
            reply200(httpExchange, new String(Objects.requireNonNull(resourceStream).readAllBytes(), UTF_8));
        } catch (IOException ex) {
            UnsafeUtils.getUnsafe().throwException(ex);
        }
    }

    static void reply400(@NotNull HttpExchange httpExchange, @Nullable String message) throws IOException {
        try (var responseBody = httpExchange.getResponseBody()) {
            message = Objects.requireNonNullElse(message, "400 Bad Request");
            var body = message.getBytes(UTF_8);
            httpExchange.sendResponseHeaders(400, body.length);
            responseBody.write(body);
        }
    }

    static void reply404(@NotNull HttpExchange httpExchange) throws IOException {
        try (var responseBody = httpExchange.getResponseBody()) {
            var body = "404 Not Found".getBytes(UTF_8);
            httpExchange.sendResponseHeaders(404, body.length);
            responseBody.write(body);
        }
    }

    static @NotNull HttpServer createHttpServer(int port) {
        try {
            var address = new InetSocketAddress(port);
            return HttpServer.create(address, 0);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    record Route(@NotNull String method, @NotNull String path) {
    }
}

public class DesktopService {

    private final Map<Supports.Route, HttpHandler> router = new HashMap<>();

    private HttpServer httpServer;

    private void handleIndex(@NotNull HttpExchange httpExchange) throws IOException {
        Supports.replyHtml(httpExchange, "index.html");
    }


    public void start(int port) {
        this.router.put(new Supports.Route("GET", "/"), this::handleIndex);
        //
        this.httpServer = Supports.createHttpServer(port);
        this.httpServer.createContext("/", (httpExchange) -> {
            var path = httpExchange.getRequestURI().getPath();
            var method = httpExchange.getRequestMethod();
            var handler = this.router.get(new Supports.Route(method, path));
            if (handler == null) {
                Supports.reply404(httpExchange);
                return;
            }
            try {
                handler.handle(httpExchange);
            } catch (Exception ex) {
                var message = Objects.requireNonNullElse(ex.getMessage(), "Bad Request");
                Supports.reply400(httpExchange, message);
            }
        });
        this.httpServer.setExecutor(ForkJoinPool.commonPool());
        this.httpServer.start();
    }

    public void stop() {
        if (this.httpServer == null) {
            return;
        }
        this.httpServer.stop(0);
        this.httpServer = null;
    }
}
