package com.example.movieapi.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.movieapi.databinding.FragmentProductionCompaniesBinding;
import com.example.movieapi.pojo.CompanyModel;
import com.example.movieapi.ui.MovieViewModel;
import com.example.movieapi.ui.main.adapters.ProductionCompaniesAdapter;

import java.util.List;

public class ProductionCompaniesFragment extends Fragment {
    FragmentProductionCompaniesBinding companiesBinding;
    MovieViewModel movieViewModel;

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
        movieViewModel.getCompanies();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        companiesBinding = FragmentProductionCompaniesBinding.inflate(inflater, container, false);
        movieViewModel.getCompaniesLiveData().observe(getViewLifecycleOwner(), new Observer<List<CompanyModel>>() {
            @Override
            public void onChanged(List<CompanyModel> companyModels) {
                if (companyModels != null) {
                    prepareAdapter(companyModels);
                }
            }
        });
        return companiesBinding.getRoot();
    }

    private void prepareAdapter(List<CompanyModel> companyModels) {
        if (!companyModels.isEmpty()) {
            ProductionCompaniesAdapter companiesAdapter = new ProductionCompaniesAdapter();
            companiesAdapter.setList(companyModels);
            companiesBinding.companiesRecycler.setHasFixedSize(true);
            companiesBinding.companiesRecycler.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
            companiesBinding.companiesRecycler.setAdapter(companiesAdapter);
        }
    }
}