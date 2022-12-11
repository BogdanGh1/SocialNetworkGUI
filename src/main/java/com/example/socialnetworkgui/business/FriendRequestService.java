package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.dtos.ReceivedRequestDTO;
import com.example.socialnetworkgui.domain.dtos.SentRequestDTO;
import com.example.socialnetworkgui.infrastructure.database.FriendRequestDbRepository;
import com.example.socialnetworkgui.utils.FriendshipStatus;

import java.util.List;

public class FriendRequestService {
    private final FriendRequestDbRepository friendRequestDbRepository;

    public FriendRequestService(FriendRequestDbRepository friendRequestDbRepository) {
        this.friendRequestDbRepository = friendRequestDbRepository;
    }

    public void setFriendshipStatusBetween(Long idUser, Long idFriend, FriendshipStatus friendshipStatus) {
        String status = friendshipStatus.toString();
        friendRequestDbRepository.setFriendshipStatusBetween(idUser, idFriend,status);
    }

    public List<SentRequestDTO> getSentRequests(User user) {
        return friendRequestDbRepository.getSentRequests(user);

    }

    public void remove(Long idUser, long idFriend) {
        friendRequestDbRepository.remove(idUser, idFriend);
    }

    public List<ReceivedRequestDTO> getReceivedRequests(User user) {
        return friendRequestDbRepository.getReceivedRequests(user);
    }
}
