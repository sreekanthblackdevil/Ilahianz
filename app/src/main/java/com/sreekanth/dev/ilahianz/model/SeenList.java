package com.sreekanth.dev.ilahianz.model;

public class SeenList {
    private String id;
    private String time;

    public SeenList(String id, String time) {
        this.id = id;
        this.time = time;
    }

    public SeenList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
