package com.example.socialnetworkgui.infrastructure.database;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.FriendDTO;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.utils.FriendshipStatus;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
            return new Friendship(id, idUser, idFriend, localDateTimeFriendsFrom, friendshipStatus);

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
                Friendship friendship = new Friendship(id, idUser, idFriend, localDateTimeFriendsFrom, friendshipStatus);
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

    public Iterable<FriendDTO> findFriends(User user) {
        Map<Long, FriendDTO> friendsDTOMap = new HashMap<>();
        String sql = "select u2.id as id,u2.name as name,count(u2.name) as count\n" +
                "from users u1 inner join friendships fr1\n" +
                "on u1.id = fr1.iduser or u1.id = fr1.idfriend\n" +
                "inner join users u2\n" +
                "on u2.id = fr1.iduser or u2.id = fr1.idfriend\n" +
                "inner join friendships fr2\n" +
                "on u2.id = fr2.iduser or u2.id = fr2.idfriend\n" +
                "inner join users u3\n" +
                "on u3.id = fr2.iduser or u3.id = fr2.idfriend\n" +
                "inner join friendships fr3\n" +
                "on u3.id = fr3.iduser or u3.id = fr3.idfriend\n" +
                "where u1.id = ? and u2.id <> ? and u3.id <> ? and u2.id <> u3.id and (u1.id = fr3.iduser or u1.id = fr3.idfriend) and fr1.status = 'ACCEPTED' and fr2.status = 'ACCEPTED' and fr3.status = 'ACCEPTED'\n" +
                "group by u2.name,u2.id;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setLong(2, user.getId());
            statement.setLong(3, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int commonFriendsNumber = resultSet.getInt("count");

                FriendDTO friendDTO = new FriendDTO(id, name, commonFriendsNumber);
                friendsDTOMap.put(id, friendDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        sql = "select u.name,u.id as id\n" +
                "from users u\n" +
                "inner join friendships fr\n" +
                "on u.id = fr.iduser or u.id = fr.idfriend\n" +
                "where (fr.iduser = ? or fr.idfriend = ?) and u.id <> ? and fr.status = 'ACCEPTED';";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setLong(2, user.getId());
            statement.setLong(3, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                FriendDTO friendDTO = new FriendDTO(id, name, 0);
                friendsDTOMap.putIfAbsent(id, friendDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return friendsDTOMap.values();
    }

    public Iterable<FriendDTO> findFriendSuggestions(User user) {
        Map<Long, FriendDTO> friendsDTOMap = new HashMap<>();
        //selecting all users from the database
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select id,name from users")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                FriendDTO friendDTO = new FriendDTO(id, name, 0);
                friendsDTOMap.put(id, friendDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //setting the count
        String sql = "select u1.id as id,count(u1.id) as count\n" +
                "from users u1\n" +
                "inner join friendships fr1\n" +
                "on u1.id = fr1.iduser or u1.id = fr1.idfriend\n" +
                "inner join users u2\n" +
                "on u2.id = fr1.iduser or u2.id = fr1.idfriend\n" +
                "inner join friendships fr2\n" +
                "on u2.id = fr2.iduser or u2.id = fr2.idfriend\n" +
                "where u1.id <> ? and u2.id <> ? and u1.id <> u2.id and (fr2.iduser = ? or fr2.idfriend = ?) and fr1.status = 'ACCEPTED' and fr2.status = 'ACCEPTED'\n" +
                "group by u1.name,u1.id\n" +
                "order by u1.id;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setLong(2, user.getId());
            statement.setLong(3, user.getId());
            statement.setLong(4, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                int commonFriendsNumber = resultSet.getInt("count");
                FriendDTO friendDTO = friendsDTOMap.get(id);
                friendDTO.setCommonFriendsNumber(commonFriendsNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //removing user's friends
        Iterable<FriendDTO> friendsDTOS = findFriends(user);
        for (FriendDTO friendDTO : friendsDTOS)
            friendsDTOMap.remove(friendDTO.getId());

        //remove the pending and rejected friendships
        sql = "select fr.iduser as id1,fr.idfriend as id2 from users u\n" +
                "inner join friendships fr\n" +
                "on u.id = fr.iduser or u.id = fr.idfriend\n" +
                "where (fr.status = 'REJECTED' or fr.status = 'PENDING') and u.id = ?;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id1 = resultSet.getLong("id1");
                long id2 = resultSet.getLong("id2");
                friendsDTOMap.remove(id1);
                friendsDTOMap.remove(id2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //remove the current user
        friendsDTOMap.remove(user.getId());
        return friendsDTOMap.values();
    }
}
