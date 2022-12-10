package com.example.socialnetworkgui.domain;

import com.example.socialnetworkgui.utils.FriendshipStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private final long idUser;
    private final long idFriend;
    private final LocalDateTime friendsFrom;
    private FriendshipStatus status;

    public Friendship(long id, long idUser, long idFriend, LocalDateTime friendsFrom,FriendshipStatus friendshipStatus) {
        super(id);
        this.idUser = idUser;
        this.idFriend = idFriend;
        this.friendsFrom = friendsFrom;
        this.status = friendshipStatus;

    }

    public long getIdUser() {
        return idUser;
    }

    public long getIdFriend() {
        return idFriend;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return idUser == that.idUser && idFriend == that.idFriend && Objects.equals(friendsFrom, that.friendsFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idFriend, friendsFrom);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idFriend=" + idFriend +
                ", friendsFrom=" + friendsFrom +
                '}';
    }
}
