package com.example.kinobilet;

public class Session {
    private String time;
    private int price;

    public Session() {}

    public Session(String time, int price) {
        this.time = time;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }
}
