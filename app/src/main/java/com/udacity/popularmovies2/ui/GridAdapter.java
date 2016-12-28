package com.udacity.popularmovies2.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = GridAdapter.class.getSimpleName();
    public static final String SELECTED_MOVIE_BUNDLE = "GridAdapter.selectedMovie";
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    private ArrayList<Movie> movies;
    private Context context;

    public GridAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new MovieHolder(inflater.inflate(R.layout.grid_item_movie, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.grid_item_loading, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_MOVIE) {
            Glide
                    .with(context)
                    .load(movies.get(position).getPoster_path())
                    .thumbnail(0.25f)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((MovieHolder) holder).poster);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startDetailsActivity = new Intent(context, DetailsActivity.class);
                    startDetailsActivity.putExtra(SELECTED_MOVIE_BUNDLE, movies.get(holder.getAdapterPosition()));
                    context.startActivity(startDetailsActivity);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getId().equals("-1")) {
            return TYPE_LOAD;
        } else {
            return TYPE_MOVIE;
        }
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof MovieHolder) {
            Glide.clear(((MovieHolder) holder).poster);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void reset() {
        movies.clear();
        notifyDataChanged();
    }

    public void refill(List<Movie> list) {
        movies.clear();
        movies.addAll(list);
        notifyDataChanged();
    }

    public void addAll(List<Movie> list) {
        movies.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Movie movie) {
        movies.add(movie);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeLast() {
        movies.remove(getItemCount() - 1);
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grid_item_image)
        ImageView poster;

        MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }
}
