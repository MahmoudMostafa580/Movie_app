package com.example.movieapi.pojo;

public class MovieModel {
    private String poster_path;
    private String overView;
    private String release_date;
    private int id;
    private String title;
    private float vote_average;

    public MovieModel(String title, int id, String overView, float vote_average, String poster_path, String release_date) {
        this.title = title;
        this.id = id;
        this.overView = overView;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getOverView() {
        return overView;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

}
