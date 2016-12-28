package com.udacity.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    @SerializedName("results")
    private List<Video> results;

    public List<Video> getVideos() {
        return results;
    }
}
