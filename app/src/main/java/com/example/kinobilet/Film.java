package com.example.kinobilet;

import java.io.Serializable;

public class Film implements Serializable {
    private String title, posterUrl, genre, country, description;
    private int cinemaCount;

    public Film() {}

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getGenre() {
        return genre;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public int getCinemaCount() {
        return cinemaCount;
    }
}