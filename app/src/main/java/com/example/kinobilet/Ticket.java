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

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public void setTotal(int total) {
        this.total = total;
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
