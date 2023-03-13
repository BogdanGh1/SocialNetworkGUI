package com.example.socialnetworkgui.infrastructure.database;

import com.example.socialnetworkgui.domain.Conversation;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.FriendDTO;
import com.example.socialnetworkgui.utils.Utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDbRepository {

    private final String url;
    private final String username;
    private final String password;

    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public List<Conversation> getConversations(User user) {
        String sql = "SELECT c.id,c.name FROM conversations c " +
                "INNER JOIN convparticipants cv ON c.id = cv.idconv " +
                "WHERE cv.iduser = ? ";
        List<Conversation> conversations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1,user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                Conversation conversation = new Conversation(id, name);
                conversations.add(conversation);
            }
            return conversations;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return conversations;
    }

    public List<Message> getMessages(long idConversation) {
        List<Message> messages = new ArrayList<>();
        String sql = "select m.id,text,date,idsender,name as \"sendername\" from messages m\n" +
                     "inner join users u on u.id = m.idsender\n" +
                     "where m.idconversation = ?;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idConversation);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long senderId = resultSet.getLong("idsender");
                String text = resultSet.getString("text");
                String senderName = resultSet.getString("sendername");
                String date = resultSet.getString("date");
                LocalDateTime localDateTime = LocalDateTime.parse(date, Utils.DATE_TIME_FORMATTER);
                Message message = new Message(id,text,senderId,senderName,localDateTime);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return messages;
    }

    public void save(long idConversation,String text,long idSender,LocalDateTime date){
        String sql = "insert into messages (idConversation, text, idSender, date ) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idConversation);
            ps.setString(2, text);
            ps.setLong(3, idSender);
            ps.setString(4, date.format(Utils.DATE_TIME_FORMATTER));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void addConversation(String name, List<Long> participants) {
        String sql = "insert into conversations (name) values (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        sql = "select max(id) from conversations;";
        long idConv = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                idConv = resultSet.getLong("max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        sql = "insert into convparticipants (idconv, iduser) values (?,?)";
        for (Long participant:participants){
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, idConv);
                ps.setLong(2, participant);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
