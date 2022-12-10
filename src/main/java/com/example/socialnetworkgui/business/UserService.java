package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;

public class UserService {
    private final UserDbRepository userRepository;

    public UserService(UserDbRepository userRepository) {
        this.userRepository = userRepository;
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

    public User findUser(String email) {
        return userRepository.findOneByEmail(email);
    }

    public Iterable<User> getUserFriends(User user) throws RepoException {
        return userRepository.findFriends(user);
    }
    public Iterable<User> getUserFriendSuggestions(User user) throws RepoException {
        return userRepository.findfriendSuggestions(user);
    }
}
