package com.example.socialnetworkgui.domain;

public class Entity<ID> {
    protected final ID id;

    public Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

}
