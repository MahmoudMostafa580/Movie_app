package com.example.movieapi.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    @Expose
    private int total_results;

    @SerializedName("results")
    @Expose
    private List<MovieModel> movies;

    public int getTotal_results() {
        return total_results;
    }

    public List<MovieModel> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "total_results=" + total_results +
                ", movies=" + movies +
                '}';
    }
}
