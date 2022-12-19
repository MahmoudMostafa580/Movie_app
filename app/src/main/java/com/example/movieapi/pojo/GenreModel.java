package com.example.movieapi.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class GenreModel implements Parcelable {

    private int id;
    private String name;

    public GenreModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected GenreModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<GenreModel> CREATOR = new Creator<GenreModel>() {
        @Override
        public GenreModel createFromParcel(Parcel in) {
            return new GenreModel(in);
        }

        @Override
        public GenreModel[] newArray(int size) {
            return new GenreModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
