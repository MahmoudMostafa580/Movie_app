package com.example.movieapi.ui.main.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.movieapi.databinding.FragmentProductionCompaniesBinding;
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.ui.CompanyMoviesActivity;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.main.adapters.ProductionCompaniesAdapter;
import com.example.movieapi.utils.Credentials;

import java.util.List;
import java.util.logging.Handler;

public class ProductionCompaniesFragment extends Fragment {
    FragmentProductionCompaniesBinding companiesBinding;
    MovieViewModel movieViewModel;
    private Credentials.LoadingDialog loadingDialog;

    public ProductionCompaniesFragment() {
        // Required empty public constructor
    }

    public static ProductionCompaniesFragment newInstance(String param1, String param2) {
        ProductionCompaniesFragment fragment = new ProductionCompaniesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        loadingDialog = new Credentials.LoadingDialog(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        companiesBinding = FragmentProductionCompaniesBinding.inflate(inflater, container, false);
        companiesBinding.companiesProgress.setVisibility(View.VISIBLE);
        loadingDialog.showDialog();
        if (!Credentials.isConnected(getContext())){
            loadingDialog.HideDialog();

            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Cancel", (dialogInterface, i) -> {
                        requireActivity().finish();
                    }).show();
        }else{
            movieViewModel.getCompanies();
            movieViewModel.getCompaniesLiveData().observe(getViewLifecycleOwner(), new Observer<List<CompanyModel>>() {
                @Override
                public void onChanged(List<CompanyModel> companyModels) {
                    if (companyModels != null) {
                        prepareAdapter(companyModels);
                    }
                }
            });
            if (loadingDialog.isShowing()){
                loadingDialog.HideDialog();
            }
        }

        return companiesBinding.getRoot();
    }

    private void prepareAdapter(List<CompanyModel> companyModels) {
        if (!companyModels.isEmpty()) {
            ProductionCompaniesAdapter companiesAdapter = new ProductionCompaniesAdapter();
            companiesAdapter.setList(companyModels);
            companiesBinding.companiesRecycler.setHasFixedSize(true);
            companiesBinding.companiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            companiesBinding.companiesProgress.setVisibility(View.GONE);
            companiesBinding.companiesRecycler.setVisibility(View.VISIBLE);
            companiesBinding.companiesRecycler.setAdapter(companiesAdapter);
            loadingDialog.HideDialog();
            companiesAdapter.setOnCompanyClickListener(new ProductionCompaniesAdapter.OnCompanyClickListener() {
                @Override
                public void onCompanyClick(int position) {
                    int company_id = companyModels.get(position).getId();
                    Intent intent=new Intent(requireActivity(), CompanyMoviesActivity.class);
                    intent.putExtra("COMPANY_ID", company_id);
                    intent.putExtra("COMPANY_NAME", companyModels.get(position).getName());
                    startActivity(intent);
                }
            });
        }
    }
}