package com.udacity.popularmovies2.retrofit;

import com.udacity.popularmovies2.model.Movie;
import com.udacity.popularmovies2.model.Movies;
import com.udacity.popularmovies2.model.Reviews;
import com.udacity.popularmovies2.model.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("{order_by}")
    Call<Movies> getPage(
            @Path("order_by") String orderBy,
            @Query("page") Integer page);

    @GET("{id}")
    Call<Movie> getMovie(
            @Path("id") String id);

    @GET("{id}/videos")
    Call<Videos> getVideos(
            @Path("id") String id);

    @GET("{id}/reviews")
    Call<Reviews> getReviews(
            @Path("id") String id);
}
