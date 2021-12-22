package com.example.myapp_sherkat.classs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponcePresent {

    @SerializedName("results")
    private List<Present> presentList;
    private int pages;


    public List<Present> getPresentList() {
        return presentList;
    }

    public int getPage() {
        return pages;
    }
}
