package com.example.socialnetworkgui.domain;

import java.io.Serializable;

public class Entity<ID>   {
    protected final ID id;

    public ID getId() {
        return id;
    }

    public Entity(ID id) {
        this.id = id;
    }

}
