module org.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;

    opens org.javafx to javafx.fxml;
    opens org.javafx.controller to javafx.fxml;
    exports org.javafx;
    exports org.javafx.controller;
}