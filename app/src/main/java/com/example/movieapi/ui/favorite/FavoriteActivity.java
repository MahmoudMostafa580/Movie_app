package com.example.movieapi.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapi.R;
import com.example.movieapi.databinding.ActivityFavoriteBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.movieDetails.MovieDetailsActivity;

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

        Intent intent = getIntent();
        favoriteCount = intent.getLongExtra("FAVORITE COUNT", 0);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getFavoriteMovies();
        movieViewModel.getFavoriteMoviesLiveData().observe(FavoriteActivity.this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel movieModel) {
                movies.add(movieModel);
                if (movies.size() == favoriteCount) {
                    Log.v("SIZE LIST ", movies.size() + "");
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
        favoriteAdapter.setOnFavoriteItemClickListener(new FavoriteMoviesAdapter.OnFavoriteItemClickListener() {
            @Override
            public void onFavoriteItemClick(int position) {
                int id = movies.get(position).getId();
                Intent intent = new Intent(FavoriteActivity.this, MovieDetailsActivity.class);
                intent.putExtra("MOVIE_ID", id);
                startActivity(intent);
            }

            @Override
            public void onFavoriteMenuClick(int position, View v) {
                PopupMenu popup = new PopupMenu(FavoriteActivity.this, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.remove:
                                int id = movies.get(position).getId();
                                movies.remove(position);
                                favoriteAdapter.notifyItemRemoved(position);
                                movieViewModel.removeMovieFromFavorite(id);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.favorite_menu);
                popup.show();
            }
        });
    }
}