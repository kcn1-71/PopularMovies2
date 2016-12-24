package com.udacity.popularmovies2.retrofit;

import com.udacity.popularmovies2.model.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("{order_by}")
    Call<Results> getPage(
            @Path("order_by") String orderBy,
            @Query("page") Integer page);
}
