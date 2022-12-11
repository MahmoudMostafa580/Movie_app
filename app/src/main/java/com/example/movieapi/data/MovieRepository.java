package com.example.movieapi.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private Application application;
    private MutableLiveData<MoviesResponse> responseMutableLiveData;
    private MutableLiveData<MoviesResponse> searchMutableLiveData;
    private MutableLiveData<MovieModel> detailsMutableLiveData;
    private MutableLiveData<GenresResponse> genresMutableLiveData;

    public MovieRepository(Application application){
        this.application=application;
        this.genresMutableLiveData=new MutableLiveData<>();
    }


    public LiveData<MoviesResponse> getPopularMovies() {
        responseMutableLiveData = new MutableLiveData<>();
        MovieClient.getINSTANCE().getPopularMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                responseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: "+t.getMessage());
            }
        });
        return responseMutableLiveData;
    }

    public LiveData<MoviesResponse> searchMovie(String queue){
        searchMutableLiveData = new MutableLiveData<>();
        MovieClient.getINSTANCE().SearchMovie(queue).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.code()== 200 && response.isSuccessful()){
                    searchMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.v("ERROR TEG: ", t.getMessage());
            }
        });
        return searchMutableLiveData;
    }

    public LiveData<MovieModel> getMovieDetails(int id){
        detailsMutableLiveData = new MutableLiveData<>();
        MovieClient.getINSTANCE().getMovieDetails(id).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                detailsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.e("ERROR TAG: ", t.getMessage());
            }
        });
        return detailsMutableLiveData;
    }
    public MutableLiveData<GenresResponse> getGenresLiveData(){
        return genresMutableLiveData;
    }

    public void getGenres(){
        MovieClient.getINSTANCE().getGenres().enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.code()==200 && response.isSuccessful()){
                    genresMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                Log.e("ERROR TAG: ", t.getMessage());
            }
        });
    }


}
