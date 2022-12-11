package com.example.movieapi.pojo;

public class GenreModel {

    private int id;
    private String name;

    public GenreModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
