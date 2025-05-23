package com.example.kinobilet;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private String time, id;
    private int price;
    private List<String> banned;

    public Session() {}

    public Session(String time, int price) {
        this.time = time;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getBanned() {
        return banned != null ? banned : new ArrayList<>();
    }

    public void setBanned(List<String> banned) {
        this.banned = banned;
    }
}
