package com.example.movieapi.ui.login;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository=new AuthRepository(application);
        userLiveData=authRepository.getUserLiveData();
        loggedOutLiveData=authRepository.getLoggedOutLiveData();
    }

    public void signUp(String name, String email, String password, Uri profileImage){
        authRepository.signUp(name, email, password, profileImage);
    }
    public void signIn(String email, String password){
        authRepository.signIn(email, password);
    }
    public void logOut(){
        authRepository.logOut();
    }
    public void changeDisplayName(String newName){
        authRepository.changeDisplayName(newName);
    }
    public void changePassword(String newPass){
        authRepository.changePassword(newPass);
    }
    public void changeProfileImage(Uri profileUri){
        authRepository.changeProfileImage(profileUri);
    }
    public MutableLiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }
    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }


}
