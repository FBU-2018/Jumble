package com.example.lkimberly.userstories.models;

import com.parse.ParseUser;

import java.util.List;

public class MatchDataModel {
    // This class is used as a general class to hold either a job objectid which will be used to extract a job for the header,
    // or a list of users, which will be an item, in the match page recycler view system

    public static final int ITEM_TYPEE=0;
    public static final int HEADER_TYPE=1;


    public int type;
    public String jobObjectId;
    public List<ParseUser> listOfUsersForMatch;

    public MatchDataModel(int type, String jobObjectId)
    {
        this.type=type;
        this.jobObjectId=jobObjectId;
    }

    public MatchDataModel(int type, List<ParseUser> listOfUsersForMatch)
    {
        this.type=type;
        this.listOfUsersForMatch=listOfUsersForMatch;
    }

    public List<ParseUser> getAllMatches() {
        return listOfUsersForMatch;
    }

    public String getJobId() {
        return jobObjectId;
    }

    public int getItemTypee(){
        return type;
    }

    @Override
    public String toString() {
        if (type == 0) {
            return listOfUsersForMatch.toString();
        } else {
            return jobObjectId;
        }
    }
}
