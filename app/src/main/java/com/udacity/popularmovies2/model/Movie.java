package com.udacity.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private static final String LOG_TAG = Movie.class.getSimpleName();
    private static final String HTTP_IMAGE_TMDB = "http://image.tmdb.org/t/p/w300";

    private String id;
    private String title;
    private String poster_path;
    private String overview;
    private Double vote_average;
    private String release_date;

    public Movie(String id, String title, String poster_path, String overview, Double vote_average, String release_date) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return HTTP_IMAGE_TMDB + poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeDouble(vote_average);
        parcel.writeString(release_date);
    }

    public static final Creator CREATOR = new Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "\n\nMovie{" +
                "\nid='" + id + '\'' +
                ", \ntitle='" + title + '\'' +
                ", \nposter_path='" + poster_path + '\''+
                ", \noverview='" + overview + '\''+
                ", \nvote_average=" + vote_average +
                ", \nrelease_date='" + release_date + '\'' +
                '}';
    }
}
