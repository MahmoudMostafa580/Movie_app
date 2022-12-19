package com.example.movieapi.pojo;

public class CastModel {
    private String name;
    private String profile_path;
    private String character;

    public CastModel(String name, String profile_path, String character) {
        this.name = name;
        this.profile_path = profile_path;
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getCharacter() {
        return character;
    }
}
