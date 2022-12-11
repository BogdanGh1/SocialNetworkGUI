package com.example.socialnetworkgui.infrastructure.database;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.ReceivedRequestDTO;
import com.example.socialnetworkgui.domain.dtos.SentRequestDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepository {
    private final String url;
    private final String username;
    private final String password;

    public FriendRequestDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void setFriendshipStatusBetween(Long idUser, Long idFriend,String status) {
        String sql = "update friendships set status = ? where (iduser = ? and idfriend = ?) or (iduser = ? and idfriend = ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,status);
            ps.setLong(2, idUser);
            ps.setLong(3, idFriend);
            ps.setLong(4, idFriend);
            ps.setLong(5, idUser);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void remove(Long idUser, long idFriend) {
        String sql = "delete from friendships where iduser = ? and idfriend = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idUser);
            ps.setLong(2, idFriend);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<SentRequestDTO> getSentRequests(User user) {
        List<SentRequestDTO> sentRequestDTOS = new ArrayList<>();
        String sql = " select u2.id as id,u2.name as name,fr.friendsfrom as date from users u1    \n" +
                "                                  inner join friendships fr        \n" +
                "                                  on u1.id = fr.iduser\n" +
                "                                  inner join users u2    \n" +
                "                                  on u2.id = fr.idfriend\n" +
                "                                  where u1.id = ? and fr.status = 'PENDING';";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                SentRequestDTO sentRequestDTO = new SentRequestDTO(id,name,date);
                sentRequestDTOS.add(sentRequestDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return sentRequestDTOS;
    }

    public List<ReceivedRequestDTO> getReceivedRequests(User user) {
        List<ReceivedRequestDTO> receivedRequestDTOS = new ArrayList<>();
        String sql = " select u2.id as id,u2.name as name,fr.friendsfrom as date from users u1    \n" +
                "                                  inner join friendships fr        \n" +
                "                                  on u1.id = fr.idfriend\n" +
                "                                  inner join users u2    \n" +
                "                                  on u2.id = fr.iduser\n" +
                "                                  where u1.id = ? and fr.status = 'PENDING';";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                ReceivedRequestDTO receivedRequestDTO = new ReceivedRequestDTO(id,name,date);
                receivedRequestDTOS.add(receivedRequestDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return receivedRequestDTOS;
    }
}
