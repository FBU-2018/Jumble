package com.example.lkimberly.userstories;

import android.app.Application;

import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.Matches;
import com.example.lkimberly.userstories.models.Ratings;
import com.example.lkimberly.userstories.models.User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

//import com.onesignal.OneSignal;

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
        ParseObject.registerSubclass(Ratings.class);

        // FCM token will be automatically registered by ParseFirebaseJobService
        // Look for ParseFCM log messages to confirm
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu-2018")
                .clientKey("fbu-2018-key")
                .server("http://fbu-2018.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "275566501773");
        installation.put("channel", "default");
        installation.saveInBackground();
    }
}