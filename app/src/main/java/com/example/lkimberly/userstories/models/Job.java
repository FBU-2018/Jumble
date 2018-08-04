package com.example.lkimberly.userstories.models;



import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Job")
public class Job  extends ParseObject {

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_ESTIMATION = "estimation";

    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private static final String KEY_MONEY = "money";

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getTime() {
        return getString(KEY_TIME);
    }

    public void setTime(String time) {
        put(KEY_TIME, time);
    }

    public String getDate() {
        return getString(KEY_DATE);
    }

    public void setDate(String date) {
        put(KEY_DATE, date);
    }

    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION, location);
    }

    public String getEstimation() {
        return getString(KEY_ESTIMATION);
    }

    public void setEstimation(String estimation) {
        put(KEY_ESTIMATION, estimation);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public static String getLatitude() {
        return KEY_LATITUDE;
    }

    public void setLatitude(String latitude) {
        put(KEY_LATITUDE, latitude);
    }

    public static String getLongitude() {
        return KEY_LONGITUDE;
    }

    public void setLongitude(String longitude) {
        put(KEY_LONGITUDE, longitude);
    }

    public static String getMoney() {
        return KEY_MONEY;
    }

    public void setMoney(String money) {
        put(KEY_MONEY, money);
    }

    public static class Query extends ParseQuery<Job> {

        public Query() {
            super(Job.class);
        }

        public Query getTop() {
            orderByDescending("createdAt");
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}