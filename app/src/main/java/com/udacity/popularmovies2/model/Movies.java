package com.udacity.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Movies {

    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getMovies() {
        return results;
    }

}
