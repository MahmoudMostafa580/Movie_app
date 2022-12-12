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
    private MutableLiveData<MoviesResponse> popularMutableLiveData;
    private MutableLiveData<MoviesResponse> searchMutableLiveData;
    private MutableLiveData<MovieModel> detailsMutableLiveData;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository=new MovieRepository(application);
    }

    public void getPopularMovies() {
        popularMutableLiveData=movieRepository.getPopularLiveData();
        movieRepository.getPopularMovies();
    }
    public MutableLiveData<MoviesResponse> getPopularLiveData(){
        return popularMutableLiveData;
    }

    public void searchMovie(String queue) {
        searchMutableLiveData=movieRepository.searchMovieByNameLiveData();
        movieRepository.searchMovie(queue);
    }
    public MutableLiveData<MoviesResponse> searchMovieByNameLiveData(){
        return searchMutableLiveData;
    }

    public void getMovieDetails(int id) {
        detailsMutableLiveData=movieRepository.getDetailsLiveData();
        movieRepository.getMovieDetails(id);
    }
    public MutableLiveData<MovieModel> getDetailsLiveData(){
        return detailsMutableLiveData;
    }

    public void getGenres(){
        genresMutableLiveData=movieRepository.getGenresLiveData();
        movieRepository.getGenres();
    }
    public MutableLiveData<GenresResponse> getGenresLiveData(){
        return genresMutableLiveData;
    }
}
