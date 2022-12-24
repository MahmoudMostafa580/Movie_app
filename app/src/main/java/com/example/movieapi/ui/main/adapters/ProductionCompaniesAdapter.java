package com.example.movieapi.ui.main.adapters;

import android.text.method.LinkMovementMethod;
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
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.utils.Credentials;

import java.util.ArrayList;
import java.util.List;

public class ProductionCompaniesAdapter extends RecyclerView.Adapter<ProductionCompaniesAdapter.CompanyViewHolder> {
    List<CompanyModel> companiesList=new ArrayList<>();
    private OnCompanyClickListener mListener;
    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.production_company_list_item, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        CompanyModel currentCompany=companiesList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Credentials.IMAGE_URL+currentCompany.getLogo_path())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_movie_clapper)
                .into(holder.companyImg);
        if (currentCompany.getName()==null || currentCompany.getName().isEmpty()){
            holder.company_name.setText("Not available");
        }else{
            holder.company_name.setText(currentCompany.getName());
        }
        if (currentCompany.getHeadquarters()==null|| currentCompany.getHeadquarters().isEmpty()){
            holder.headquarters.setText("Not available");
        }else{
            holder.headquarters.setText(currentCompany.getHeadquarters());
        }
        if (currentCompany.getHomePage()==null|| currentCompany.getHomePage().isEmpty()){
            holder.homepage.setText("Not available");
        }else{
            holder.homepage.setText(currentCompany.getHomePage());
            holder.homepage.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    @Override
    public int getItemCount() {
        return companiesList.size();
    }
    public void setList(List<CompanyModel> list){
        companiesList=list;
        notifyItemRangeChanged(0, list.size());
    }
    public void setOnCompanyClickListener(OnCompanyClickListener listener){
        mListener=listener;
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        ImageView companyImg;
        TextView company_name, headquarters, homepage;

        public CompanyViewHolder(@NonNull View itemView, OnCompanyClickListener listener) {
            super(itemView);
            companyImg=itemView.findViewById(R.id.company_img);
            company_name=itemView.findViewById(R.id.name_txt);
            headquarters=itemView.findViewById(R.id.location_txt);
            homepage=itemView.findViewById(R.id.homepage_link);
            itemView.setOnClickListener(view -> {
                if (listener!=null){
                    int position=getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        listener.onCompanyClick(position);
                    }
                }
            });
        }
    }
    public interface OnCompanyClickListener{
        void onCompanyClick(int position);
    }
}
