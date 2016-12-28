package com.udacity.popularmovies2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {

    @SerializedName("results")
    private List<Review> results;

    public List<Review> getReviews() {
        return results;
    }
}
