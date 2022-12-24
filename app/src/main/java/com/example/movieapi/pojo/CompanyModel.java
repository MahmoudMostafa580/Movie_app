package com.example.movieapi.pojo;

public class CompanyModel {
    private int id;
    private String name;
    private String logo_path;
    private String headquarters;
    private String homepage;

    public CompanyModel(int id, String name, String logo_path, String headquarters, String homepage) {
        this.id = id;
        this.name = name;
        this.logo_path = logo_path;
        this.headquarters=headquarters;
        this.homepage=homepage;
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

    public String getHeadquarters() {
        return headquarters;
    }

    public String getHomePage() {
        return homepage;
    }
}
