package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.business.FriendRequestService;
import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.MessageService;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogInController {

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;

    private MessageService messageService;
    @FXML
    private Stage mainStage;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label passwordErrorLabel;

    @FXML
    public void onEmailTextFieldClicked(MouseEvent mouseEvent) {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    public void onPasswordTextFieldClicked(MouseEvent mouseEvent) {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    @FXML
    public void onCreateAccountClick(ActionEvent actionEvent) throws IOException {
        System.out.println("Create new Account!");
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/CreateAccount.fxml"));
        Scene scene = new Scene(loader.load());

        CreateAccountController createAccountController = loader.getController();
        createAccountController.setUserService(userService);
        createAccountController.setFriendshipService(friendshipService);
        createAccountController.setMainStage(mainStage);
        createAccountController.setLogInScene(mainStage.getScene());
        mainStage.setScene(scene);
    }

    @FXML
    public void onLogInButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();
        textFieldEmail.setText("");
        textFieldPassword.setText("");
        User user = userService.findUser(email);
        if (user == null)
            return;
        if (Objects.equals(user.getPassword(), password)) {
            System.out.println("Logged In");

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/Friends.fxml"));
            Scene scene = new Scene(loader.load());

            FriendsController friendsController = loader.getController();
            friendsController.setUserService(userService);
            friendsController.setFriendshipService(friendshipService);
            friendsController.setFriendRequestService(friendRequestService);
            friendsController.setMessageService(messageService);
            friendsController.setUser(user);
            friendsController.setMainStage(mainStage);
            friendsController.setLogInScene(mainStage.getScene());
            friendsController.init();
            mainStage.setScene(scene);
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setFriendRequestService(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }


}
