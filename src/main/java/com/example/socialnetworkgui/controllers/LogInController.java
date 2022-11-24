package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogInController {

    private Service service;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private Stage logInStage;

    @FXML
    public void onCreateAccountClick(ActionEvent actionEvent) throws IOException {
        System.out.println( "Create new Account!");
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/CreateAccount.fxml"));
        Scene scene = new Scene(loader.load());
        logInStage.setScene(scene);
    }

    @FXML
    public void onLogInButtonClick(ActionEvent actionEvent) {
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();
        textFieldEmail.setText("");
        textFieldPassword.setText("");

        User user = service.findUser(email);
        if(user==null)
            return;
        if(Objects.equals(user.getPassword(), password))
            System.out.println(user.getPassword() + "\n" + password);
    }
    public void setService(Service service){
        this.service = service;
    }

    public void setLogInStage(Stage stage){
        this.logInStage = stage;
    }

}
