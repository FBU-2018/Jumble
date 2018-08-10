package com.example.lkimberly.userstories.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Ratings")
public class Ratings extends ParseObject {
    private static final String KEY_RATING = "rating";
    private static final String KEY_TOTAL_SCORE = "totalScore";
    private static final String KEY_TIMES_RATED = "timesRated";



    public String getRating() {
        return getString(KEY_RATING);
    }

    public void setRating(String rating) {
        put(KEY_RATING, rating);
    }

    public String getTotalScore() {
        return getString(KEY_TOTAL_SCORE);
    }

    public void setTotalScore(String totalScore) {
        put(KEY_TOTAL_SCORE, totalScore);
    }

    public String getTimesRated() {
        return getString(KEY_TIMES_RATED);
    }

    public void setTimesRated(String timesRated) {
        put(KEY_TIMES_RATED, timesRated);
    }

}
