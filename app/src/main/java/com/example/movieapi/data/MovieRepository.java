package com.example.movieapi.data;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapi.pojo.CastResponse;
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.pojo.GenresResponse;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.pojo.MoviesResponse;
import com.example.movieapi.utils.Credentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("FieldMayBeFinal")
public class MovieRepository {
    Application application;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    private MutableLiveData<MoviesResponse> popularMutableLiveData;
    private MutableLiveData<MoviesResponse> upcomingMutableLiveData;
    private MutableLiveData<MoviesResponse> topRatedMutableLiveData;
    private MutableLiveData<MoviesResponse> discoverMoviesLiveData;
    private MutableLiveData<MoviesResponse> searchMutableLiveData;
    private MutableLiveData<MovieModel> detailsMutableLiveData;
    private MutableLiveData<GenresResponse> genresMutableLiveData;
    private MutableLiveData<CastResponse> castMutableLiveData;
    private MutableLiveData<MoviesResponse> similarMoviesLiveData;
    private MutableLiveData<List<CompanyModel>> productionCompaniesLiveData;
    private MutableLiveData<MoviesResponse> moviesByCompanyLiveData;
    private MutableLiveData<Boolean> favoriteLiveData;
    private MutableLiveData<Long> favoriteCountLiveData;
    private MutableLiveData<MovieModel> favoriteMoviesLiveData;

    public MovieRepository(Application application) {
        this.application = application;
        this.genresMutableLiveData = new MutableLiveData<>();
        this.popularMutableLiveData = new MutableLiveData<>();
        this.upcomingMutableLiveData = new MutableLiveData<>();
        this.topRatedMutableLiveData = new MutableLiveData<>();
        this.discoverMoviesLiveData = new MutableLiveData<>();
        this.detailsMutableLiveData = new MutableLiveData<>();
        this.searchMutableLiveData = new MutableLiveData<>();
        this.castMutableLiveData = new MutableLiveData<>();
        this.similarMoviesLiveData = new MutableLiveData<>();
        this.productionCompaniesLiveData = new MutableLiveData<>();
        this.moviesByCompanyLiveData = new MutableLiveData<>();
        this.favoriteLiveData = new MutableLiveData<>();
        this.favoriteCountLiveData = new MutableLiveData<>();
        this.favoriteMoviesLiveData = new MutableLiveData<>();
        this.mFirestore = FirebaseFirestore.getInstance();
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }


    public MutableLiveData<MoviesResponse> getPopularLiveData() {
        return popularMutableLiveData;
    }

    public void getPopularMovies() {
        MovieClient.getINSTANCE().getPopularMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                popularMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public LiveData<MoviesResponse> getUpcomingLiveData() {
        return upcomingMutableLiveData;
    }

    public void getUpcomingMovies() {
        MovieClient.getINSTANCE().getUpcomingMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    upcomingMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public LiveData<MoviesResponse> getTopRatedLiveData() {
        return topRatedMutableLiveData;
    }

    public void getTopRatedMovies() {
        MovieClient.getINSTANCE().getTopRatedMovies().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    topRatedMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public void discoverMovies(int genreId) {
        MovieClient.getINSTANCE().discoverMovies(genreId).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    discoverMoviesLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<MoviesResponse> getDiscoverMoviesLiveData() {
        return discoverMoviesLiveData;
    }


    public MutableLiveData<MoviesResponse> searchMovieByNameLiveData() {
        return searchMutableLiveData;
    }

    public void searchMovie(String queue) {
        MovieClient.getINSTANCE().SearchMovie(queue).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    searchMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.v("ERROR TEG: ", t.getMessage());
            }
        });
    }

    public MutableLiveData<MovieModel> getDetailsLiveData() {
        return detailsMutableLiveData;
    }

    public void getMovieDetails(int id) {
        MovieClient.getINSTANCE().getMovieDetails(id).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                detailsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.e("ERROR TAG: ", t.getMessage());
            }
        });
    }

    public MutableLiveData<GenresResponse> getGenresLiveData() {
        return genresMutableLiveData;
    }

    public void getGenres() {
        MovieClient.getINSTANCE().getGenres().enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    genresMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                Log.e("ERROR TAG: ", t.getMessage());
            }
        });
    }


    public void getMovieCast(int id) {
        MovieClient.getINSTANCE().getMovieCast(id).enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    castMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                Log.v("TAG RESPONSE", t.getMessage());
            }
        });
    }

    public LiveData<CastResponse> getCastLiveData() {
        return castMutableLiveData;
    }

    public void getSimilarMovies(int movieId) {
        MovieClient.getINSTANCE().getSimilarMovies(movieId).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    similarMoviesLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.v("ERROR TAG: ", t.getMessage());
            }
        });
    }

    public MutableLiveData<MoviesResponse> getSimilarMoviesLiveData() {
        return similarMoviesLiveData;
    }

    public void getCompanies() {
        String json = Credentials.loadJSONFromAsset(application.getApplicationContext());
        List<CompanyModel> companyModelList = Credentials.extractFeaturesFromJson(json);
        List<CompanyModel> resultList = new ArrayList<>();
        if (companyModelList != null && !companyModelList.isEmpty()) {
            for (int i = 0; i < companyModelList.size(); i++) {
                CompanyModel currentCompany = companyModelList.get(i);
                int id = currentCompany.getId();
                MovieClient.getINSTANCE().getProductionCompanies(id)
                        .enqueue(new Callback<CompanyModel>() {
                            @Override
                            public void onResponse(Call<CompanyModel> call, Response<CompanyModel> response) {
                                if (response.isSuccessful() && response.code() == 200) {
                                    resultList.add(response.body());
                                    Log.v("SIZE TAG ", resultList.size() + "");
                                    if (resultList.size() == companyModelList.size()) {
                                        productionCompaniesLiveData.setValue(resultList);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CompanyModel> call, Throwable t) {
                                Log.v("ERROR TAG: ", t.getMessage());
                            }
                        });
            }
        }
    }

    public LiveData<List<CompanyModel>> getCompaniesLiveData() {
        return productionCompaniesLiveData;
    }

    public void getMoviesByCompany(int companyId) {
        MovieClient.getINSTANCE().getMoviesByCompany(companyId).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    moviesByCompanyLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG: ", "Error in response: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<MoviesResponse> getMoviesByCompanyLiveData() {
        return moviesByCompanyLiveData;
    }

    public void addToFavorite(int movieId) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            String userId = mFirebaseAuth.getCurrentUser().getUid();
            Map<String, Integer> movie = new HashMap<>();
            movie.put("Movie id", movieId);
            mFirestore.collection("users")
                    .document(userId).collection("Favorites")
                    .document(String.valueOf(movieId)).set(movie)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                favoriteLiveData.setValue(true);
                                Toast.makeText(application.getApplicationContext(), "Movie added to favorite successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                favoriteLiveData.setValue(false);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(application.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        favoriteLiveData.setValue(false);
                    });
        } else {
            Toast.makeText(application.getApplicationContext(), "No user exists!", Toast.LENGTH_SHORT).show();
        }
    }

    public MutableLiveData<Boolean> getFavoriteLiveData() {
        return favoriteLiveData;
    }

    public void checkIsFavorite(int movieId) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            String userId = mFirebaseAuth.getCurrentUser().getUid();
            mFirestore.collection("users")
                    .document(userId)
                    .collection("Favorites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    if (documentSnapshot.getReference().getId().equals(String.valueOf(movieId))) {
                                        favoriteLiveData.setValue(true);
                                        return;
                                    } else {
                                        favoriteLiveData.setValue(false);
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(application.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(application.getApplicationContext(), "No user exists!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getFavoriteMoviesCount() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            String userId = mFirebaseAuth.getCurrentUser().getUid();
            CollectionReference collection = mFirestore.collection("users").document(userId).collection("Favorites");
            AggregateQuery countQuery = collection.count();
            countQuery.get(AggregateSource.SERVER)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            long count = task.getResult().getCount();
                            favoriteCountLiveData.setValue(count);
                        } else {
                            Toast.makeText(application.getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(application.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(application.getApplicationContext(), "No user exists!", Toast.LENGTH_SHORT).show();
        }
    }

    public MutableLiveData<Long> getFavoriteCountLiveData() {
        return favoriteCountLiveData;
    }

    public void getFavoriteMovies() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            String userId = mFirebaseAuth.getCurrentUser().getUid();
            mFirestore.collection("users").document(userId).collection("Favorites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<MovieModel> movies = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    MovieClient.getINSTANCE().getMovieDetails(Integer.parseInt(document.getId()))
                                            .enqueue(new Callback<MovieModel>() {
                                                @Override
                                                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                                    if (response.isSuccessful() && response.code() == 200) {
                                                        //movies.add(response.body());
                                                        favoriteMoviesLiveData.setValue(response.body());

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MovieModel> call, Throwable t) {
                                                    Log.e("TAG: ", "Error in response: " + t.getMessage());
                                                }
                                            });
                                }
                                Log.v("SIZE TAAAG ", movies.size()+"");

                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(application.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    public LiveData<MovieModel> getFavoriteMoviesLiveData(){
        return favoriteMoviesLiveData;
    }

}
