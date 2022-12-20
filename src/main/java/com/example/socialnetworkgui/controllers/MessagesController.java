package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MessagesController {
    @FXML
    private Scene logInScene;
    @FXML
    private Scene friendsScene;
    @FXML
    private Stage mainStage;

    private User user;
    private UserService userService;



    public void setFriendsScene(Scene scene){this.friendsScene = scene;}

    public void setLogInScene(Scene scene) {
        this.logInScene = scene;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void onFriendsButtonClick() throws IOException {
        System.out.println("To Friends");
        mainStage.setScene(friendsScene);
    }
    @FXML
    public void onLogOutButtonClick() {
        System.out.println("Back to LogIn");
        mainStage.setScene(logInScene);
    }
}
