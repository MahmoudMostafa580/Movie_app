package com.example.movieapi.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MovieModel implements Parcelable {
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

    protected MovieModel(Parcel in) {
        poster_path = in.readString();
        overView = in.readString();
        release_date = in.readString();
        id = in.readInt();
        title = in.readString();
        vote_average = in.readFloat();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(overView);
        parcel.writeString(release_date);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeFloat(vote_average);
    }
}
