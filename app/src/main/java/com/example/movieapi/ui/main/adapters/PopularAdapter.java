package com.example.movieapi.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.utils.Credentials;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private List<MovieModel> mList;
    private int resourceId;

    public PopularAdapter(List<MovieModel> movieList, int resourceId) {
        mList = movieList;
        this.resourceId=resourceId;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        MovieModel currentMovie = mList.get(position);
        if (currentMovie.getPoster_path() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Credentials.IMAGE_URL + currentMovie.getPoster_path())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.ic_movie_place_holder)
                    .into(holder.movieImg);

        }
        if (currentMovie.getTitle()!=null){
            holder.movieName.setText(currentMovie.getTitle());
        }else{
            holder.movieName.setText("null");
        }
        holder.movieRate.setRating((currentMovie.getVote_average() / 2));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImg;
        TextView movieName;
        RatingBar movieRate;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImg = itemView.findViewById(R.id.movie_img);
            movieName = itemView.findViewById(R.id.movie_title_txt);
            movieRate = itemView.findViewById(R.id.movie_rating_bar);
        }
    }
}
