package com.example.socialnetworkgui.infrastructure.database;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
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

public class UserDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

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

    public User findOneByEmail(String email) {
        String sql = "SELECT name,id,password FROM users where email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            long id = resultSet.getLong("id");
            String password = resultSet.getString("password");
            User user = new User(id, name, email, password);
            loadFriends(user);
            return user;

        } catch (SQLException e) {
                e.printStackTrace();
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
        String sql = "SELECT id,idfriend,friendsfrom,status FROM friendships where iduser = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long idFriend = resultSet.getLong("idfriend");
                String friendsFrom = resultSet.getString("friendsfrom");
                String status = resultSet.getString("status");
                FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
                LocalDateTime localDateTimeFriendsFrom = LocalDateTime.parse(friendsFrom, Utils.DATE_TIME_FORMATTER);
                Friendship friendship = new Friendship(id, user.getId(), idFriend, localDateTimeFriendsFrom,friendshipStatus);
                user.addFriendship(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Iterable<User> findFriends(User user) {
        Set<User> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)){
             PreparedStatement ps = connection.prepareStatement("select  u2.id,u2.name,u2.email,u2.password from friendships fr inner join users u1 on u1.id = fr.iduser inner join users u2 on u2.id = fr.idfriend where u1.id = ?");
             ps.setLong(1, user.getId());
             ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User friend = new User(id, name, email, password);
                friends.add(friend);
            }

            ps = connection.prepareStatement("select u2.id,u2.name,u2.email,u2.password from friendships fr inner join users u1 on u1.id = fr.idfriend inner join users u2 on u2.id = fr.iduser where u1.id = ?");
            ps.setLong(1, user.getId());
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User friend = new User(id, name, email, password);
                friends.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return friends;
    }
    public Iterable<User> findfriendSuggestions(User user) {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User u = new User(id, name, email, password);
                validator.validate(u);
                loadFriends(u);
                users.add(u);
            }

        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Iterable<User> friends = findFriends(user);
        for (User u:friends){
            users.remove(u);
        }
        return users;
    }
}
/*
select u2.id,u2.name,count(u2.name)
from users u1 inner join friendships fr1
on u1.id = fr1.iduser or u1.id = fr1.idfriend
inner join users u2
on u2.id = fr1.iduser or u2.id = fr1.idfriend
inner join friendships fr2
on u2.id = fr2.iduser or u2.id = fr2.idfriend
inner join users u3
on u3.id = fr2.iduser or u3.id = fr2.idfriend
inner join friendships fr3
on u3.id = fr3.iduser or u3.id = fr3.idfriend
where u1.id = 13 and u2.id <> 13 and u3.id <> 13 and u2.id <> u3.id and (u1.id = fr3.iduser or u1.id = fr3.idfriend)
group by u2.name,u2.id;
 */