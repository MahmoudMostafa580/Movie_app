package com.example.movieapi.ui.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.movieapi.R;
import com.example.movieapi.databinding.FragmentCategoriesBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.main.adapters.PopularAdapter;
import com.example.movieapi.ui.movieDetails.MovieDetailsActivity;
import com.example.movieapi.utils.Credentials;

import java.util.List;


public class CategoriesFragment extends Fragment {
    FragmentCategoriesBinding categoriesBinding;
    MovieViewModel movieViewModel;
    private GridLayoutManager gridLayoutManager;
    Credentials.LoadingDialog loadingDialog;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new Credentials.LoadingDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        categoriesBinding = FragmentCategoriesBinding.inflate(inflater, container, false);

        return categoriesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayoutManager = new GridLayoutManager(requireContext(), getResources().getInteger(R.integer.grid_column_count));
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        movieViewModel.getSelectedCategory().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (!Credentials.isConnected(getActivity())) {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Internet Connection Alert")
                            .setMessage("Please Check Your Internet Connection")
                            .setPositiveButton("Cancel", (dialogInterface, i) -> {
                                requireActivity().finish();
                            }).show();
                } else {
                    loadMovies(integer);
                    loadingDialog.HideDialog();

                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gridLayoutManager.setSpanCount(getResources().getInteger(R.integer.grid_column_count));
        if (loadingDialog.isShowing()){
            loadingDialog.HideDialog();
        }


    }

    private void loadMovies(Integer genreId) {
        loadingDialog.showDialog();
        movieViewModel.discoverMovies(genreId);
        movieViewModel.getDiscoverMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse moviesResponse) {
                prepareAdapter(moviesResponse);
            }
        });
    }

    private void prepareAdapter(MoviesResponse moviesResponse) {
        List<MovieModel> list = moviesResponse.getMovies();
        if (!list.isEmpty()) {
            PopularAdapter popularAdapter = new PopularAdapter(list, R.layout.movies_by_category_list_item);
            categoriesBinding.categoryMoviesList.setHasFixedSize(true);
            categoriesBinding.categoryMoviesList.setLayoutManager(gridLayoutManager);
            categoriesBinding.categoryMoviesList.setAdapter(popularAdapter);
            loadingDialog.HideDialog();
            popularAdapter.setOnItemClickListener(new PopularAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    int id = list.get(position).getId();
                    Intent intent = new Intent(requireActivity(), MovieDetailsActivity.class);
                    intent.putExtra("MOVIE_ID", id);
                    startActivity(intent);
                }
            });
        }
    }


}