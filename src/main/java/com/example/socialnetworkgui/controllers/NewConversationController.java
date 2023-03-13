package com.example.socialnetworkgui.controllers;

import com.example.socialnetworkgui.business.FriendshipService;
import com.example.socialnetworkgui.business.MessageService;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.FriendDTO;
import com.example.socialnetworkgui.utils.FriendsTableState;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class NewConversationController {
    @FXML
    private TableView<FriendDTO> friendsTableView;
    @FXML
    private TableColumn<FriendDTO, String> friendsTableColumn;
    @FXML
    private TextField conversationNameTextField;

    private User user;
    private FriendshipService friendshipService;
    private MessageService messageService;

    public void init() {
        friendsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        friendsTableColumn.setResizable(false);
        friendsTableColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("name"));
        reloadFriendsTable();
    }

    private void reloadFriendsTable() {
        Iterable<FriendDTO> friendsDTOS;
        List<FriendDTO> friendDTOList = new ArrayList<>();
        friendsDTOS = friendshipService.getFriends(user);
        for (FriendDTO friendDTO : friendsDTOS) {
            friendDTOList.add(friendDTO);
        }
        friendsTableView.getItems().setAll(friendDTOList);
    }
    public void onCreateConversationClick(ActionEvent actionEvent) {
        String name = conversationNameTextField.getText();
        conversationNameTextField.setText("");
        List<FriendDTO> participants = friendsTableView.getSelectionModel().getSelectedItems();
        List<Long> participantIds = new ArrayList<>();
        participantIds.add(user.getId());
        for( FriendDTO participant: participants)
            participantIds.add(participant.getId());
        messageService.addConversation(name,participantIds);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

}
