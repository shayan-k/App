package com.example.myapp_sherkat.classs;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {


    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefManager(Context context) {
        if (context != null) {
            sharedPreferences = context.getSharedPreferences("user_shared_pref", Context.MODE_PRIVATE);
            this.context = context;
        }

    }



    public void saveUserInfo(int user_id, String username, String mobile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.putString("username", username);
        editor.putInt("user_id", user_id);
        editor.apply();

    }


    public String getUserPhone() {
        return sharedPreferences.getString("mobile", "");
    }

    public String getUserUsername() {
        return sharedPreferences.getString("username", "");
    }

    public int getUserId() {
        return sharedPreferences.getInt("user_id", -1);
    }


    public void remove() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("mobile");
        editor.remove("username");
        editor.remove("user_id");
        editor.apply();
    }


}

