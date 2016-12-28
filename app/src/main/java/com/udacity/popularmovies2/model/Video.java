package com.udacity.popularmovies2.model;

public class Video {

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private String name;
    private String key;

    public String getKey() {
        return YOUTUBE_URL + key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "\nVideo{" + name + " : " + key + "}";
    }
}
