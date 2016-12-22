package com.udacity.popularmovies2.content;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

class Movie implements Parcelable {

    private static final String LOG_TAG = Movie.class.getSimpleName();

    private String id;
    private String title;
    private String posterPath;
    private String overview;
    private Double rating;
    private String releaseDate;
    private Set<Trailer> trailers;

    public Movie(String id, String title, String posterPath, String overview, Double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.rating = in.readDouble();
        this.releaseDate = in.readString();
    }

    public Movie(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Set<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(Set<Trailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
    }

    public static final Creator CREATOR = new Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
