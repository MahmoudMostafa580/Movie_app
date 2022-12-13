package com.example.movieapi.data;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("FieldMayBeFinal")
public class MovieRepository {
    private MutableLiveData<MoviesResponse> popularMutableLiveData;
    private MutableLiveData<MoviesResponse> upcomingMutableLiveData;
    private MutableLiveData<MoviesResponse> topRatedMutableLiveData;
    private MutableLiveData<MoviesResponse> searchMutableLiveData;
    private MutableLiveData<MovieModel> detailsMutableLiveData;
    private MutableLiveData<GenresResponse> genresMutableLiveData;

    public MovieRepository(Application application) {
        this.genresMutableLiveData = new MutableLiveData<>();
        this.popularMutableLiveData = new MutableLiveData<>();
        this.upcomingMutableLiveData = new MutableLiveData<>();
        this.topRatedMutableLiveData = new MutableLiveData<>();
        this.detailsMutableLiveData = new MutableLiveData<>();
        this.searchMutableLiveData = new MutableLiveData<>();

    }


    public MutableLiveData<MoviesResponse> getPopularLiveData() {
        return popularMutableLiveData;
    }

    public void getPopularMovies() {
        MovieClient.getINSTANCE().getPopularMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                popularMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public LiveData<MoviesResponse> getUpcomingLiveData() {
        return upcomingMutableLiveData;
    }

    public void getUpcomingMovies() {
        MovieClient.getINSTANCE().getUpcomingMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.code()==200 && response.isSuccessful()){
                    upcomingMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public LiveData<MoviesResponse> getTopRatedLiveData() {
        return topRatedMutableLiveData;
    }

    public void getTopRatedMovies() {
        MovieClient.getINSTANCE().getTopRatedMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.code()==200 && response.isSuccessful()){
                    topRatedMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }


    public MutableLiveData<MoviesResponse> searchMovieByNameLiveData() {
        return searchMutableLiveData;
    }

    public void searchMovie(String queue) {
        MovieClient.getINSTANCE().SearchMovie(queue).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    searchMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.v("ERROR TEG: ", t.getMessage());
            }
        });
    }

    public MutableLiveData<MovieModel> getDetailsLiveData() {
        return detailsMutableLiveData;
    }

    public void getMovieDetails(int id) {
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
    }

    public MutableLiveData<GenresResponse> getGenresLiveData() {
        return genresMutableLiveData;
    }

    public void getGenres() {
        MovieClient.getINSTANCE().getGenres().enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
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