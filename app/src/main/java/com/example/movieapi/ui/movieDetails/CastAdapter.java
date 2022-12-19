package com.example.movieapi.ui.movieDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapi.R;
import com.example.movieapi.pojo.CastModel;
import com.example.movieapi.utils.Credentials;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder>{
    private List<CastModel> castList;

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        CastModel currentCast=castList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Credentials.IMAGE_URL+currentCast.getProfile_path())
                .placeholder(R.drawable.ic_person)
                .into(holder.characterImg);

        holder.personName.setText(currentCast.getName());
        holder.characterName.setText(currentCast.getCharacter());

    }

    @Override
    public int getItemCount() {
        return castList.size();
    }
    public void setList(List<CastModel> list){
        castList=list;
        notifyItemRangeChanged(0, castList.size());
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder{
        ImageView characterImg;
        TextView personName, characterName;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            characterImg=itemView.findViewById(R.id.character_img);
            personName=itemView.findViewById(R.id.person_name);
            characterName=itemView.findViewById(R.id.character_name);
        }
    }
}
