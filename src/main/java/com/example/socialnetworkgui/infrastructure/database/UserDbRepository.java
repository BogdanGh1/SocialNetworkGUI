package com.example.socialnetworkgui.infrastructure.database;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(Long id) {
        String sql = "SELECT name,email,password FROM users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            User user = new User(id, name, password, email);
            loadFriends(user);
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(id, name, email, password);
                validator.validate(user);
                loadFriends(user);
                users.add(user);
            }
            return users;
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return users;
    }

    @Override
    public void save(User entity) {

        String sql = "insert into users (name, email, password ) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            validator.validate(entity);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
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
        User user = findOne(id);
        if (user == null)
            throw new RepoException("Non-existing user!");

        String sql = "DELETE FROM users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void update(User entity) {

    }

    private void loadFriends(User user) {
        String sql = "SELECT id,idfriend,friendsfrom FROM friendships where iduser = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long idFriend = resultSet.getLong("idfriend");
                String friendsFrom = resultSet.getString("friendsfrom");
                LocalDateTime localDateTimeFriendsFrom = LocalDateTime.parse(friendsFrom, Utils.DATE_TIME_FORMATTER);
                Friendship friendship = new Friendship(id, user.getId(), idFriend, localDateTimeFriendsFrom);
                user.addFriendship(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
