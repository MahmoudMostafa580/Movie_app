package com.example.movieapi.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.pojo.MovieModel;
import com.example.movieapi.utils.Credentials;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.searchResultViewHolder> {
    private ArrayList<MovieModel> mList=new ArrayList<>();

    @NonNull
    @Override
    public searchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new searchResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull searchResultViewHolder holder, int position) {
        MovieModel currentMovie = mList.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        holder.movieReleaseDate.setText(currentMovie.getRelease_date());
        if (currentMovie.getPoster_path() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Credentials.IMAGE_URL + currentMovie.getPoster_path())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.ic_movie_place_holder)
                    .into(holder.movieImg);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void setList(ArrayList<MovieModel> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }
    public void clearList(){
        int size=mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class searchResultViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImg;
        TextView movieTitle, movieReleaseDate;

        public searchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImg = itemView.findViewById(R.id.search_movie_img);
            movieTitle = itemView.findViewById(R.id.search_movie_title);
            movieReleaseDate = itemView.findViewById(R.id.search_movie_release_date);
        }
    }
}
