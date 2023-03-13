package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.MessageService;
import com.example.socialnetworkgui.business.UserService;
import com.example.socialnetworkgui.domain.Conversation;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MessagesController {
    @FXML
    private Scene logInScene;
    @FXML
    private Scene friendsScene;
    @FXML
    private Stage mainStage;
    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<Conversation> conversationsTableView;
    @FXML
    private TableColumn<Conversation, String> conversationsTableColumn;
    @FXML
    private TableView<Message> messagesTableView;
    @FXML
    private TableColumn<Message, String> fromTableColumn;
    @FXML
    private TableColumn<Message, String> messageTableColumn;
    @FXML
    private TableColumn<Message, String> dateTableColumn;
    @FXML
    private TextArea inputTextArea;

    private long idConversation;
    private User user;
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;

    public void setFriendsScene(Scene scene) {
        this.friendsScene = scene;
    }

    public void setLogInScene(Scene scene) {
        this.logInScene = scene;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setUser(User user) {
        this.user = user;
        this.userNameLabel.setText(user.getName());
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void init() {
        //messagesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        idConversation = -1;
        conversationsTableColumn.setResizable(false);
        conversationsTableColumn.setCellValueFactory(new PropertyValueFactory<Conversation, String>("name"));
        reloadConversationsTable();

        fromTableColumn.setResizable(false);
        messageTableColumn.setResizable(false);
        dateTableColumn.setResizable(false);
        fromTableColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("senderName"));
        messageTableColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("text"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("date"));
        //reloadMessagesTable();

    }

    private void reloadMessagesTable() {
        List<Message> messages = messageService.getMessages(idConversation);
        messagesTableView.getItems().setAll(messages);
    }

    private void reloadConversationsTable() {
        List<Conversation> conversations = messageService.getConversations(user);
        conversationsTableView.getItems().setAll(conversations);
    }

    @FXML
    public void onFriendsButtonClick() throws IOException {
        System.out.println("To Friends");
        mainStage.setScene(friendsScene);
    }

    @FXML
    public void onLogOutButtonClick() {
        System.out.println("Back to LogIn");
        mainStage.setScene(logInScene);
    }

    public void onSendTextImageViewClick(MouseEvent mouseEvent) {
        if (idConversation != -1) {
            String text = inputTextArea.getText();
            inputTextArea.setText("");
            messageService.addMessage(user, text, idConversation);
            reloadMessagesTable();
        }
    }

    public void onNewConversationButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Views/NewConversation.fxml"));
        Scene scene = new Scene(loader.load());

        NewConversationController newConversationController = loader.getController();
        newConversationController.setUser(user);
        newConversationController.setFriendshipService(friendshipService);
        newConversationController.setMessageService(messageService);
        newConversationController.init();

        stage.setTitle("Create Conversation");
        stage.setScene(scene);
        stage.show();
    }

    public void onConversationClick(MouseEvent mouseEvent) {
        Conversation conversation = conversationsTableView.getSelectionModel().getSelectedItem();
        if (conversation != null) {
            this.idConversation = conversation.getId();
            reloadMessagesTable();
        }
    }
}
