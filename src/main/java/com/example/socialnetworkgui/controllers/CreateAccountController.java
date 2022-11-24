package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountController {

    private Service service;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldName;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    public void onCreateAccountButtonClick(ActionEvent actionEvent){
        String email = textFieldEmail.getText();
        String name = textFieldName.getText();
        String password = textFieldPassword.getText();
        textFieldEmail.setText("");
        textFieldName.setText("");
        textFieldPassword.setText("");
        try {
            service.addUser(name,email,password);
            User user = service.findUser(email);
        }catch (ValidationException | RepoException e) {
            e.printStackTrace();
        }

    }
    public void setService(Service service){
        this.service = service;
    }
}
