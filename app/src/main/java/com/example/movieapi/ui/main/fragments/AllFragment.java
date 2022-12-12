package com.example.movieapi.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private MovieViewModel movieViewModel;
    private PopularAdapter popularAdapter;

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

        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        movieViewModel.getPopularMovies();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void getPopularMovies(MoviesResponse moviesResponse) {
        ArrayList<MovieModel> popularList = new ArrayList<>(moviesResponse.getMovies());
        List<MovieModel> popularTemp;
        if (!popularList.isEmpty()) {
            popularTemp= popularList.subList(0,4);
            allBinding.popularRecycler.setAdapter(popularAdapter);
            allBinding.popularRecycler.setHasFixedSize(true);
            allBinding.popularRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            popularAdapter=new PopularAdapter(popularTemp);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        allBinding = FragmentAllBinding.inflate(inflater, container, false);
        movieViewModel.getPopularMovies();
        movieViewModel.getPopularLiveData().observe(requireActivity(), new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse moviesResponse) {
                if (moviesResponse != null) {
                    getPopularMovies(moviesResponse);
                }
            }
        });
        return allBinding.getRoot();
    }
}