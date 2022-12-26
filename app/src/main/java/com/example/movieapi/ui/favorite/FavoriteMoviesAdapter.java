package com.example.movieapi.ui.favorite;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.movieapi.R;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.utils.Credentials;

import java.text.NumberFormat;
import java.util.ArrayList;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMovieViewHolder> {
    private ArrayList<MovieModel> moviesList;
    private OnFavoriteItemClickListener mListener;

    @NonNull
    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteMovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_movie_list_item, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieViewHolder holder, int position) {
        MovieModel currentMovie = moviesList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Credentials.IMAGE_URL + currentMovie.getPoster_path())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_movie_place_holder)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.movieImg.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        holder.movieName.setText(currentMovie.getTitle());
        holder.movieRuntime.setText(currentMovie.getRuntime() + " min");

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(0);
        String rating = nf.format(currentMovie.getVote_average());
        holder.movieRatingTxt.setText(rating);

        holder.movieRatingBar.setRating((currentMovie.getVote_average() / 2));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setList(ArrayList<MovieModel> mList) {
        this.moviesList = mList;
        notifyItemRangeChanged(0, mList.size());
    }

    public void setOnFavoriteItemClickListener(OnFavoriteItemClickListener listener) {
        this.mListener = listener;
    }

    public static class FavoriteMovieViewHolder extends RecyclerView.ViewHolder {

        View movieImg;
        TextView movieName, movieRuntime, movieRatingTxt;
        RatingBar movieRatingBar;
        CardView menu;

        public FavoriteMovieViewHolder(@NonNull View itemView, OnFavoriteItemClickListener listener) {
            super(itemView);
            movieImg = itemView.findViewById(R.id.movie_img);
            movieName = itemView.findViewById(R.id.movie_name);
            movieRuntime = itemView.findViewById(R.id.time_value);
            movieRatingTxt = itemView.findViewById(R.id.rating_txt);
            movieRatingBar = itemView.findViewById(R.id.movie_rating_bar);
            menu = itemView.findViewById(R.id.favorite_menu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavoriteItemClick(position);
                        }
                    }
                }
            });
            menu.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteMenuClick(position, view);
                    }
                }
            });
        }
    }

    public interface OnFavoriteItemClickListener {
        void onFavoriteItemClick(int position);
        void onFavoriteMenuClick(int position, View v);
    }
}
