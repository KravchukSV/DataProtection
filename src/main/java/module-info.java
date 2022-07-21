module com.example.dataprotection {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dataprotection to javafx.fxml;
    exports com.example.dataprotection;
    exports com.example.dataprotection.controllers;
    opens com.example.dataprotection.controllers to javafx.fxml;
}