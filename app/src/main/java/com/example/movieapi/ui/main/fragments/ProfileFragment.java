package com.example.movieapi.ui.main.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private FragmentProfileBinding profileBinding;
    private AuthViewModel authViewModel;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);

        profileBinding.logOutBtn.setOnClickListener(view -> {
            showDialog();
        });
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
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

    private void showDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity())
                .setTitle("Logout")
                .setMessage("Are you sure to logout?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    authViewModel.logOut();
                    editor.putBoolean("is_logged", false);
                    editor.apply();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
        builder.show();
    }

    public void getUserData(FirebaseUser firebaseUser){
        profileBinding.userNameTxt.setText(firebaseUser.getDisplayName());
        profileBinding.userEmailTxt.setText(firebaseUser.getEmail());
        Log.v("PHOTO TAG: ", firebaseUser.getPhotoUrl().toString());
        Glide.with(requireActivity())
                .load(firebaseUser.getPhotoUrl().toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(profileBinding.profileImg);
    }
}