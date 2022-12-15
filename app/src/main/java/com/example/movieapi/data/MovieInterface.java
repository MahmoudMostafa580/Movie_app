package com.example.movieapi.data;

import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {



    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String key);

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(@Query("api_key") String key);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String key);

    @GET("discover/movie")
    Call<MoviesResponse> discoverMovies(@Query("api_key") String key, @Query("with_genres") int genreId);

    @GET("movie/{id}")
    Call<MovieModel> getMovieDetails(@Path("id") int id, @Query("api_key") String key);

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(@Query("api_key") String key);

    @GET("search/movie")
    Call<MoviesResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query
    );
}
