package com.example.socialnetworkgui.domain.dtos;

import javafx.scene.control.Button;

import java.util.Objects;

public class SentRequestDTO {
    public long id;
    private String name;
    private String date;
    private Button button;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentRequestDTO that = (SentRequestDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(button, that.button);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, button);
    }

    public long getId(){
        return id;
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

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public SentRequestDTO(long id,String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.button = new Button("Cancel");
    }
}
