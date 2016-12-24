package com.udacity.popularmovies2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.udacity.popularmovies2.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private static final String LOG_TAG = GridAdapter.class.getSimpleName();
    boolean isLoading = false, isMoreDataAvailable = true;

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
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapter.ViewHolder holder, int position) {
        Glide
                .with(context)
                .load(movies.get(position).getPoster_path())
                .thumbnail(0.25f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.poster);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void reset() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void refill(List<Movie> list) {
        movies.clear();
        movies.addAll(list);
        notifyDataSetChanged();
    }

    public void add(List<Movie> list) {
        movies.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grid_item_image)
        ImageView poster;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
