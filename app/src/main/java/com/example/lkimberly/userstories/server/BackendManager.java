package com.example.lkimberly.userstories.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class BackendManager {

    private static final String SERVER_HOST_NAME = "jumble-backend.herokuapp.com";

    public static URL getTaggingEndpoint(String job_description) throws MalformedURLException {
        return new URL("http://" + SERVER_HOST_NAME + "/tagging/" + job_description);
    }

//    public static URL saveTaggingEndpoint(int PLU, String name) throws MalformedURLException {
//        return new URL("http://" + SERVER_HOST_NAME + "/plu/" + PLU + "/" + name);
//    }

    public static URL getRecommendationsEndpoint(List<List<Integer>> new_user_data) throws MalformedURLException {
        return new URL("http://" + SERVER_HOST_NAME + "/recommending/" + new_user_data);
    }

}