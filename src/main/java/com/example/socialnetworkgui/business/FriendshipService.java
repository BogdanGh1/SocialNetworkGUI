package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.FriendDTO;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.database.FriendshipDbRepository;
import com.example.socialnetworkgui.utils.FriendshipStatus;

import java.time.LocalDateTime;

public class FriendshipService {
    private final FriendshipDbRepository friendshipRepository;

    public FriendshipService(FriendshipDbRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
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
    public Iterable<FriendDTO> getFriends(User user){
        return friendshipRepository.findFriends(user);
    }

    public Iterable<FriendDTO> getFriendSuggestions(User user) {
        return friendshipRepository.findFriendSuggestions(user);
    }
}
