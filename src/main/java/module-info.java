module com.example.toylanguageinterpretorgui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.toylanguageinterpretorgui to javafx.fxml;
    exports com.example.toylanguageinterpretorgui;
}