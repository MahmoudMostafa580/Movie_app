package com.example.movieapi.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapi.data.MovieRepository;
import com.example.movieapi.pojo.CastResponse;
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private MutableLiveData<Integer> selectedCategoryLiveData = new MutableLiveData<>();


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public void selectCategory(int categoryId) {
        selectedCategoryLiveData.setValue(categoryId);
    }
    public LiveData<Integer> getSelectedCategory() {
        return selectedCategoryLiveData;
    }

    public void getPopularMovies() {
        movieRepository.getPopularMovies();
    }
    public MutableLiveData<MoviesResponse> getPopularLiveData() {
        return movieRepository.getPopularLiveData();
    }

    public void getUpcomingMovies() {
        movieRepository.getUpcomingMovies();
    }
    public LiveData<MoviesResponse> getUpcomingLiveData() {
        return movieRepository.getUpcomingLiveData();
    }

    public void getTopRatedMovies() {
        movieRepository.getTopRatedMovies();
    }
    public LiveData<MoviesResponse> getTopRatedLiveData() {
        return movieRepository.getTopRatedLiveData();
    }

    public void discoverMovies(int genreId) {
        movieRepository.discoverMovies(genreId);
    }
    public MutableLiveData<MoviesResponse> getDiscoverMoviesLiveData() {
        return movieRepository.getDiscoverMoviesLiveData();
    }

    public void searchMovie(String queue) {
        movieRepository.searchMovie(queue);
    }
    public MutableLiveData<MoviesResponse> searchMovieByNameLiveData() {
        return movieRepository.searchMovieByNameLiveData();
    }

    public void getMovieDetails(int id) {
        movieRepository.getMovieDetails(id);
    }
    public MutableLiveData<MovieModel> getDetailsLiveData() {

        return movieRepository.getDetailsLiveData();
    }

    public void getGenres() {
        movieRepository.getGenres();
    }
    public MutableLiveData<GenresResponse> getGenresLiveData() {
        return movieRepository.getGenresLiveData();
    }

    public void getMovieCast(int movieId) {
        movieRepository.getMovieCast(movieId);
    }
    public LiveData<CastResponse> getCastLiveData() {
        return movieRepository.getCastLiveData();
    }

    public void getSimilarMovies(int movieId){
        movieRepository.getSimilarMovies(movieId);
    }
    public LiveData<MoviesResponse> getSimilarMoviesLiveData(){
        return movieRepository.getSimilarMoviesLiveData();
    }

    public void getCompanies(){
        movieRepository.getCompanies();
    }
    public LiveData<List<CompanyModel>> getCompaniesLiveData(){
        return movieRepository.getCompaniesLiveData();
    }
}
