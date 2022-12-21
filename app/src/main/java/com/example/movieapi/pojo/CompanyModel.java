package com.example.movieapi.pojo;

public class CompanyModel {
    private int id;
    private String name;
    private String logo_path;

    public CompanyModel(int id, String name, String logo_path) {
        this.id = id;
        this.name = name;
        this.logo_path = logo_path;
    }

    public CompanyModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo_path() {
        return logo_path;
    }
}
