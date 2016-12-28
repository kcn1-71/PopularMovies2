package com.udacity.popularmovies2.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.databinding.ActivityDetailsBinding;
import com.udacity.popularmovies2.model.Movie;
import com.udacity.popularmovies2.model.Review;
import com.udacity.popularmovies2.model.Reviews;
import com.udacity.popularmovies2.model.Video;
import com.udacity.popularmovies2.model.Videos;
import com.udacity.popularmovies2.retrofit.MovieService;
import com.udacity.popularmovies2.retrofit.ServiceGenerator;
import com.udacity.popularmovies2.utilities.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.linear_videos)
    LinearLayout linearVideos;

    @BindView(R.id.linear_reviews)
    LinearLayout linearReviews;

    private ActivityDetailsBinding binding;

    private ArrayList<Integer> favoritesId;

    private ArrayHelper arrayHelper;

    private MovieService movieService;

    private Movie selectedMovie;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayHelper = new ArrayHelper(this);
        favoritesId = arrayHelper.getArray(GridActivity.SHARED_PREF_FAVORITES_ARRAY);

        movieService = ServiceGenerator.createService(MovieService.class);
        inflater = getLayoutInflater();

        selectedMovie = getIntent().getExtras().getParcelable(GridAdapter.SELECTED_MOVIE_BUNDLE);
        bindMainData(selectedMovie);
        loadVideos();
        loadReviews();
    }

    @OnClick(R.id.button_favorite)
    public void addFavorite(View view) {
        Integer id = Integer.parseInt(selectedMovie.getId());
        if (favoritesId.contains(id)) {
            favoritesId.remove(id);
        } else {
            favoritesId.add(Integer.parseInt(selectedMovie.getId()));
        }
        arrayHelper.saveArray(GridActivity.SHARED_PREF_FAVORITES_ARRAY, favoritesId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindMainData(Movie movie) {

        binding.textviewMovieTitle.setText(movie.getTitle());

        Glide
                .with(this)
                .load(movie.getPoster_path())
                .thumbnail(0.5f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageviewDetailPoster);

        binding.textviewReleaseDate.setText(movie.getRelease_date());

        Float voteAverageDouble = Float.parseFloat(movie.getVote_average());
        binding.textviewVoteAverage.setText(getString(R.string.rating_text_formatted, voteAverageDouble));
        binding.ratingBar.setRating(voteAverageDouble / 2);

        binding.textviewOverview.setText(movie.getOverview());

        if (favoritesId.contains(Integer.parseInt(selectedMovie.getId()))) {
            binding.buttonFavorite.setChecked(true);
        }
        ;

    }

    private void loadVideos() {
        Call<Videos> call = movieService.getVideos(selectedMovie.getId());
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                Videos videos = response.body();
                final List<Video> videoList = videos.getVideos();
                for (final Video video : videoList) {
                    View view = inflater.inflate(R.layout.videos_list_item, null, false);
                    ((TextView) view.findViewById(R.id.textview_video_title)).setText(video.getName());

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            watchYoutubeVideo(video.getKey());
                        }
                    });

                    linearVideos.addView(view);
                }

            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
                Log.v(LOG_TAG, "Shit happens");
            }
        });
    }

    private void loadReviews() {
        Call<Reviews> call = movieService.getReviews(selectedMovie.getId());
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews reviews = response.body();
                final List<Review> reviewList = reviews.getReviews();
                for (final Review review : reviewList) {

                    View view = inflater.inflate(R.layout.reviews_list_item, null, false);
                    ((TextView) view.findViewById(R.id.textview_review)).setText(review.getContent());
                    ((TextView) view.findViewById(R.id.textview_review_by_name)).setText(getString(R.string.by_text_formatted, review.getAuthor()));
                    linearReviews.addView(view);

                }

            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.v(LOG_TAG, "Shit happens");
            }
        });
    }

    private void watchYoutubeVideo(String url) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}