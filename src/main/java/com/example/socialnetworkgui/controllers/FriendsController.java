package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.business.FriendRequestService;
import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.FriendDTO;
import com.example.socialnetworkgui.domain.dtos.ReceivedRequestDTO;
import com.example.socialnetworkgui.domain.dtos.SentRequestDTO;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.utils.FriendsTableState;
import com.example.socialnetworkgui.utils.FriendshipStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendsController {
    private FriendsTableState friendsTableState;
    private User user;
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    @FXML
    private Scene logInScene;
    @FXML
    private Stage mainStage;
    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<FriendDTO> friendsTableView;
    @FXML
    private TableColumn<FriendDTO, String> friendsNameColumn;
    @FXML
    private TableColumn<FriendDTO, Long> friendsCommonFriendsNumberColumn;
    @FXML
    private TableColumn<FriendDTO, Button> friendsModifyColumn;
    @FXML
    private TableView<SentRequestDTO> sentRequestsTableView;
    @FXML
    private TableColumn<SentRequestDTO, String> sentRequestsNameColumn;
    @FXML
    private TableColumn<SentRequestDTO, String> sentRequestsDateColumn;
    @FXML
    private TableColumn<SentRequestDTO, Button> sentRequestsCancelColumn;
    @FXML
    private TableView<ReceivedRequestDTO> receivedRequestsTableView;
    @FXML
    private TableColumn<ReceivedRequestDTO, String> receivedRequestsNameColumn;
    @FXML
    private TableColumn<ReceivedRequestDTO, String> receivedRequestsDateColumn;
    @FXML
    private TableColumn<ReceivedRequestDTO, Button> receivedRequestsAcceptColumn;
    @FXML
    private TableColumn<ReceivedRequestDTO, Button> receivedRequestsRejectColumn;
    @FXML
    private ToggleButton friendsToggleButton;
    @FXML
    private ImageView logOutImageView;
    @FXML
    private Label friendsLabel;


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setFriendRequestService(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    public void setLogInScene(Scene scene) {
        this.logInScene = scene;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void init() {
        userNameLabel.setText(user.getName());
        //friends table
        friendsNameColumn.setResizable(false);
        friendsCommonFriendsNumberColumn.setResizable(false);
        friendsModifyColumn.setResizable(false);
        friendsModifyColumn.setText("Remove");
        friendsNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("name"));
        friendsCommonFriendsNumberColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, Long>("commonFriendsNumber"));
        friendsModifyColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, Button>("button"));
        friendsTableState = FriendsTableState.FRIENDS;
        reloadFriendsTable(friendsTableState);
        //sentRequests table
        sentRequestsNameColumn.setResizable(false);
        sentRequestsDateColumn.setResizable(false);
        sentRequestsCancelColumn.setResizable(false);
        sentRequestsNameColumn.setCellValueFactory(new PropertyValueFactory<SentRequestDTO, String>("name"));
        sentRequestsDateColumn.setCellValueFactory(new PropertyValueFactory<SentRequestDTO, String>("date"));
        sentRequestsCancelColumn.setCellValueFactory(new PropertyValueFactory<SentRequestDTO, Button>("button"));
        reloadSentRequestsTable();
        //receivedRequests table
        receivedRequestsNameColumn.setResizable(false);
        receivedRequestsDateColumn.setResizable(false);
        receivedRequestsAcceptColumn.setResizable(false);
        receivedRequestsRejectColumn.setResizable(false);
        receivedRequestsNameColumn.setCellValueFactory(new PropertyValueFactory<ReceivedRequestDTO, String>("name"));
        receivedRequestsDateColumn.setCellValueFactory(new PropertyValueFactory<ReceivedRequestDTO, String>("date"));
        receivedRequestsAcceptColumn.setCellValueFactory(new PropertyValueFactory<ReceivedRequestDTO, Button>("acceptButton"));
        receivedRequestsRejectColumn.setCellValueFactory(new PropertyValueFactory<ReceivedRequestDTO, Button>("rejectButton"));
        reloadReceivedRequestsTable();

        logOutImageView.setOnMouseClicked(event -> {
            onLogOutButtonClick();
        });

    }

    private void reloadFriendsTable(FriendsTableState friendsTableState) {
        try {
            List<FriendDTO> friendDTOS = parseFriendsList(friendsTableState);
            for (FriendDTO friendDTO : friendDTOS) {
                Button button = friendDTO.getButton();
                if (friendsTableState.equals(FriendsTableState.FRIENDS)) {
                    button.setText("Remove");
                    button.setOnAction(event -> {
                        System.out.println("Remove friendship between " + user.getName() + " and " + friendDTO.getName());
                        friendRequestService.setFriendshipStatusBetween(user.getId(), friendDTO.getId(), FriendshipStatus.REJECTED);
                        reloadFriendsTable(friendsTableState);
                    });
                } else {
                    button.setText("Add");
                    button.setOnAction(event -> {
                        System.out.println(" Send request from " + user.getName() + " to " + friendDTO.getName());
                        reloadFriendsTable(friendsTableState);
                        try {
                            friendshipService.addFriendship(user.getId(), friendDTO.getId());
                        } catch (ValidationException | RepoException e) {
                            throw new RuntimeException(e);
                        }
                        reloadSentRequestsTable();
                        reloadFriendsTable(friendsTableState);
                    });

                }
            }
            friendsTableView.getItems().setAll(friendDTOS);
        } catch (RepoException e) {
            throw new RuntimeException(e);
        }
    }

    private List<FriendDTO> parseFriendsList(FriendsTableState friendsTableState) throws RepoException {
        // parse and construct User datamodel list by looping your ResultSet rs
        // and return the list
        Iterable<FriendDTO> friendsDTOS;
        List<FriendDTO> friendDTOList = new ArrayList<>();
        if (friendsTableState == FriendsTableState.FRIENDS)
            friendsDTOS = friendshipService.getFriends(user);
        else friendsDTOS = friendshipService.getFriendSuggestions(user);
        for (FriendDTO friendDTO : friendsDTOS) {
            friendDTOList.add(friendDTO);
        }
        return friendDTOList;
    }


    private void reloadSentRequestsTable() {
        List<SentRequestDTO> sentRequestDTOS = parseSentRequestsList();
        for (SentRequestDTO sentRequestDTO : sentRequestDTOS) {
            System.out.println(sentRequestDTO.getName());
            Button button = sentRequestDTO.getButton();
            button.setOnAction(event -> {
                friendRequestService.remove(user.getId(), sentRequestDTO.getId());
                reloadSentRequestsTable();
                reloadFriendsTable(friendsTableState);
            });
        }
        sentRequestsTableView.getItems().setAll(sentRequestDTOS);
    }

    private List<SentRequestDTO> parseSentRequestsList() {
        return friendRequestService.getSentRequests(user);
    }

    private void reloadReceivedRequestsTable() {
        List<ReceivedRequestDTO> receivedRequestDTOS = parseReceivedRequestsList();
        for (ReceivedRequestDTO receivedRequestDTO : receivedRequestDTOS) {
            System.out.println(receivedRequestDTO.getName());
            Button acceptButton = receivedRequestDTO.getAcceptButton();
            Button rejectButton = receivedRequestDTO.getRejectButton();
            acceptButton.setOnAction(event -> {
                friendRequestService.setFriendshipStatusBetween(user.getId(), receivedRequestDTO.getId(), FriendshipStatus.ACCEPTED);
                reloadReceivedRequestsTable();
                reloadFriendsTable(friendsTableState);
            });
            rejectButton.setOnAction(event -> {
                friendRequestService.setFriendshipStatusBetween(user.getId(), receivedRequestDTO.getId(), FriendshipStatus.REJECTED);
                reloadReceivedRequestsTable();
                reloadFriendsTable(friendsTableState);
            });
        }
        receivedRequestsTableView.getItems().setAll(receivedRequestDTOS);
    }

    private List<ReceivedRequestDTO> parseReceivedRequestsList() {
        return friendRequestService.getReceivedRequests(user);
    }

    @FXML
    public void onToggleFriendsClick(ActionEvent actionEvent) {
        if (friendsTableState == FriendsTableState.FRIENDS) {
            friendsTableState = FriendsTableState.SUGGESTIONS;
            friendsToggleButton.setText("View your friends");
            friendsLabel.setText("Friends suggestions");
            friendsModifyColumn.setText("Add");
        } else {
            friendsTableState = FriendsTableState.FRIENDS;
            friendsToggleButton.setText("View friend suggestions");
            friendsLabel.setText("Your friends");
            friendsModifyColumn.setText("Remove");
        }
        reloadFriendsTable(friendsTableState);
    }


    @FXML
    public void onLogOutButtonClick() {
        System.out.println("Back to LogIn");
        mainStage.setScene(logInScene);
    }
    @FXML
    public void onMessagesButtonClick() throws IOException {
        System.out.println("To Messages");
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/Messages.fxml"));
        Scene scene = new Scene(loader.load());
        MessagesController messagesController = loader.getController();
        messagesController.setFriendsScene(mainStage.getScene());
        messagesController.setLogInScene(logInScene);
        messagesController.setUserService(userService);
        messagesController.setUser(user);
        messagesController.setMainStage(mainStage);

        mainStage.setScene(scene);
    }
}
