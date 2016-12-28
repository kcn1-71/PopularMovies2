package com.udacity.popularmovies2.ui;


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

import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.model.Movie;
import com.udacity.popularmovies2.model.Movies;
import com.udacity.popularmovies2.retrofit.MovieService;
import com.udacity.popularmovies2.retrofit.ServiceGenerator;
import com.udacity.popularmovies2.settings.SettingsActivity;
import com.udacity.popularmovies2.utilities.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GridActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = GridActivity.class.getSimpleName();

    public static final String SHARED_PREF_FAVORITES_ARRAY = "PopularMovies.favoritesArray";
    private static final String BUNDLE_RECYCLER_LAYOUT = "GridActivity.recycler.layout";
    private static final String BUNDLE_ADAPTER = "GridActivity.adapter";
    private static final String BUNDLE_CURRENT_PAGE = "GridActivity.currentPage";

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.loading_error_message)
    TextView errorMessage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    MovieService movieService;

    ArrayHelper arrayHelper;

    GridAdapter adapter;

    SharedPreferences sharedPreferences;
    String orderBy;
    int currentPage;
    ArrayList<Integer> favoritesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        currentPage = 1;
        adapter = new GridAdapter(this, new ArrayList<Movie>());

        arrayHelper = new ArrayHelper(this);
        favoritesId = arrayHelper.getArray(SHARED_PREF_FAVORITES_ARRAY);

        adapter.setLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = adapter.getItemCount() - 1;
                        loadMore();
                    }
                });
            }
        });

        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        movieService = ServiceGenerator.createService(MovieService.class);

        orderBy = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_order_by_top_rated_value));

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_ADAPTER) && savedInstanceState.containsKey(BUNDLE_CURRENT_PAGE)) {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(BUNDLE_ADAPTER);
            currentPage = savedInstanceState.getInt(BUNDLE_CURRENT_PAGE);
            adapter.refill(movies);
            Log.v(LOG_TAG, "SAVED");
        } else {
            loadFirstPage();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("qweqwe", recyclerView.getLayoutManager().onSaveInstanceState().toString());
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_ADAPTER, adapter.getMovies());
        outState.putInt(BUNDLE_CURRENT_PAGE, currentPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
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

    private void loadFirstPage() {
        showLoadingIndicator();
        if (orderBy.equals(getString(R.string.pref_order_by_top_rated_value)) || orderBy.equals(getString(R.string.pref_order_by_popularity_value))) {
            Call<Movies> call = movieService.getPage(orderBy, currentPage);
            call.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Movies movies = response.body();
                    showRecyclerView();
                    adapter.refill(movies.getMovies());
                    currentPage++;
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.v(LOG_TAG, "Shit happens");
                    showErrorMessage();
                }
            });
            Log.v(LOG_TAG, "LOADED");
        } else if (orderBy.equals(getString(R.string.pref_order_by_favorites_value))) {
            adapter.reset();
            if (!favoritesId.isEmpty())
                for (Integer id : favoritesId) {
                    Call<Movie> call = movieService.getMovie(String.valueOf(id));
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            Movie movie = response.body();
                            adapter.add(movie);
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Log.v(LOG_TAG, "Shit happens");
                            showErrorMessage();
                        }
                    });
                }
            showRecyclerView();
        }
    }

    private void loadMore() {
        if (orderBy.equals(getString(R.string.pref_order_by_top_rated_value)) || orderBy.equals(getString(R.string.pref_order_by_popularity_value))) {
            adapter.add(new Movie());
            Call<Movies> call = movieService.getPage(orderBy, currentPage);
            call.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.isSuccessful()) {
                        adapter.removeLast();
                        Movies result = response.body();
                        List<Movie> movies = result.getMovies();
                        if (movies.size() > 0) {
                            adapter.addAll(movies);
                            currentPage++;
                        } else {
                            adapter.setMoreDataAvailable(false);
                        }
                        adapter.notifyDataChanged();
                    } else {
                        Log.e(LOG_TAG, " Load More Response Error " + String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.e(LOG_TAG, " Load More Response Error " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key)) || orderBy.equals(getString(R.string.pref_order_by_favorites_value))) {
            orderBy = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_order_by_top_rated_value));
            currentPage = 1;
            adapter.reset();
            favoritesId = arrayHelper.getArray(SHARED_PREF_FAVORITES_ARRAY);
            loadFirstPage();
        }
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
