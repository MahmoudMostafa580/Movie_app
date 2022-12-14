package com.example.movieapi.ui.movieDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.databinding.ActivityMovieDetailsBinding;
import com.example.movieapi.pojo.CastResponse;
import com.example.movieapi.pojo.GenreModel;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.main.adapters.PopularAdapter;
import com.example.movieapi.utils.Credentials;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    ActivityMovieDetailsBinding detailsBinding;
    MovieViewModel movieViewModel;
    CastAdapter castAdapter;
    PopularAdapter popularAdapter;
    Credentials.LoadingDialog loadingDialog;
    int movieId;
    boolean addedToFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(detailsBinding.getRoot());
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        castAdapter = new CastAdapter();
        loadingDialog = new Credentials.LoadingDialog(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        detailsBinding.backCard.setOnClickListener(view -> {
            onBackPressed();
        });

        Intent intent = getIntent();
        if (intent.hasExtra("MOVIE_ID")) {
            movieId = intent.getIntExtra("MOVIE_ID", 0);
            if (movieId != 0) {
                if (!Credentials.isConnected(this)) {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Internet Connection Alert")
                            .setMessage("Please Check Your Internet Connection")
                            .setPositiveButton("Cancel", (dialogInterface, i) -> {
                                finish();
                            }).show();
                } else {
                    loadMovieDetails(movieId);
                    loadingDialog.HideDialog();
                }
            } else {
                Toast.makeText(this, "Can't load movie details!", Toast.LENGTH_SHORT).show();
            }
        }
        movieViewModel.getFavoriteLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavorite) {
                if (isFavorite) {
                    addedToFavorite = true;
                    detailsBinding.favoriteIcon.setColorFilter(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
                } else {
                    addedToFavorite = false;
                    detailsBinding.favoriteIcon.setColorFilter(getResources().getColor(R.color.chip_background_color, getTheme()));
                }
            }
        });
        detailsBinding.addToFavoriteInnerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addedToFavorite) {
                    Toast.makeText(MovieDetailsActivity.this, "Movie already added to favorite list", Toast.LENGTH_SHORT).show();
                } else {
                    detailsBinding.favoriteIcon.setColorFilter(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
                    movieViewModel.addToFavorite(movieId);
                }
            }
        });

    }


    private void loadMovieDetails(int movieId) {
        loadingDialog.showDialog();
        movieViewModel.getMovieDetails(movieId);
        movieViewModel.checkIsFavorite(movieId);
        movieViewModel.getDetailsLiveData().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel movieModel) {
                Glide.with(MovieDetailsActivity.this)
                        .load(Credentials.IMAGE_URL + movieModel.getPoster_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_movie_place_holder)
                        .into(detailsBinding.posterImg);

                detailsBinding.movieTitle.setText(movieModel.getTitle());
                detailsBinding.runTimeValue.setText(movieModel.getRuntime() + " min");
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(1);
                nf.setMinimumFractionDigits(0);
                String rating = nf.format(movieModel.getVote_average());
                detailsBinding.ratingValue.setText(rating);
                detailsBinding.releaseDateValue.setText(movieModel.getRelease_date());
                detailsBinding.overViewTxt.setText(movieModel.getOverView());
                List<GenreModel> movieGenres = movieModel.getGenres();
                if (!movieGenres.isEmpty()) {
                    for (int i = 0; i < movieGenres.size(); i++) {
                        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_item, detailsBinding.genresGroup, false);
                        chip.setText(movieGenres.get(i).getName());
                        chip.setChecked(true);
                        chip.setCheckable(false);
                        chip.setId(movieGenres.get(i).getId());
                        detailsBinding.genresGroup.addView(chip);
                    }
                }
                loadMovieCast(movieId);
                loadMovieSimilarMovies(movieId);
            }
        });
    }

    private void loadMovieCast(int movieId) {
        movieViewModel.getMovieCast(movieId);
        movieViewModel.getCastLiveData().observe(MovieDetailsActivity.this, new Observer<CastResponse>() {
            @Override
            public void onChanged(CastResponse castResponse) {
                castAdapter.setList(castResponse.getCastModelList());
                detailsBinding.castList.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this,
                        LinearLayoutManager.HORIZONTAL, false));
                detailsBinding.castList.setHasFixedSize(true);
                detailsBinding.castList.setAdapter(castAdapter);
            }
        });
    }

    private void loadMovieSimilarMovies(int movieId) {
        movieViewModel.getSimilarMovies(movieId);
        movieViewModel.getSimilarMoviesLiveData().observe(MovieDetailsActivity.this, new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse moviesResponse) {
                List<MovieModel> list = moviesResponse.getMovies();
                popularAdapter = new PopularAdapter(list, R.layout.similar_movies_list_item);
                detailsBinding.similarMoviesList.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this,
                        LinearLayoutManager.HORIZONTAL, false));
                detailsBinding.similarMoviesList.setHasFixedSize(true);
                detailsBinding.similarMoviesList.setAdapter(popularAdapter);
                loadingDialog.HideDialog();
                popularAdapter.setOnItemClickListener(new PopularAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        int mPosition = list.get(position).getId();
                        Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                        intent.putExtra("MOVIE_ID", mPosition);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (loadingDialog.isShowing()){
            loadingDialog.HideDialog();
        }
    }
}