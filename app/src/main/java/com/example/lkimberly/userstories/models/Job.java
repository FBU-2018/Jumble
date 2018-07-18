package com.example.lkimberly.userstories.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Job")
public class Job extends ParseObject {

    private  static final String KEY_NAME = "user";



    // Get name
    public ParseUser getUser() {
        return getParseUser(KEY_NAME);
    }

    public void setUser(ParseUser user) {
        put(KEY_NAME, user);
    }

//    public ParseFile getMedia() {
//        return getParseFile(KEY_IMAGE);
//    }
//
//    public void setMedia(ParseFile parseFile) {
//        put(KEY_IMAGE, parseFile);
//    }

    public static class Query extends ParseQuery<Job> {
        public Query() {
            super(Job.class);
        }

        public Query getTop() {
            orderByDescending("createdAt");
            setLimit(20);
            return  this;
        }

//        public Query withUser() {
//            include("user");
//            return this;
//        }
    }

}
