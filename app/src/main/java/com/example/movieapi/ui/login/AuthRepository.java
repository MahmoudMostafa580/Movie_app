package com.example.movieapi.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AuthRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageReference;
    private MutableLiveData<FirebaseUser> userLivedata;
    private MutableLiveData<Boolean> loggedOutLivedata;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthRepository(Application application) {
        this.application = application;

        mSharedPreferences = application.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.mStorageReference = FirebaseStorage.getInstance().getReference();
        this.userLivedata = new MutableLiveData<>();
        this.loggedOutLivedata = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            userLivedata.postValue(firebaseAuth.getCurrentUser());
            loggedOutLivedata.postValue(false);
        }
    }

    public void changePassword(String newPass) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userLivedata.postValue(user);
                            Toast.makeText(application.getApplicationContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.v("ERROR KEY", e.getMessage());
                    });
        }
    }

    public void changeDisplayName(String newName){
        FirebaseUser user= firebaseAuth.getCurrentUser();
        if (user!=null){
            UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            userLivedata.postValue(user);
                            Toast.makeText(application.getApplicationContext(), "Display name updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                       Log.v("ERROR KEY", e.getMessage());
                    });
        }
    }
    public void changeProfileImage(Uri profileUri){
        FirebaseUser user= firebaseAuth.getCurrentUser();
        if (user!=null){
            UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                    .setPhotoUri(profileUri)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            userLivedata.postValue(user);
                            Toast.makeText(application.getApplicationContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.v("ERROR KEY", e.getMessage());
                    });
        }
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLivedata;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLivedata;
    }

    public void signUp(String name, String email, String password, Uri profileImage) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            StorageReference imageReference = mStorageReference.child(firebaseAuth.getCurrentUser().getEmail() + "/profileImage.jpg");
                            imageReference.putFile(profileImage)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        imageReference.getDownloadUrl()
                                                .addOnSuccessListener(uri -> {
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(name)
                                                            .setPhotoUri(uri)
                                                            .build();
                                                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                                                    userLivedata.postValue(firebaseAuth.getCurrentUser());
                                                    editor.putBoolean("is_logged", true);
                                                    editor.apply();
                                                    Toast.makeText(application.getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(application, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(application.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(application.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLivedata.postValue(firebaseAuth.getCurrentUser());
                            editor.putBoolean("is_logged", true);
                            editor.apply();
                        } else {
                            Toast.makeText(application.getApplicationContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLivedata.postValue(true);
    }
}
