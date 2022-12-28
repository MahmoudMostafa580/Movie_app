package com.example.movieapi.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.movieapi.R;
import com.example.movieapi.databinding.ActivityCompanyMoviesBinding;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.main.adapters.PopularAdapter;
import com.example.movieapi.ui.movieDetails.MovieDetailsActivity;
import com.example.movieapi.utils.Credentials;

public class CompanyMoviesActivity extends AppCompatActivity {

    ActivityCompanyMoviesBinding binding;
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.moviesProgress.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        int company_id = intent.getIntExtra("COMPANY_ID", 0);
        String name = intent.getStringExtra("COMPANY_NAME");
        getSupportActionBar().setTitle(name);
        if (!Credentials.isConnected(this)) {
            binding.moviesProgress.setVisibility(View.INVISIBLE);
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Cancel", (dialogInterface, i) -> {
                        finish();
                    }).show();
        }else{
            movieViewModel.getMoviesByCompany(company_id);
            movieViewModel.getMoviesByCompanyLiveData().observe(this, new Observer<MoviesResponse>() {
                @Override
                public void onChanged(MoviesResponse moviesResponse) {
                    if (moviesResponse.getMovies().size() == 0) {
                        binding.moviesProgress.setVisibility(View.GONE);
                        binding.noMoviesTxt.setVisibility(View.VISIBLE);
                    } else {
                        prepareAdapter(moviesResponse);
                    }
                }
            });
        }

    }

    private void prepareAdapter(MoviesResponse moviesResponse) {
        PopularAdapter popularAdapter = new PopularAdapter(moviesResponse.getMovies(), R.layout.movies_by_category_list_item);
        binding.companyMoviesList.setHasFixedSize(true);
        binding.companyMoviesList.setLayoutManager(new GridLayoutManager(CompanyMoviesActivity.this, 2));
        binding.moviesProgress.setVisibility(View.GONE);
        binding.companyMoviesList.setVisibility(View.VISIBLE);
        binding.companyMoviesList.setAdapter(popularAdapter);
        popularAdapter.setOnItemClickListener(new PopularAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id = moviesResponse.getMovies().get(position).getId();
                Intent intent = new Intent(CompanyMoviesActivity.this, MovieDetailsActivity.class);
                intent.putExtra("MOVIE_ID", id);
                startActivity(intent);
            }
        });
    }
}