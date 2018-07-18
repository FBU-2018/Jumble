package com.example.lkimberly.userstories.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Matches")
public class Matches extends ParseObject {

    private  static final String KEY_JOB_POSTER = "jobPoster";
    private  static final String KEY_JOB_SUBSCRIBER = "jobSubscriber";
    private  static final String KEY_JOB = "job";



    // Get job poster
    public ParseUser getJobPoster() {
        return getParseUser(KEY_JOB_POSTER);
    }

    public void setJobPoster(ParseUser user) {
        put(KEY_JOB_POSTER, user);
    }


    // Get job subscriber
    public ParseUser getJobSubscriber() {
        return getParseUser(KEY_JOB_SUBSCRIBER);
    }

    public void setJobSubscriber(ParseUser user) {
        put(KEY_JOB_SUBSCRIBER, user);
    }

    // Get job
    public ParseObject getJob() {
        return getParseObject(KEY_JOB);
    }

    public void setJob(ParseObject job) {
        put(KEY_JOB, job);
    }



    public static class Query extends ParseQuery<Matches> {
        public Query() {
            super(Matches.class);
        }

        public Query getTop() {
            orderByDescending("createdAt");
            setLimit(20);
            return  this;
        }

        public Query withJobPoster() {
            include(KEY_JOB_POSTER);
            return this;
        }

        public Query withJobSubscriber() {
            include(KEY_JOB_SUBSCRIBER);
            return this;
        }

        public Query withJob() {
            include(KEY_JOB);
            return this;
        }
    }

}

