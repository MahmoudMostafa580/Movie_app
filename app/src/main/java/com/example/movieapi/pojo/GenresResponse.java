package com.example.movieapi.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresResponse {

    @SerializedName("genres")
    @Expose
    private List<GenreModel> genresList;

    public List<GenreModel> getGenresList() {
        return genresList;
    }
}
