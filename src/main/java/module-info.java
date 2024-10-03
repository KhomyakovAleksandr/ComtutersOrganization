module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.swt;
    requires java.sql;

//    opens sample;
    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
}