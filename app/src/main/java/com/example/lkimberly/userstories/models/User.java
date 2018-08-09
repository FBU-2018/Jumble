package com.example.lkimberly.userstories.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("_User")
public class User extends ParseUser {

    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_LINKEDIN = "linkedin";
    private static final String KEY_FACEBOOK = "facebook";
    private static final String KEY_TWITTER = "twitter";
    private static final String KEY_IMAGE = "profilePicture";
    private static final String KEY_USER = "user";
    private static final String KEY_DATE = "createdAt";
    private static final String KEY_INSTITUTION = "institution";

    public User() {
    }

    // Get name
    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) { put(KEY_NAME, name); }

    public String getPhoneNumber() {
        return getString(KEY_PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber) { put(KEY_PHONE_NUMBER, phoneNumber); }

    // Get profile pic
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    // Get facebook url

    public String getFacebook() { return getString(KEY_FACEBOOK); }

    public void setFacebook(String facebook) { put(KEY_FACEBOOK, facebook); }

    // Get linkedin url
    public String getLinkedIn() {
        return getString(KEY_LINKEDIN);
    }

    public void setLinkedIn(String linkedin) {
        put(KEY_LINKEDIN, linkedin);
    }


    // Get twitter url
    public String getTwitter() {
        return getString(KEY_TWITTER);
    }

    public void setTwitter(String name) {
        put(KEY_TWITTER, name);
    }


    // Get institution
    public String getInstitution() {
        return getString(KEY_INSTITUTION);
    }

    public void setInstitution(String institution) {
        put(KEY_INSTITUTION, institution);
    }


    //Get created at
    public String createdAt() {
        return getString(KEY_DATE);
    }



//    public ParseFile getMedia() {
//        return getParseFile(KEY_IMAGE);
//    }
//
//    public void setMedia(ParseFile parseFile) {
//        put(KEY_IMAGE, parseFile);
//    }

    public static class Query extends ParseQuery<User> {
        public Query() {
            super(User.class);
        }

        public Query getTop() {
            orderByDescending("createdAt");
            setLimit(20);
            return this;
        }

//        public Query withUser() {
//            include("user");
//            return this;
//        }
    }

}
