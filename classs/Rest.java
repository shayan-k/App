package com.example.myapp_sherkat.classs;

public class Rest {

    private String resultRest;
    private String description;
    private String date_rest;
    private byte rest_status;
    private String  date_created;

    private boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public String getDate_rest() {
        return date_rest;
    }

    public byte getRest_status() {
        return rest_status;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getResultRest() {
        return resultRest;
    }
}
