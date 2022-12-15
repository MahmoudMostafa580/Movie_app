package com.example.movieapi.ui.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.databinding.FragmentProfileBinding;
import com.example.movieapi.ui.login.AuthViewModel;
import com.example.movieapi.ui.login.SignInActivity;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentProfileBinding profileBinding;
    private AuthViewModel authViewModel;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel=new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        mSharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();


        authViewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut){
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                    getActivity().overridePendingTransition(0,0);
                    getActivity().finish();
                }
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);

        profileBinding.logOutBtn.setOnClickListener(view -> {
            authViewModel.logOut();
            editor.putBoolean("is_logged", false);
            editor.apply();
        });
        authViewModel.getUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser!=null){
                    profileBinding.logOutBtn.setEnabled(true);
                    getUserData(firebaseUser);
                }else{
                    profileBinding.logOutBtn.setEnabled(false);
                }
            }
        });
        return profileBinding.getRoot();
    }
    public void getUserData(FirebaseUser firebaseUser){
        profileBinding.userNameTxt.setText(firebaseUser.getDisplayName());
        profileBinding.userEmailTxt.setText(firebaseUser.getEmail());
        Log.v("PHOTO TAG: ", firebaseUser.getPhotoUrl().toString());
        Glide.with(getContext())
                .load(firebaseUser.getPhotoUrl().toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(profileBinding.profileImg);
    }
}