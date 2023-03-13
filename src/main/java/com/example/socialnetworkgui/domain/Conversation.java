package com.example.socialnetworkgui.domain;

public class Conversation extends Entity<Long> {
    private String name;

    public Conversation(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
