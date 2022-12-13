package com.example.movieapi.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movieapi.R;
import com.example.movieapi.databinding.ActivityMainBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.main.fragments.FavoriteFragment;
import com.example.movieapi.ui.main.fragments.HomeFragment;
import com.example.movieapi.ui.main.fragments.ProfileFragment;
import com.example.movieapi.ui.main.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    MovieViewModel movieViewModel;
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_favorite));
        mainBinding.bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        mainBinding.bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_person));
        mainBinding.bottomNav.show(2, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        addFragmentsToBottomNav();

    }

    private void addFragmentsToBottomNav() {
        mainBinding.bottomNav.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
            }
            return null;
        });


        mainBinding.bottomNav.setOnReselectListener(model -> null);
    }


    /*public void getMovieDetails(int id) {
        movieViewModel.getMovieDetails(id).observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel movieModel) {
                Log.v("TAG: ", movieModel.getTitle());
            }
        });
    }

    public void searchMovies(String queue) {
        movieViewModel.searchMovie(queue).observe(this, new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse moviesResponse) {
                List<MovieModel> mList = new ArrayList<>(moviesResponse.getMovies());
                for (MovieModel movie : mList) {
                    Log.v("SEARCH TAG : ", movie.getTitle());
                }
            }
        });
    }*/
}