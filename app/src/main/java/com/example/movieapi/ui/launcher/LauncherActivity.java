package com.example.movieapi.ui.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapi.R;
import com.example.movieapi.ui.login.AuthViewModel;
import com.example.movieapi.ui.login.SignInActivity;
import com.example.movieapi.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        splashScreen.setKeepOnScreenCondition(() -> true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mSharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLogged = mSharedPreferences.getBoolean("is_logged", false);
        if (isLogged){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }

    }
}