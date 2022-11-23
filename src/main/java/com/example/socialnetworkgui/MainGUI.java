package com.example.socialnetworkgui;

import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.infrastructure.database.FriendshipDbRepository;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;
import com.example.socialnetworkgui.presentation.UI;
import com.example.socialnetworkgui.validation.FriendshipValidator;
import com.example.socialnetworkgui.validation.UserValidator;
import com.example.socialnetworkgui.validation.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Demo.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Demo!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}