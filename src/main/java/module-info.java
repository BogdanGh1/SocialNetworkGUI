module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetworkgui to javafx.fxml;
    opens com.example.socialnetworkgui.domain to javafx.fxml;
    opens com.example.socialnetworkgui.domain.dtos to javafx.fxml;
    opens com.example.socialnetworkgui.controllers to javafx.fxml;
    //opens com.example.socialnetworkgui.business to javafx.fxml;
    exports com.example.socialnetworkgui;
    exports com.example.socialnetworkgui.domain;
    exports com.example.socialnetworkgui.domain.dtos;
    exports com.example.socialnetworkgui.controllers;
    //exports com.example.socialnetworkgui.business;
}