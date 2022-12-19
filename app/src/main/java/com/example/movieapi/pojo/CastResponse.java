package com.example.movieapi.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {
    private int id;

    @SerializedName("cast")
    private List<CastModel> castModelList;

    public CastResponse(int id, List<CastModel> castModelList) {
        this.id = id;
        this.castModelList = castModelList;
    }

    public int getId() {
        return id;
    }

    public List<CastModel> getCastModelList() {
        return castModelList;
    }
}
