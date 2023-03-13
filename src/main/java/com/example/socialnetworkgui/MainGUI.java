package com.example.socialnetworkgui;

import com.example.socialnetworkgui.business.FriendRequestService;
import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.MessageService;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.controllers.LogInController;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.infrastructure.database.FriendRequestDbRepository;
import com.example.socialnetworkgui.infrastructure.database.FriendshipDbRepository;
import com.example.socialnetworkgui.infrastructure.database.MessageDbRepository;
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

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private MessageService messageService;

    private void createServices(){
        Validator<User> userValidator = new UserValidator();
        UserDbRepository userRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        FriendshipDbRepository friendshipRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", friendshipValidator);
        FriendRequestDbRepository friendRequestDbRepository = new FriendRequestDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        MessageDbRepository messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        userService = new UserService(userRepository);
        friendshipService = new FriendshipService(friendshipRepository);
        friendRequestService = new FriendRequestService(friendRequestDbRepository);
        messageService = new MessageService(messageDbRepository);
    }
    @Override
    public void start(Stage stage) throws IOException {
        createServices();
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/LogIn.fxml"));
        Scene scene = new Scene(loader.load());

        LogInController logInController = loader.getController();
        logInController.setUserService(userService);
        logInController.setFriendshipService(friendshipService);
        logInController.setFriendRequestService(friendRequestService);
        logInController.setMessageService(messageService);
        logInController.setMainStage(stage);

        stage.setTitle("THB Network!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}