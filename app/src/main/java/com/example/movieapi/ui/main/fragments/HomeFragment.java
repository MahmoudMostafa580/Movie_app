package com.example.movieapi.ui.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.databinding.FragmentHomeBinding;
import com.example.movieapi.pojo.GenreModel;
import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.ui.search.SearchActivity;
import com.example.movieapi.ui.login.AuthViewModel;
import com.example.movieapi.ui.MovieViewModel;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentHomeBinding fragmentHomeBinding;
    private AuthViewModel authViewModel;
    private MovieViewModel movieViewModel;


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
        setRetainInstance(true);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.categories_fragments_container, new AllFragment()).commit();

        movieViewModel.getGenres();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        fragmentHomeBinding.categoriesChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                fragmentHomeBinding.categoriesChipGroup.check(R.id.chip_all);
                fragmentHomeBinding.categoriesScrollView.smoothScrollTo(0,0);
            } else if (checkedId == R.id.chip_all) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.categories_fragments_container, new AllFragment()).commit();
            } else {
                group.check(checkedId);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.categories_fragments_container, new CategoriesFragment()).commit();
                movieViewModel.selectCategory(checkedId);
            }
        });

        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                getUserData(firebaseUser);
            }
        });

        movieViewModel.getGenresLiveData().observe(requireActivity(), genresResponse -> {
            if (genresResponse != null) {
                getGenres(inflater, genresResponse);
            } else {
                Log.v("GENRE ERROR:: ", "Error in loading genres!");
            }
        });

        fragmentHomeBinding.searchLayout.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });

        return fragmentHomeBinding.getRoot();
    }

    private void getGenres(LayoutInflater inflater, GenresResponse genresResponse) {
        List<GenreModel> genresList = new ArrayList<>(genresResponse.getGenresList());
        if (!genresList.isEmpty()) {
            for (int i = 0; i < genresList.size(); i++) {
                Chip chip = (Chip) inflater.inflate(R.layout.chip_item, fragmentHomeBinding.categoriesChipGroup, false);
                chip.setText(genresList.get(i).getName());
                chip.setCheckable(true);
                chip.setId(genresList.get(i).getId());
                fragmentHomeBinding.categoriesChipGroup.addView(chip);
            }
        }
    }

    public void getUserData(FirebaseUser firebaseUser) {
        fragmentHomeBinding.userNameTxt.setText(firebaseUser.getDisplayName());
        Glide.with(requireActivity().getApplicationContext())
                .load(Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(fragmentHomeBinding.userImg);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}