package com.example.movieapi.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class MovieModel implements Parcelable {
    private String poster_path;
    private String overview;
    private String release_date;
    private int id;
    private String title;
    private float vote_average;
    private int runtime;
    private List<GenreModel> genres;

    public MovieModel(String title, int id, String overView, float vote_average, String poster_path,
                      String release_date, int runtime, List<GenreModel> genres) {
        this.title = title;
        this.id = id;
        this.overview = overView;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.runtime=runtime;
        this.genres=genres;
    }


    protected MovieModel(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        title = in.readString();
        vote_average = in.readFloat();
        runtime = in.readInt();
        genres = in.createTypedArrayList(GenreModel.CREATOR);
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
        return overview;
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

    public int getRuntime() {
        return runtime;
    }

    public List<GenreModel> getGenres() {
        return genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeFloat(vote_average);
        parcel.writeInt(runtime);
        parcel.writeTypedList(genres);
    }
}
