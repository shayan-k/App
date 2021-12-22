package com.example.myapp_sherkat.classs;

import com.google.gson.annotations.SerializedName;

public class User {

    private int id;
    private String username;
    @SerializedName("result")
    private String result_verify;
    private String date;

    public String getDate() {
        return date;
    }
    public int getIduser() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getResult_verify() {
        return result_verify;
    }
}
