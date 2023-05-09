module jvav {
    requires javafx.web;
    requires jdk.httpserver;
    requires jdk.unsupported;
    requires static org.jetbrains.annotations;

    opens jvav to javafx.graphics;
}