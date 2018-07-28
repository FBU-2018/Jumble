package com.example.lkimberly.userstories;

import android.app.Application;

import com.example.lkimberly.userstories.models.Job;

import com.example.lkimberly.userstories.models.Matches;
import com.example.lkimberly.userstories.models.User;

//import com.onesignal.OneSignal;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();

        ParseObject.registerSubclass(Job.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Matches.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu-2018-jumble")
                .clientKey("fbu-2018-key")
                .server("http://fbu-2018-jumble.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}