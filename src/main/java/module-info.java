module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires net.sf.jasperreports.core;
    requires java.desktop;


    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}