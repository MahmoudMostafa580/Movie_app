package com.example.movieapi.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapi.databinding.FragmentAllBinding;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.ui.main.MovieViewModel;
import com.example.movieapi.ui.main.adapters.PopularAdapter;

import java.util.ArrayList;
import java.util.List;


public class AllFragment extends Fragment {
    FragmentAllBinding allBinding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AllFragment() {
        // Required empty public constructor
    }

    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
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

    private void loadMovies(MoviesResponse moviesResponse, RecyclerView recyclerView) {
        ArrayList<MovieModel> list = new ArrayList<>(moviesResponse.getMovies());
        List<MovieModel> listTemp;
        if (!list.isEmpty()) {
            listTemp= list.subList(0,4);
            PopularAdapter popularAdapter = new PopularAdapter(listTemp);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(popularAdapter);

        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        allBinding = FragmentAllBinding.inflate(inflater, container, false);

        return allBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MovieViewModel movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        //Get popular movies
        movieViewModel.getPopularMovies();
        movieViewModel.getPopularLiveData().observe(getViewLifecycleOwner(), moviesResponse -> {
            if (moviesResponse != null) {
                loadMovies(moviesResponse, allBinding.popularRecycler);
            }
        });

        //Get upcoming movies
        movieViewModel.getUpcomingMovies();
        movieViewModel.getLatestLiveData().observe(getViewLifecycleOwner(), moviesResponse -> {
            if (moviesResponse != null) {
                loadMovies(moviesResponse, allBinding.upComingRecycler);
            }
        });

        //Get Top-rated movies
        movieViewModel.getTopRatedMovies();
        movieViewModel.getTopRatedLiveData().observe(getViewLifecycleOwner(), moviesResponse -> {
            if (moviesResponse!=null){
                loadMovies(moviesResponse, allBinding.topRatedRecycler);
            }
        });

    }
}