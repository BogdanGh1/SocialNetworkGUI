package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    long idUser;
    long idFriend;
    LocalDateTime friendsFrom;

    public Friendship(long id, long idUser, long idFriend, LocalDateTime friendsFrom) {
        super(id);
        this.idUser = idUser;
        this.idFriend = idFriend;
        this.friendsFrom = friendsFrom;
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
