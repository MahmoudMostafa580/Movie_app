package com.example.movieapi.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.movieapi.databinding.ActivitySearchBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.main.MovieViewModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding searchBinding;
    private SearchResultAdapter searchAdapter;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(searchBinding.getRoot());
        movieViewModel=new ViewModelProvider(this).get(MovieViewModel.class);

        setSupportActionBar(searchBinding.toolbar);
        searchBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchAdapter=new SearchResultAdapter();
        searchBinding.searchResultList.setHasFixedSize(true);
        searchBinding.searchResultList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchBinding.searchResultList.setAdapter(searchAdapter);
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        searchBinding.searchResultList.addItemDecoration(itemDecoration);
        setupSearchMovie();
    }

    private void setupSearchMovie() {
        searchBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()==0){
                    searchAdapter.clearList();
                }else{
                    movieViewModel.searchMovie(query);
                    movieViewModel.searchMovieByNameLiveData().observe(SearchActivity.this, new Observer<MoviesResponse>() {
                        @Override
                        public void onChanged(MoviesResponse moviesResponse) {
                            if (!moviesResponse.getMovies().isEmpty()){
                                searchAdapter.setList((ArrayList<MovieModel>) moviesResponse.getMovies());
                            }
                        }
                    });

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()==0){
                    searchAdapter.clearList();
                }else{
                    movieViewModel.searchMovie(newText);
                    movieViewModel.searchMovieByNameLiveData().observe(SearchActivity.this, new Observer<MoviesResponse>() {
                        @Override
                        public void onChanged(MoviesResponse moviesResponse) {
                            if (!moviesResponse.getMovies().isEmpty()){
                                searchAdapter.setList((ArrayList<MovieModel>) moviesResponse.getMovies());
                            }
                        }
                    });
                }

                return false;
            }
        });
    }
}