package com.example.movieapi.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapi.pojo.CastResponse;
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.utils.Credentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClient {

    private final MovieInterface movieInterface;
    private static MovieClient INSTANCE;
    MutableLiveData<MoviesResponse> mMovies;

    public MovieClient(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieInterface =retrofit.create(MovieInterface.class);

        mMovies=new MutableLiveData<>();
    }

    public static MovieClient getINSTANCE() {
        if (INSTANCE==null){
            INSTANCE =new MovieClient();
        }
        return INSTANCE;
    }

    public Call<MoviesResponse> getPopularMovies(){
        return movieInterface.getPopularMovies(Credentials.API_KEY);
    }

    public Call<MoviesResponse> getUpcomingMovies(){
        return movieInterface.getUpcomingMovies(Credentials.API_KEY);
    }

    public Call<MoviesResponse> getTopRatedMovies(){
        return movieInterface.getTopRatedMovies(Credentials.API_KEY);
    }

    public Call<MoviesResponse> discoverMovies(int genreId){
        return movieInterface.discoverMovies(Credentials.API_KEY, genreId);
    }

    public Call<MoviesResponse> SearchMovie(String queue){
        return movieInterface.searchMovie(Credentials.API_KEY, queue);
    }

    public Call<MovieModel> getMovieDetails(int id){
        return movieInterface.getMovieDetails(id, Credentials.API_KEY);
    }

    public Call<GenresResponse> getGenres(){
        return movieInterface.getGenres(Credentials.API_KEY);
    }

    public Call<CastResponse> getMovieCast(int movie_id){
        return movieInterface.getMovieCast(movie_id, Credentials.API_KEY);
    }

    public Call<MoviesResponse> getSimilarMovies(int movieId){
        return movieInterface.getSimilarMovies(movieId, Credentials.API_KEY);
    }
    public Call<CompanyModel> getProductionCompanies(int id){
        return movieInterface.getProductionCompanies(id, Credentials.API_KEY);
    }

    public Call<MoviesResponse> getMoviesByCompany(int companyId){
        return movieInterface.getMoviesByCompany(Credentials.API_KEY,companyId);
    }
}
