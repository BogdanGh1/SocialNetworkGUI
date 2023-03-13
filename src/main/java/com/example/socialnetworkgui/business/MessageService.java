package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.Conversation;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.FriendDTO;
import com.example.socialnetworkgui.infrastructure.database.MessageDbRepository;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService {
    private final MessageDbRepository messageDbRepository;

    public MessageService(MessageDbRepository messageDbRepository) {
        this.messageDbRepository = messageDbRepository;
    }

    public List<Conversation> getConversations(User user) {
        return messageDbRepository.getConversations(user);
    }

    public List<Message> getMessages(long idConversation) {
        return messageDbRepository.getMessages(idConversation);
    }

    public void addMessage(User user,String text,long idConversation){
        messageDbRepository.save(idConversation,text,user.getId(), LocalDateTime.now());
    }

    public void addConversation(String name, List<Long> participants) {
        messageDbRepository.addConversation(name,participants);
    }
}
