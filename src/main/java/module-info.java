module com.example.finalprojonline {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires annotations;


    opens com.example.finalprojonline to javafx.fxml;
    exports com.example.finalprojonline;
}