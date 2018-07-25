package com.example.lkimberly.userstories.models;

import com.parse.ParseUser;

import java.util.List;

public class MatchDataModel {
    // This class is used as a general class to hold either a job objectid which will be used to extract a job for the header,
    // or a list of users, which will be an item, in the match page recycler view system

    public static final int ITEM_TYPEE=0;
    public static final int HEADER_TYPE=1;
    public static final int NO_MATCH_TYPE=2;
    public static final int NO_MATCH_HEADER=3;


    public int type;
    public String jobObjectId;
    public List<ParseUser> listOfUsersForMatch;
    public Job job;


    // Constructor for headers in match page (job headers)
    public MatchDataModel(int type, String jobObjectId)
    {
        this.type=type;
        this.jobObjectId=jobObjectId;
    }

    // Constructor for list of matches in match page
    public MatchDataModel(int type, List<ParseUser> listOfUsersForMatch, String jobObjectId)
    {
        this.type=type;
        this.listOfUsersForMatch=listOfUsersForMatch;
        this.jobObjectId=jobObjectId;
    }

    // Constructor for job with no matches in match page
    public MatchDataModel(int type, Job job){
        this.type = type;
        this.job = job;
    }

    // Constructor for header stating the following jobs have no matches
    public  MatchDataModel(int type){this.type = type;}

    public List<ParseUser> getAllMatches() {
        return listOfUsersForMatch;
    }

    public String getJobId() {
        return jobObjectId;
    }

    public Job getJob() {
        return job;
    }

    public int getItemTypee(){
        return type;
    }

    @Override
    public String toString() {
        if (type == 0) {
            return jobObjectId + ", " + listOfUsersForMatch.toString();
        } else if (type == 1){
            return jobObjectId;
        } else if (type == 2) {
            return "Job object: " + job.getObjectId();
        } else {
            return Integer.toString(type);
        }
    }
}
