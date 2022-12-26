package com.example.movieapi.ui.main.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.databinding.FragmentProfileBinding;
import com.example.movieapi.ui.favorite.FavoriteActivity;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.login.AuthViewModel;
import com.example.movieapi.ui.login.SignInActivity;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding profileBinding;
    private AuthViewModel authViewModel;
    private MovieViewModel movieViewModel;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private Uri profileImg;
    private Long favoriteCount;


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
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        mSharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();


        authViewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut) {
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                    requireActivity().overridePendingTransition(0, 0);
                    requireActivity().finish();
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);

        profileBinding.editProfileImg.setOnClickListener(view -> {
            editImageLauncher.launch("image/*");
        });
        profileBinding.saveImageBtn.setOnClickListener(view -> {
            authViewModel.changeProfileImage(profileImg);
        });
        profileBinding.changeUserNameBtn.setOnClickListener(view -> {
            showNameEditText();
        });
        profileBinding.changePasswordBtn.setOnClickListener(view -> {
            showPassEditText();
        });
        profileBinding.favoriteCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FavoriteActivity.class);
            intent.putExtra("FAVORITE COUNT", favoriteCount);
            startActivity(intent);
        });
        profileBinding.logOutBtn.setOnClickListener(view -> {
            showDialog();
        });
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    profileBinding.logOutBtn.setEnabled(true);
                    getUserData(firebaseUser);
                    profileBinding.saveImageBtn.setVisibility(View.GONE);
                } else {
                    profileBinding.logOutBtn.setEnabled(false);
                }
            }
        });


        return profileBinding.getRoot();
    }

    ActivityResultLauncher<String> editImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        profileImg = result;
                        Glide.with(requireActivity())
                                .load(profileImg.toString())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(profileBinding.profileImg);

                        profileBinding.saveImageBtn.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(requireActivity(), "Can't pick image!\nPlease try again..", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void showPassEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.change_pass_layout, null);
        EditText passEditText = dialogView.findViewById(R.id.change_pass_et);
        builder.setTitle("Change Password")
                .setView(dialogView)
                .setIcon(R.drawable.ic__edit)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newPass = passEditText.getText().toString();
                    authViewModel.changePassword(newPass);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
        builder.show();
    }

    private void showNameEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.change_name_layout, null);
        EditText nameEditText = dialogView.findViewById(R.id.change_name_et);
        builder.setTitle("Change Display Name")
                .setView(dialogView)
                .setIcon(R.drawable.ic__edit)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newName = nameEditText.getText().toString();
                    authViewModel.changeDisplayName(newName);

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
        builder.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle("LOG OUT")
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

    public void getUserData(FirebaseUser firebaseUser) {
        profileBinding.userNameTxt.setText(firebaseUser.getDisplayName());
        profileBinding.userEmailTxt.setText(firebaseUser.getEmail());
        Glide.with(requireActivity())
                .load(firebaseUser.getPhotoUrl().toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(profileBinding.profileImg);
    }

    @Override
    public void onStart() {
        movieViewModel.getFavoriteCount();
        movieViewModel.getFavoriteCountLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long count) {
                profileBinding.favoriteItemsCount.setText(String.valueOf(count));
                favoriteCount=count;
            }
        });
        super.onStart();
    }
}