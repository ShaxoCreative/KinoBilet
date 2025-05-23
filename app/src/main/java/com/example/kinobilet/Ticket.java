package com.example.kinobilet;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {
    private String filmId, filmTitle, date, time, code, id;
    private List<String> seats;
    private int total;

    public Ticket() {}

    public void setId(String id) {
        this.id = id;
    }

    public String getFilmId() {
        return filmId;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public List<String> getSeats() {
        return seats;
    }

    public int getTotal() {
        return total;
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }
}
