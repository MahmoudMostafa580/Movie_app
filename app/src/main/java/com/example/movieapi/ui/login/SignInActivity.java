package com.example.movieapi.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapi.databinding.ActivitySignInBinding;
import com.example.movieapi.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    public ActivitySignInBinding signInBinding;
    private AuthViewModel authViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(signInBinding.getRoot());


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

        signInBinding.signInBtn.setOnClickListener(view -> {
            String email = signInBinding.emailLayout.getEditText().getText().toString().trim();
            String password = signInBinding.passLayout.getEditText().getText().toString().trim();
            if (isValidData(email, password)) {
                loading(true);
                authViewModel.signIn(email, password);

            }else{
                loading(false);
            }
        });

        signInBinding.createNewAccountTxt.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            finish();
        });

        checkEditTextState();
    }

    public void checkEditTextState(){
        signInBinding.emailLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    signInBinding.emailLayout.setErrorEnabled(true);
                    signInBinding.emailLayout.setError("Please enter your mail!");
                } else {
                    signInBinding.emailLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()) {
                    signInBinding.emailLayout.setErrorEnabled(true);
                    signInBinding.emailLayout.setError("Enter valid mail!");
                } else {
                    signInBinding.emailLayout.setErrorEnabled(false);
                }
            }
        });

        signInBinding.passLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    signInBinding.passLayout.setErrorEnabled(true);
                    signInBinding.passLayout.setError("Please enter your password!");
                } else {
                    signInBinding.passLayout.setErrorEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 10) {
                    signInBinding.passLayout.setErrorEnabled(true);
                    signInBinding.passLayout.setError("Password can't be more than 10 characters!");
                } else {
                    signInBinding.passLayout.setErrorEnabled(false);
                }
            }
        });
    }

    public boolean isValidData(String email, String password) {
        if (email.isEmpty()) {
            signInBinding.emailLayout.setError("Please enter your mail!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInBinding.emailLayout.setError("Enter valid mail!");
            return false;
        } else if (password.isEmpty()) {
            signInBinding.passLayout.setError("Please enter password!");
            return false;
        } else {
            return true;
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void loading(boolean isLoading) {
        if (isLoading) {
            signInBinding.signInProgressBar.setVisibility(View.VISIBLE);
            signInBinding.signInBtn.setVisibility(View.INVISIBLE);
        } else {
            signInBinding.signInProgressBar.setVisibility(View.INVISIBLE);
            signInBinding.signInBtn.setVisibility(View.VISIBLE);
        }
    }


}