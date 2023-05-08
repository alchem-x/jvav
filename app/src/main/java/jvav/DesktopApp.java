package jvav;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DesktopApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Jvav");
        var webView = new WebView();
        webView.getEngine().load(this.getUrl());
        var vBox = new VBox(webView);
        var scene = new Scene(vBox, 960, 600);
        stage.setScene(scene);
        stage.show();
    }

    private String getUrl() {
        var port = this.getParameters().getNamed().get("port");
        return "http://localhost:%s/".formatted(port);
    }
}
