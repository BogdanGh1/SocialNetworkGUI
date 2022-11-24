package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;

public class Service {

    private final UserDbRepository userRepository;

    private final Repository<Long, Friendship> friendshipRepository;

    private final CommunityProblemsSolver solver; 

    public Service(UserDbRepository userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        solver = new CommunityProblemsSolver(userRepository, friendshipRepository);
    }

    public void addUser( String name, String email, String password) throws ValidationException, RepoException {
        User user = new User(1, name, email, password);
        userRepository.save(user);
    }

    public void removeUser(long id) throws RepoException {
        userRepository.delete(id);
    }

    public Iterable<User> getUsersList() throws RepoException {
        return userRepository.findAll();
    }

    public void addFriendship( long idUser, long idFriend) throws ValidationException, RepoException {
        Friendship friendship = new Friendship(1, idUser, idFriend, LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    public void removeFriendship(long id) throws RepoException {
        friendshipRepository.delete(id);
    }

    public Iterable<Friendship> getFriendshipsList() throws RepoException {
        return friendshipRepository.findAll();
    }

    public int getNumberOfCommunities() throws RepoException {
        return solver.getNumberOfCommunities();
    }

    public List<User> getMostSociableCommunity() throws RepoException {
        return solver.getMostSociableCommunity();
    }

    public User findUser(String email){
        return userRepository.findOneByEmail(email);
    }
}
