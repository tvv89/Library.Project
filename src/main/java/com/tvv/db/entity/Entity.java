package com.tvv.db.entity;

public abstract class Entity {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
