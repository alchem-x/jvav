module jvav {
    requires javafx.web;
    requires jdk.httpserver;
    requires org.jetbrains.annotations;
    requires jdk.unsupported;

    opens jvav to javafx.graphics;
}