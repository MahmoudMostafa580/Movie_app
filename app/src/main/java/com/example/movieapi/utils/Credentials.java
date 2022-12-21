package com.example.movieapi.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

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
            for (int i = 0; i < companiesList.length() ; i++) {
                JSONObject currentCompany=companiesList.getJSONObject(i);
                int id = currentCompany.getInt("id");
                String name = currentCompany.getString("name");
                CompanyModel companyModel=new CompanyModel(id,name);
                companyList.add(companyModel);
            }

        } catch (JSONException e) {
            Log.e("company json extraction","Error while extract json features!", e);
        }
        return companyList;
    }

}
