package com.udacity.popularmovies2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies2.model.Movie;
import com.udacity.popularmovies2.model.Results;
import com.udacity.popularmovies2.retrofit.MovieService;
import com.udacity.popularmovies2.retrofit.ServiceGenerator;
import com.udacity.popularmovies2.settings.SettingsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GridActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = GridActivity.class.getSimpleName();
    private static final Integer MAX_PAGE = 30;
    private static final String BUNDLE_RECYCLER_LAYOUT = "GridActivity.recycler.layout";
    private static final String BUNDLE_ADAPTER = "GridActivity.adapter";


    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.loading_error_message)
    TextView errorMessage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    MovieService movieService;

    GridAdapter adapter;

    SharedPreferences sharedPreferences;
    String orderBy;
    int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        adapter = new GridAdapter(this, new ArrayList<Movie>());

        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        currentPage = 1;
        movieService = ServiceGenerator.createService(MovieService.class);

        orderBy = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_order_by_top_rated_value));


        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_ADAPTER)) {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(BUNDLE_ADAPTER);
            adapter.refill(movies);
            Log.v(LOG_TAG, "СОХРАНЁННЫЙ");
        } else {
            loadPage(currentPage);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("qweqwe", recyclerView.getLayoutManager().onSaveInstanceState().toString());
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_ADAPTER, adapter.getMovies());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPage(Integer page) {
        showLoadingIndicator();
        if (orderBy.equals(getString(R.string.pref_order_by_top_rated_value)) || orderBy.equals(getString(R.string.pref_order_by_popularity_value))) {
            Call<Results> call = movieService.getPage(orderBy, page);
            call.enqueue(new Callback<Results>() {
                @Override
                public void onResponse(Call<Results> call, Response<Results> response) {
                    Results results = response.body();
                    showRecyclerView();
                    adapter.add(results.getMovies());
                }

                @Override
                public void onFailure(Call<Results> call, Throwable t) {
                    Log.v(LOG_TAG, "Shit happens");
                    showErrorMessage();
                }
            });
            Log.v(LOG_TAG, "ЗАГРУЖЕН");
        } else {
            //TODO
            //load favorites movies from SP
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        orderBy = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_order_by_top_rated_value));
        currentPage = 1;
        adapter.reset();
        loadPage(currentPage);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

}
