package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.infrastructure.database.FriendshipDbRepository;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;
import com.example.socialnetworkgui.validation.FriendshipValidator;
import com.example.socialnetworkgui.validation.UserValidator;
import com.example.socialnetworkgui.validation.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DemoController implements Initializable {
    private Service service;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, Long> userIdColumn;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TableColumn<User, String> userEmailColumn;

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private void onAddUserClick() throws ValidationException, RepoException {
        String name = textFieldName.getText();
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();

        textFieldName.setText("");
        textFieldEmail.setText("");
        textFieldPassword.setText("");
        service.addUser(name,email,password);
        reloadUserTableView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Validator<User> userValidator = new UserValidator();
        Repository<Long, User> userRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres", friendshipValidator);
        service = new Service(userRepository, friendshipRepository);
        userIdColumn.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        reloadUserTableView();
    }

    private void reloadUserTableView() {
        try {
            userTableView.getItems().setAll(parseUserList());
        } catch (RepoException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> parseUserList() throws RepoException {
        // parse and construct User datamodel list by looping your ResultSet rs
        // and return the list
        List<User> users = new ArrayList<>();
        for (User user : service.getUsersList()) {
            users.add(user);
        }
        return users;
    }

}
