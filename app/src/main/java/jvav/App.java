package jvav;

import javafx.application.Application;

public class App {

    public static void main(String... args) {
        var port = 8080;
        //
        var service = new DesktopService();
        service.start(port);
        //
        Application.launch(DesktopApp.class, "--port=" + port);
        service.stop();
    }
}
