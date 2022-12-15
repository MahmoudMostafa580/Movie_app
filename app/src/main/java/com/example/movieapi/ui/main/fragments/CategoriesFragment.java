package com.example.movieapi.ui.main.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapi.R;
import com.example.movieapi.databinding.FragmentCategoriesBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.main.MovieViewModel;
import com.example.movieapi.ui.main.adapters.PopularAdapter;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment {
    FragmentCategoriesBinding categoriesBinding;
    MovieViewModel movieViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        categoriesBinding=FragmentCategoriesBinding.inflate(inflater, container, false);

        return categoriesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieViewModel=new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        movieViewModel.getSelectedCategory().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadMovies(integer);
            }
        });

    }

    private void loadMovies(Integer genreId) {
        movieViewModel.discoverMovies(genreId);
        movieViewModel.getDiscoverMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse moviesResponse) {
                prepareAdapter(moviesResponse);
            }
        });
    }

    private void prepareAdapter(MoviesResponse moviesResponse) {
        ArrayList<MovieModel> list = new ArrayList<>(moviesResponse.getMovies());
        List<MovieModel> listTemp;
        if (!list.isEmpty()) {
            listTemp= list.subList(0,10);
            PopularAdapter popularAdapter = new PopularAdapter(listTemp, R.layout.movies_by_category_list_item);
            categoriesBinding.categoryMoviesList.setHasFixedSize(true);
            categoriesBinding.categoryMoviesList.setLayoutManager(new GridLayoutManager(getContext(),2));
            categoriesBinding.categoryMoviesList.setAdapter(popularAdapter);

        }
    }
}