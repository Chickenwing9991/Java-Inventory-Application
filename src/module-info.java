module C482.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.logging;

    opens C482.main to javafx.fxml;
    opens C482.Controllers to javafx.fxml;
    opens C482.Model to javafx.base;
    exports C482.main;
}