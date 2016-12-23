package com.udacity.popularmovies2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies2.model.Movie;
import com.udacity.popularmovies2.settings.SettingsActivity;
import com.udacity.popularmovies2.utilities.MovieRequestUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = GridActivity.class.getSimpleName();
    private static final int MOVIE_LOADER = 4;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.loading_error_message)
    TextView errorMessage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);

        adapter = new GridAdapter(this, new ArrayList<Movie>());

        Bundle b = new Bundle();
        b.putString("order_by", "top_rated");
        b.putString("page", "1");
        getSupportLoaderManager().initLoader(MOVIE_LOADER, b, this);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        //TODO
        // adapter.clear()
        // restart loader
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> movies;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                showLoadingIndicator();

                if (movies != null) {
                    deliverResult(movies);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                final String ORDER_BY = args.getString("order_by");
                final String PAGE = args.getString("page");
                return MovieRequestUtils.loadMoviesFromTMDB(ORDER_BY, PAGE);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> list) {

        loadingIndicator.setVisibility(View.INVISIBLE);

        if (null == list) {
            showErrorMessage();
        } else {
            adapter.add(list);
            showRecyclerView();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        adapter.reset();
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }


}
