package com.example.movieapi.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.movieapi.R;
import com.example.movieapi.databinding.ActivityShowMoreBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.ui.main.adapters.PopularAdapter;
import com.example.movieapi.ui.main.fragments.AllFragment;
import com.example.movieapi.ui.movieDetails.MovieDetailsActivity;
import com.example.movieapi.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class ShowMoreActivity extends AppCompatActivity {
    ActivityShowMoreBinding showMoreBinding;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMoreBinding=ActivityShowMoreBinding.inflate(getLayoutInflater());
        gridLayoutManager=new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count));
        setContentView(showMoreBinding.getRoot());

        setSupportActionBar(showMoreBinding.toolbar);
        showMoreBinding.toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        Intent intent=getIntent();
        if (intent.hasExtra(AllFragment.POPULAR_LIST_KEY)){
            ArrayList<MovieModel> popularList = intent.getParcelableArrayListExtra(AllFragment.POPULAR_LIST_KEY);
            showMoreBinding.toolbar.setTitle("Popular Movies");
            getSupportActionBar().setTitle("Popular Movies");
            prepareRecycler(popularList);
        }else if (intent.hasExtra(AllFragment.UPCOMING_LIST_KEY)){
            ArrayList<MovieModel> upComingList = intent.getParcelableArrayListExtra(AllFragment.UPCOMING_LIST_KEY);
            getSupportActionBar().setTitle("Upcoming Movies");
            prepareRecycler(upComingList);
        }else if (intent.hasExtra(AllFragment.TOP_RATED_LIST_KEY)){
            ArrayList<MovieModel> topRatedList = intent.getParcelableArrayListExtra(AllFragment.TOP_RATED_LIST_KEY);
            getSupportActionBar().setTitle("Top Rated Movies");
            prepareRecycler(topRatedList);
        }


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gridLayoutManager.setSpanCount(getResources().getInteger(R.integer.grid_column_count));
    }

    private void prepareRecycler(ArrayList<MovieModel> list) {
        Log.v("LIST TAG: ", list.get(0).getTitle());
        PopularAdapter popularAdapter=new PopularAdapter(list, R.layout.movies_by_category_list_item);
        showMoreBinding.intentResultList.setHasFixedSize(true);
        showMoreBinding.intentResultList.setLayoutManager(gridLayoutManager);
        showMoreBinding.intentResultList.setAdapter(popularAdapter);
        popularAdapter.setOnItemClickListener(position -> {
            int id = list.get(position).getId();
            Intent intent=new Intent(ShowMoreActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", id);
            startActivity(intent);
        });
    }
}