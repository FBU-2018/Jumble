package com.example.lkimberly.userstories;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu-2018-jumble")
                .clientKey("fbu-2018-key")
                .server("http://fbu-2018-jumble.herokuapp.com/parse")
                .build();
        
        Parse.initialize(configuration);
    }
}