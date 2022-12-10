package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;
import com.example.socialnetworkgui.utils.FriendshipStatus;

import java.time.LocalDateTime;

public class Service {

    private final UserDbRepository userRepository;

    private final Repository<Long, Friendship> friendshipRepository;

    public Service(UserDbRepository userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public void addUser(String name, String email, String password) throws ValidationException, RepoException {
        User user = new User(1, name, email, password);
        userRepository.save(user);
    }

    public void removeUser(long id) throws RepoException {
        userRepository.delete(id);
    }

    public Iterable<User> getUsersList() throws RepoException {
        return userRepository.findAll();
    }

    public void addFriendship(long idUser, long idFriend) throws ValidationException, RepoException {
        Friendship friendship = new Friendship(1, idUser, idFriend, LocalDateTime.now(), FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);
    }

    public void removeFriendship(long id) throws RepoException {
        friendshipRepository.delete(id);
    }

    public Iterable<Friendship> getFriendshipsList() throws RepoException {
        return friendshipRepository.findAll();
    }

    public User findUser(String email) {
        return userRepository.findOneByEmail(email);
    }
}
