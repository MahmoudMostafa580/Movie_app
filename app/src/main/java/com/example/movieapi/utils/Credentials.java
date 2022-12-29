package com.example.movieapi.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.example.movieapi.R;
import com.example.movieapi.pojo.CompanyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Credentials {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "b13e8ea5ebb980fe5fd6a5a83e2b478c";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("production_company_ids_12_21_2022.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<CompanyModel> extractFeaturesFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<CompanyModel> companyList = new ArrayList<>();
        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray companiesList = baseJson.getJSONArray("companies");
            for (int i = 0; i < companiesList.length(); i++) {
                JSONObject currentCompany = companiesList.getJSONObject(i);
                int id = currentCompany.getInt("id");
                String name = currentCompany.getString("name");
                CompanyModel companyModel = new CompanyModel(id, name);
                companyList.add(companyModel);
            }

        } catch (JSONException e) {
            Log.e("company json extraction", "Error while extract json features!", e);
        }
        return companyList;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static class LoadingDialog {
        private Context context;
        private Dialog dialog;

        public LoadingDialog(Context context) {
            this.context = context;
        }

        public void showDialog() {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.loading_custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

        public boolean isShowing(){
             return dialog.isShowing();
        }

        public void HideDialog() {
            dialog.dismiss();
        }

    }

}
