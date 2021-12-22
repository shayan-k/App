package com.example.myapp_sherkat.classs;

public class Present {

    private String start_at;
    private String end_at;
    private String date_present;
    private String repoart;
    private String statePresent;
    private boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getRepoart() {
        return repoart;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getDate_present() {
        return date_present;
    }

    public String getStatePresent() {
        return statePresent;
    }

}
