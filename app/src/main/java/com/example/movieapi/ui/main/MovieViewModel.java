package com.example.movieapi.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapi.data.MovieClient;
import com.example.movieapi.data.MovieRepository;
import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<GenresResponse> genresMutableLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository=new MovieRepository(application);

    }

    public void getPopularMovies() {
        movieRepository.getPopularMovies();
    }

    public void searchMovie(String queue) {
        movieRepository.searchMovie(queue);
    }

    public void getMovieDetails(int id) {
        movieRepository.getMovieDetails(id);
    }

    public void getGenres(){
        genresMutableLiveData=movieRepository.getGenresLiveData();
        movieRepository.getGenres();
    }
    public MutableLiveData<GenresResponse> getGenresLiveData(){
        return genresMutableLiveData;
    }
}
