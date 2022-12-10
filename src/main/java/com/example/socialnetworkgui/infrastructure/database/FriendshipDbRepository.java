package com.example.socialnetworkgui.infrastructure.database;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.utils.FriendshipStatus;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findOne(Long id) {
        String sql = "SELECT iduser,idfriend,friendsfrom,status FROM friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            long idUser = resultSet.getLong("iduser");
            long idFriend = resultSet.getLong("idfriend");
            String friendsFrom = resultSet.getString("friendsfrom");
            String status = resultSet.getString("status");
            FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
            LocalDateTime localDateTimeFriendsFrom = LocalDateTime.parse(friendsFrom, Utils.DATE_TIME_FORMATTER);
            return new Friendship(id, idUser, idFriend, localDateTimeFriendsFrom,friendshipStatus);

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long idUser = resultSet.getLong("iduser");
                long idFriend = resultSet.getLong("idfriend");
                String friendsFrom = resultSet.getString("friendsfrom");
                String status = resultSet.getString("status");
                FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
                LocalDateTime localDateTimeFriendsFrom = LocalDateTime.parse(friendsFrom, Utils.DATE_TIME_FORMATTER);
                Friendship friendship = new Friendship(id, idUser, idFriend, localDateTimeFriendsFrom,friendshipStatus);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return friendships;
    }

    @Override
    public void save(Friendship entity) {
        String sql = "insert into friendships (iduser, idfriend, friendsfrom, status ) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            validator.validate(entity);
            ps.setLong(1, entity.getIdUser());
            ps.setLong(2, entity.getIdFriend());
            ps.setString(3, entity.getFriendsFrom().format(Utils.DATE_TIME_FORMATTER));
            ps.setString(4, entity.getStatus().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) throws RepoException {
        Friendship friendship = findOne(id);
        if (friendship == null)
            throw new RepoException("Non-existing friendship!");

        String sql = "DELETE FROM friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, friendship.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void update(Friendship entity) {
        String sql = "update friendships set status = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            validator.validate(entity);
            ps.setString(1, entity.getStatus().toString());
            ps.setLong(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
