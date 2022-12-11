package com.example.socialnetworkgui.domain.dtos;

import com.example.socialnetworkgui.domain.Entity;
import javafx.scene.control.Button;

import java.util.Objects;

public class FriendDTO {

    private Long id;
    private String name;
    private int commonFriendsNumber;

    private Button button;

    public FriendDTO(Long id, String name, int commonFriendsNumber) {
        this.id = id;
        this.name = name;
        this.commonFriendsNumber = commonFriendsNumber;
        this.button = new Button("Remove");
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommonFriendsNumber() {
        return commonFriendsNumber;
    }

    public void setCommonFriendsNumber(int commonFriendsNumber) {
        this.commonFriendsNumber = commonFriendsNumber;
    }

    public Button getButton() {
        return button;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendDTO that = (FriendDTO) o;
        return commonFriendsNumber == that.commonFriendsNumber && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, commonFriendsNumber);
    }
}
