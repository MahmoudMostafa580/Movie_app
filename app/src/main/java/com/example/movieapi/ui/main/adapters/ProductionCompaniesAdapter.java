package com.example.movieapi.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movieapi.R;
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.utils.Credentials;

import java.util.ArrayList;
import java.util.List;

public class ProductionCompaniesAdapter extends RecyclerView.Adapter<ProductionCompaniesAdapter.CompanyViewHolder> {
    List<CompanyModel> companiesList=new ArrayList<>();
    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.production_company_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        CompanyModel currentCompany=companiesList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Credentials.IMAGE_URL+currentCompany.getLogo_path())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_movie_clapper)
                .into(holder.companyImg);

    }

    @Override
    public int getItemCount() {
        return companiesList.size();
    }
    public void setList(List<CompanyModel> list){
        companiesList=list;
        notifyItemRangeChanged(0, list.size());
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        ImageView companyImg;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyImg=itemView.findViewById(R.id.company_img);
            itemView.setOnClickListener(view -> {

            });
        }
    }
}
