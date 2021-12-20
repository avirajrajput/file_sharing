package com.manacher.filesharing.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.Context.MODE_PRIVATE;

public class LocalFilesUtil {

    private Activity activity;

    public LocalFilesUtil(Activity context) {
        activity = context;
    }

    public boolean saveCountryCodeInSharedPre(String stringData){

        SharedPreferences sharedPreferences = activity.getSharedPreferences("country_code_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(stringData);
        editor.putString("country_code", json);
        return editor.commit();
    }
    public String getCountryCodeFromSharedPre(){

        String stringData;

        SharedPreferences sharedPreferences = activity.getSharedPreferences("country_code_data", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("country_code", null);
        Type type = new TypeToken<String>() {}.getType();

        stringData = gson.fromJson(json, type);
        return stringData;
    }

    public void saveToken(String token) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("app_token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appToken", token);
        editor.apply();
    }

    public String loadToken() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("app_token", MODE_PRIVATE);
        return sharedPreferences.getString("appToken", "");
    }


}
