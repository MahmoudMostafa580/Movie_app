package com.example.movieapi.ui.movieDetails;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.databinding.ActivityMovieDetailsBinding;
import com.example.movieapi.pojo.CastResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.main.adapters.PopularAdapter;
import com.example.movieapi.utils.Credentials;

public class MovieDetailsActivity extends AppCompatActivity {
    ActivityMovieDetailsBinding detailsBinding;
    MovieViewModel movieViewModel;
    CastAdapter castAdapter;
    PopularAdapter popularAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding=ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(detailsBinding.getRoot());
        movieViewModel=new ViewModelProvider(this).get(MovieViewModel.class);
        castAdapter=new CastAdapter();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        detailsBinding.backCard.setOnClickListener(view -> {
            onBackPressed();
        });

        Intent intent=getIntent();
        if (intent.hasExtra("MOVIE_POSITION")){
            int movieId = intent.getIntExtra("MOVIE_POSITION", 0);
            if (movieId!=0){
                loadMovieDetails(movieId);
            }else{
                Toast.makeText(this, "Can't load movie details!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loadMovieDetails(int movieId) {
        movieViewModel.getMovieDetails(movieId);
        movieViewModel.getDetailsLiveData().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel movieModel) {
                Glide.with(MovieDetailsActivity.this)
                        .load(Credentials.IMAGE_URL+movieModel.getPoster_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_movie_place_holder)
                        .into(detailsBinding.posterImg);

                detailsBinding.movieTitle.setText(movieModel.getTitle());
                detailsBinding.runTimeValue.setText(movieModel.getRuntime()+" min");
                detailsBinding.ratingValue.setText(String.valueOf(movieModel.getVote_average()));
                detailsBinding.releaseDateValue.setText(movieModel.getRelease_date());
                detailsBinding.overViewTxt.setText(movieModel.getOverView());
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
                popularAdapter=new PopularAdapter(moviesResponse.getMovies(), R.layout.similar_movies_list_item);
                detailsBinding.similarMoviesList.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this,
                        LinearLayoutManager.HORIZONTAL, false));
                detailsBinding.similarMoviesList.setHasFixedSize(true);
                detailsBinding.similarMoviesList.setAdapter(popularAdapter);
            }
        });
    }
}