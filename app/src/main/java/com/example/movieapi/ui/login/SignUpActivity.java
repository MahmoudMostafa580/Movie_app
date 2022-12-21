package com.example.movieapi.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.movieapi.databinding.ActivitySignUpBinding;
import com.example.movieapi.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding signUpBinding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri profileImage;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());


        authViewModel=new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser!=null){
                    loading(false);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

        signUpBinding.imageFrameLayout.setOnClickListener(view -> {
            pickImageLauncher.launch("image/*");
        });
        signUpBinding.signInTxt.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });

        signUpBinding.signUpBtn.setOnClickListener(view -> {
            String name = signUpBinding.nameLayout.getEditText().getText().toString();
            String email = signUpBinding.emailLayout.getEditText().getText().toString();
            String password = signUpBinding.passLayout.getEditText().getText().toString();
            if (isValidData(name, email, password)) {
                loading(true);
                authViewModel.signUp(name, email,password,profileImage);

            } else {
                loading(false);
            }
        });
        checkEditTextState();

    }

    public void checkEditTextState(){
        signUpBinding.nameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    signUpBinding.nameLayout.setErrorEnabled(true);
                    signUpBinding.nameLayout.setError("Please enter your mail!");
                }else{
                    signUpBinding.nameLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                signUpBinding.nameLayout.setErrorEnabled(false);
            }
        });
        signUpBinding.emailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    signUpBinding.emailLayout.setErrorEnabled(true);
                    signUpBinding.emailLayout.setError("Please enter your mail!");
                }else{
                    signUpBinding.emailLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()) {
                    signUpBinding.emailLayout.setErrorEnabled(true);
                    signUpBinding.emailLayout.setError("Enter valid mail!");
                }else{
                    signUpBinding.emailLayout.setErrorEnabled(false);
                }
            }
        });
        signUpBinding.passLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    signUpBinding.passLayout.setErrorEnabled(true);
                    signUpBinding.passLayout.setError("Please enter your password!");
                }else{
                    signUpBinding.passLayout.setErrorEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()>10){
                    signUpBinding.passLayout.setErrorEnabled(true);
                    signUpBinding.passLayout.setError("Password can't be more than 10 characters!");
                }else{
                    signUpBinding.passLayout.setErrorEnabled(false);
                }
            }
        });
    }

    private ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        signUpBinding.addImageTxt.setVisibility(View.GONE);
                        profileImage = result;
                        Glide.with(SignUpActivity.this).load(profileImage).centerCrop().into(signUpBinding.profileImage);
                    }
                }
            }
    );

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidData(String name, String email, String password) {
        if (profileImage == null) {
            showToast("Select profile image!");
            return false;
        } else if (name.trim().isEmpty()) {
            signUpBinding.nameLayout.setError("Please enter your name!");
            return false;
        } else if (email.trim().isEmpty()) {
            signUpBinding.emailLayout.setError("Please enter your mail!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpBinding.emailLayout.setError("Enter valid mail!");
            return false;
        } else if (password.trim().isEmpty()) {
            signUpBinding.passLayout.setError("Please enter password!");
            return false;
        } else {
            return true;
        }
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            signUpBinding.signUpProgressBar.setVisibility(View.VISIBLE);
            signUpBinding.signUpBtn.setVisibility(View.INVISIBLE);
        } else {
            signUpBinding.signUpProgressBar.setVisibility(View.INVISIBLE);
            signUpBinding.signUpBtn.setVisibility(View.VISIBLE);
        }
    }
}