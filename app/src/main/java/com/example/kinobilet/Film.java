package com.example.kinobilet;

import java.io.Serializable;

public class Film implements Serializable {
    private String title, posterUrl, genre, country, description, id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}