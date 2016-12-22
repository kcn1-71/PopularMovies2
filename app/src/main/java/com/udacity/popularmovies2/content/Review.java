package com.udacity.popularmovies2.content;

import android.os.Parcel;
import android.os.Parcelable;


class Review implements Parcelable, Comparable<Review> {
    private String id;
    private String name;
    private String rate;
    private String text;

    public Review(String id, String name, String rate, String text) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.text = text;
    }

    public Review(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.rate = in.readString();
        this.text = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(rate);
        parcel.writeString(text);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int compareTo(Review review) {
        return this.getId().compareTo(review.getId());
    }


}
