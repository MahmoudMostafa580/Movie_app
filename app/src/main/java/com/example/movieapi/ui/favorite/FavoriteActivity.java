package com.example.movieapi.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapi.databinding.ActivityFavoriteBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.ui.MovieViewModel;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding favoriteBinding;
    private MovieViewModel movieViewModel;
    private ArrayList<MovieModel> movies = new ArrayList<>();
    private Long favoriteCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteBinding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(favoriteBinding.getRoot());
        setSupportActionBar(favoriteBinding.toolbar);
        favoriteBinding.toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        Intent intent =getIntent();
        favoriteCount = intent.getLongExtra("FAVORITE COUNT",0);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getFavoriteMovies();
        movieViewModel.getFavoriteMoviesLiveData().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel movieModel) {
                movies.add(movieModel);
                if (movies.size() == favoriteCount){
                    Log.v("SIZE LIST ",movies.size()+"");
                    prepareAdapter(movies);
                }
            }
        });

    }

    private void prepareAdapter(ArrayList<MovieModel> movies) {
        FavoriteMoviesAdapter favoriteAdapter = new FavoriteMoviesAdapter();
        favoriteBinding.favoriteRecycler.setLayoutManager(new LinearLayoutManager(this));
        favoriteBinding.favoriteRecycler.setHasFixedSize(true);
        favoriteAdapter.setList(movies);
        favoriteBinding.favoriteRecycler.setAdapter(favoriteAdapter);
    }

}