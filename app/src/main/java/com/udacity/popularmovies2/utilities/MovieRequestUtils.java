package com.udacity.popularmovies2.utilities;


import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies2.BuildConfig;
import com.udacity.popularmovies2.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieRequestUtils {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final static String MOVIE_BASE_URL =
            "http://api.themoviedb.org/3/movie/";

    private final static String APPID_PARAM = "api_key";

    private final static String PAGE_PARAM = "page";

    private static URL buildUrl(String orderBy, String page) {

        String MOVIE_ORDER_BY = MOVIE_BASE_URL + orderBy + "?";

        Uri builtUri = Uri.parse(MOVIE_ORDER_BY).buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.IMDB_API_KEY)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.v("URL",url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static List<Movie> getMovieDataFromJson(String moviesJsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_ORIGINAL_TITLE = "original_title";
        final String TMDB_POPULARITY = "vote_average";
        final String TMDB_OVERVIEW = "overview";
        final String HTTP_IMAGE_TMDB = "http://image.tmdb.org/t/p/w500";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject movie = moviesArray.getJSONObject(i);

            String posterPath = HTTP_IMAGE_TMDB + movie.getString(TMDB_POSTER_PATH);
            String releaseDate = movie.getString(TMDB_RELEASE_DATE);
            String movieId = movie.getString(TMDB_MOVIE_ID);
            String originalTitle = movie.getString(TMDB_ORIGINAL_TITLE);
            double rating = movie.getDouble(TMDB_POPULARITY);
            String overview = movie.getString(TMDB_OVERVIEW);

            movies.add(new Movie(movieId, originalTitle, posterPath, overview, rating, releaseDate));

        }
        return movies;
    }

    public static List<Movie> loadMoviesFromTMDB(String orderBy, String page){
        String rawJson = null;
        try {
            URL url = buildUrl(orderBy, page);
            rawJson = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return getMovieDataFromJson(rawJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}









