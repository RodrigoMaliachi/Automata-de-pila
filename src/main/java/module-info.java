module com.example.automatadepila {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.automatadepila to javafx.fxml;
    exports com.example.automatadepila;
}