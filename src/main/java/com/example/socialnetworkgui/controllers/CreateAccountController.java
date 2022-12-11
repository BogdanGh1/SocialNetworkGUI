package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.business.FriendRequestService;
import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CreateAccountController {

    private UserService userService;

    private FriendshipService friendshipService;

    private FriendRequestService friendRequestService;
    @FXML
    private Stage mainStage;

    @FXML
    private Scene logInScene;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldName;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private PasswordField textFieldConfirmPassword;

    @FXML
    public void onCreateAccountButtonClick(ActionEvent actionEvent) {
        String email = textFieldEmail.getText();
        String name = textFieldName.getText();
        String password = textFieldPassword.getText();
        String confirmPassword = textFieldConfirmPassword.getText();
        textFieldEmail.setText("");
        textFieldName.setText("");
        textFieldPassword.setText("");
        textFieldConfirmPassword.setText("");
        if (Objects.equals(password, confirmPassword)) {
            try {
                userService.addUser(name, email, password);
                User user = userService.findUser(email);

                FXMLLoader loader = new FXMLLoader(CreateAccountController.class.getResource("Views/Friends.fxml"));
                Scene scene = new Scene(loader.load());
                mainStage.setScene(scene);

                FriendsController friendsController = loader.getController();
                friendsController.setUserService(userService);
                friendsController.setFriendshipService(friendshipService);
                friendsController.setFriendRequestService(friendRequestService);
                friendsController.setUser(user);
                friendsController.setMainStage(mainStage);
                friendsController.setLogInScene(logInScene);
                friendsController.init();

            } catch (ValidationException | RepoException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void onLogInButtonClick(ActionEvent actionEvent) throws IOException {
        mainStage.setScene(logInScene);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }
    public void setFriendRequestService(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
    }
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }
    public void setLogInScene(Scene scene) {
        this.logInScene = scene;
    }
}
