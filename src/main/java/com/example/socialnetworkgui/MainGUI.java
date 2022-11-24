package com.example.socialnetworkgui;

import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.controllers.LogInController;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.infrastructure.database.FriendshipDbRepository;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;
import com.example.socialnetworkgui.validation.FriendshipValidator;
import com.example.socialnetworkgui.validation.UserValidator;
import com.example.socialnetworkgui.validation.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGUI extends Application {

    private Service service;

    private void createServices(){
        Validator<User> userValidator = new UserValidator();
        UserDbRepository userRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", friendshipValidator);
        service = new Service(userRepository, friendshipRepository);
    }
    @Override
    public void start(Stage stage) throws IOException {
        createServices();
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/LogIn.fxml"));
        Scene scene = new Scene(loader.load());

        LogInController logInController = loader.getController();
        logInController.setService(service);
        logInController.setLogInStage(stage);

        stage.setTitle("Demo!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}