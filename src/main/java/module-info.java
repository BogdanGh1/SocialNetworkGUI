module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.socialnetworkgui to javafx.fxml;
    exports com.example.socialnetworkgui;
}