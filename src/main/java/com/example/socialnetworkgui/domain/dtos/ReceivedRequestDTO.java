package com.example.socialnetworkgui.domain.dtos;

import javafx.scene.control.Button;

import java.util.Objects;

public class ReceivedRequestDTO {
    public long id;
    private String name;
    private String date;
    private Button acceptButton;
    private Button rejectButton;

    public ReceivedRequestDTO(long id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        acceptButton = new Button("Accept");
        rejectButton = new Button("Reject");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceivedRequestDTO that = (ReceivedRequestDTO) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(acceptButton, that.acceptButton) && Objects.equals(rejectButton, that.rejectButton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, acceptButton, rejectButton);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public void setAcceptButton(Button acceptButton) {
        this.acceptButton = acceptButton;
    }

    public Button getRejectButton() {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton) {
        this.rejectButton = rejectButton;
    }
}
