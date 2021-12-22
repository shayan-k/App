package com.example.myapp_sherkat.classs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponceRest {

    @SerializedName("results")
    private List<Rest> restList;
    private int pages;



    public int getPage() {
        return pages;
    }

    public List<Rest> getRestList() {
        return restList;
    }
}
