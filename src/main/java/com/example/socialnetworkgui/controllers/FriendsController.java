package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.utils.FriendsTableState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FriendsController {
    private FriendsTableState friendsTableState;
    private User user;
    private UserService userService;
    private FriendshipService friendshipService;
    @FXML
    private Stage mainStage;
    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<User> friendsTableView;
    @FXML
    private TableColumn<User, String> friendsNameColumn;
    @FXML
    private TableColumn<User, String> friendsAddColumn;
    @FXML
    private ToggleButton friendsToggleButton;
    @FXML
    private Label friendsLabel;


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void init() {
        friendsAddColumn.setText("Remove");
        userNameLabel.setText(user.getName());
        friendsNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        friendsTableState = FriendsTableState.FRIENDS;
        reloadFriendsTable(friendsTableState);
    }

    private void reloadTables() {

    }

    private void reloadFriendsTable(FriendsTableState friendsTableState) {
        try {
            friendsTableView.getItems().setAll(parseFriendsList(friendsTableState));
        } catch (RepoException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> parseFriendsList(FriendsTableState friendsTableState) throws RepoException {
        // parse and construct User datamodel list by looping your ResultSet rs
        // and return the list
        List<User> usersAsList = new ArrayList<>();
        Iterable<User> users;
        if (friendsTableState == FriendsTableState.FRIENDS)
            users = userService.getUserFriends(user);
        else users = userService.getUserFriendSuggestions(user);
        for (User user : users) {
            usersAsList.add(user);
        }
        return usersAsList;
    }

    private void reloadSentRequestsTable() {

    }

    private void reloadReceivedRequestsTable() {

    }

    public void onToggleFriendsClick(ActionEvent actionEvent) {
        if (friendsTableState == FriendsTableState.FRIENDS) {
            friendsTableState = FriendsTableState.SUGGESTIONS;
            friendsToggleButton.setText("View your friends");
            friendsLabel.setText("Friends suggestions");
            friendsAddColumn.setText("Add");
        } else {
            friendsTableState = FriendsTableState.FRIENDS;
            friendsToggleButton.setText("View friend suggestions");
            friendsLabel.setText("Your friends");
            friendsAddColumn.setText("Remove");
        }
        reloadFriendsTable(friendsTableState);


    }
}
