package com.example.movieapi.ui.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.databinding.FragmentHomeBinding;
import com.example.movieapi.pojo.GenreModel;
import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.ui.login.AuthViewModel;
import com.example.movieapi.ui.main.MovieViewModel;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentHomeBinding fragmentHomeBinding;
    private AuthViewModel authViewModel;
    private MovieViewModel movieViewModel;
    private Chip chip;


    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.categories_fragments_container, new AllFragment()).commit();

        authViewModel.getUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    getUserData(firebaseUser);
                }
            }
        });


        movieViewModel.getGenres();
        movieViewModel.getGenresLiveData().observe(getActivity(), new Observer<GenresResponse>() {
            @Override
            public void onChanged(GenresResponse genresResponse) {
                if (genresResponse != null) {
                    getGenres(genresResponse);
                } else {
                    Log.v("GENRE ERROR:: ", "Error in loading genres!");
                }
            }
        });

        return fragmentHomeBinding.getRoot();
    }

    private void getGenres(GenresResponse genresResponse) {
        List<GenreModel> genresList = new ArrayList<>(genresResponse.getGenresList());
        if (!genresList.isEmpty()) {
            for (int i = 0; i <genresList.size() ; i++) {
                chip = (Chip) getLayoutInflater().inflate(R.layout.chip_item, fragmentHomeBinding.categoriesChipGroup, false);
                chip.setText(genresList.get(i).getName());
                chip.setCheckable(true);
                chip.setId(i);
                fragmentHomeBinding.categoriesChipGroup.addView(chip);
            }
        }
    }

    public void getUserData(FirebaseUser firebaseUser) {
        fragmentHomeBinding.userNameTxt.setText(firebaseUser.getDisplayName());
        Glide.with(getActivity().getApplicationContext())
                .load(firebaseUser.getPhotoUrl().toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(fragmentHomeBinding.userImg);
    }
}