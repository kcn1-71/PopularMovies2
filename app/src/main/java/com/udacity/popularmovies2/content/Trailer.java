package com.udacity.popularmovies2.content;

import android.os.Parcel;
import android.os.Parcelable;

class Trailer implements Parcelable, Comparable<Trailer> {
    private String id;
    private String title;
    private String videoKey;

    public Trailer(String id, String title, String videoKey) {
        this.id = id;
        this.title = title;
        this.videoKey = videoKey;
    }

    public Trailer(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.videoKey = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getVideoKey() {
        return videoKey;
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
        parcel.writeString(title);
        parcel.writeString(videoKey);
    }

    public static final Creator CREATOR = new Creator() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int compareTo(Trailer trailer) {
        return this.id.compareTo(trailer.getId());
    }
}
