package com.example.lkimberly.userstories.fragments;

import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.HomeActivity;
import com.example.lkimberly.userstories.adapters.SwipeCardAdapter;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.Matches;
import com.example.lkimberly.userstories.models.SwipeCard;
import com.example.lkimberly.userstories.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.parse.ParseUser.getCurrentUser;

public class FeedFragment extends Fragment {

    ArrayList<SwipeCard> al;
    SwipeCardAdapter swipeCardAdapter;

    SwipeFlingAdapterView flingContainer;

    ParseUser currentUser;
    ArrayList<Job> jobs;

    boolean load = true;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    SphericalUtil mapsUtil;
    Location userCurentLocation;

    private FusedLocationProviderClient mFusedLocationProvidentClient;
    private static final String TAG = "FeedFragment";

    TextView tvNoMoreJobs;

    int swipeCount;

    List<Integer> categorySwipeCount;
    Map<String, Integer> categoryToIdxMap = new HashMap<>();

    public static List<ValueEventListener> ValueEventListenerList = new ArrayList<>();

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_feed, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        swipeCount = 0;
        initializeMap();

        categorySwipeCount = ((User) ParseUser.getCurrentUser()).getCategorySwipeCount();

        if (categorySwipeCount == null) {
            Integer arr[] = new Integer[37];
            for (int i = 0; i < arr.length; i++)
                arr[i] = 0;
            categorySwipeCount = Arrays.asList(arr);
        }

        getDeviceLocation();

        jobs = new ArrayList<>();

        currentUser = getCurrentUser();

        final Job.Query postsQuery = new Job.Query();

        //if (load) {
            postsQuery.getTop().withUser();
        //}

        al = new ArrayList<SwipeCard>();

//        if (load) {
            loadTopPosts();
            Log.d("Here", "here");
//        }

        load = false;

        swipeCardAdapter = new SwipeCardAdapter(getContext(), getLayoutInflater(), al);

        flingContainer = getActivity().findViewById(R.id.frame);

        tvNoMoreJobs = getActivity().findViewById(R.id.tv_no_more_jobs);

        if (al.size() == 0) {
            tvNoMoreJobs.setVisibility(View.VISIBLE);
        }

        flingContainer.setAdapter(swipeCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                //SwipeCard temp = al.remove(0);
                al.remove(0);
                swipeCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(getContext(), "Left!");
                swipeCount++;

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRightCardExit(Object dataObject) {
                SwipeCard currentCard = (SwipeCard) dataObject;
                createMatch(currentCard);

                ParseUser jobPoster = currentCard.getJob().getUser();

                // Notifications
                String jobObjectId = currentCard.getJob().getObjectId();
                String jobTitle = currentCard.getJob().getTitle();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("JobMatchInfo")
                        .child(jobObjectId)
                        .child("Details");

                ParseUser currentUser = getCurrentUser();
                String name = currentUser.getUsername();

                String message = name + ":" + jobObjectId + ":" + jobTitle;

                myRef.setValue(message);

                Log.d("Swipe Right", "object id = " + jobObjectId);

                //DatabaseReference pushRef = myRef.child("Your job has been matched!").push();
                //String uid = pushRef.getKey();

                //if (subscribedObjectId.equals(getCurrentUser().getObjectId())) {

                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), "CHANNEL_ID");

                notificationBuilder.setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
//                        .setContentTitle("Your job has a new match!")
                        .setContentText("Your job has a new match!")
                        .setSmallIcon(R.drawable.icon)
                        .setChannelId("CHANNEL_ID")
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent);

                // Create channel notification group
                String groupId = jobObjectId;
                CharSequence groupName = jobTitle;
                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));

                notificationManager.notify(1, notificationBuilder.build());
                //}

                swipeCount++;
                Job currentCardJob = currentCard.getJob();
                String category = currentCardJob.getCategory();

                // update swipe count for given card category for current user
                if (category != null) {
                    Integer categoryIdx = categoryToIdxMap.get(category);
                    Integer currentSwipeCountForCategory = categorySwipeCount.get(categoryIdx);
                    categorySwipeCount.set(categoryIdx, currentSwipeCountForCategory + 1);
                } else {
                    Integer currentSwipeCountForCategory = categorySwipeCount.get(36);
                    categorySwipeCount.set(36, currentSwipeCountForCategory + 1);
                }

                // update info every 5 swipes
                if (swipeCount%5 == 0){
                    User user = (User) ParseUser.getCurrentUser();
                    user.setCategorySwipeCount(categorySwipeCount);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // run alg to learn preferences and update
                            } else {
                                e.printStackTrace();
                                makeToast(getContext(), "Something went wrong with updating user preferences!");
                            }
                        }
                    });
                }

                makeToast(getContext(), "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                 Ask for more data here
                Log.d("onAdapterAboutToEmpty", "No more!");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getRootView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(getContext(), "Details");
                swipeCardAdapter.goToDetailsPage(((SwipeCard) dataObject).getJob());
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    private void createMatch(SwipeCard currentCard) {
        final Matches newMatch = new Matches();
        newMatch.setJobPoster(currentCard.getJob().getUser());
        newMatch.setJobSubscriber(currentUser);
        newMatch.setJob(currentCard.getJob());

        Log.d("newMatchSave", "1. Success!");

        newMatch.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("createMatch", "save match success!");
//                    Toast.makeText(getContext(), "Match saved", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("createMatch", "save match failed!");
                    e.printStackTrace();
                }
            }
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    private void loadTopPosts() {

        List<String> jobPreferences = ((User) ParseUser.getCurrentUser()).getJobPreferences();
        boolean handlePreferences = false;

        // only handle preferences if user has them
        if (jobPreferences != null){
            if (jobPreferences .size() > 0) {
                handlePreferences = true;
            }
        }

        // initialize query

        Log.d("Here2", "here");

        final Job.Query postsQuery = new Job.Query();
        postsQuery.getTop().withUser();

        // handle preferences if needed
        if (handlePreferences){
            postsQuery.withPreference(jobPreferences.get(0));
        }


        postsQuery.findInBackground(new FindCallback<Job>() {
            @Override
            public void done(List<Job> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() == 0) {
                        tvNoMoreJobs.setVisibility(View.VISIBLE);
                    } else {
                        tvNoMoreJobs.setVisibility(View.INVISIBLE);
                    }

                    for (int i = 0; i < objects.size(); ++i) {
                        Job job = objects.get(i);
                        Log.d("Matched job id ", job.getObjectId());
                        /*
                        if (job.getUser().getObjectId().equals(getCurrentUser().getObjectId())) {
                            String jobObjectId = job.getObjectId();
                            String jobTitle = job.getTitle();

                            Log.d("Found id", jobObjectId);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("JobMatchInfo")
                                    .child(jobObjectId).child("Details");
                            myRef.setValue("");

                            ValueEventListener listener;
                            listener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String key = dataSnapshot.getKey();
                                    Object value = dataSnapshot.getValue();

                                    if (value == null) {
                                        return;
                                    }

                                    String subscribedObjectId = value.toString();

                                    Log.d("firebase listener", key + " and " + subscribedObjectId);
                                    //Toast.makeText(getContext(), subscribedObjectId + " subscribed ", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            myRef.addValueEventListener(listener);
                            ValueEventListenerList.add(listener);
                        }
                        */

                        if (!job.getUser().getObjectId().equals(getCurrentUser().getObjectId())) {
                            try {
                                al.add(new SwipeCard(job.getTitle(), job.getDescription(), job.getImage().getUrl(), job));
                                rankList(al);
                                Collections.reverse(al);

//                            al.add(new SwipeCard(job.getTitle(), job.getDescription(), job.getImage().getUrl(), job));

                            } catch (NullPointerException e2) {
                                rankList(al);
                                Collections.reverse(al);
                                al.add(new SwipeCard("EMPTY", "EMPTY", "EMPTY", null));
                            }
                            swipeCardAdapter.notifyDataSetChanged();
                        }
                    }
                    int count = 1;
                    for (SwipeCard card: al) {
                        Log.d("Card"+count, String.valueOf(getFOneScore(card.getJob())));
                        count++;
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void rankList(ArrayList<SwipeCard> jobList) {
        Collections.sort(jobList, new Comparator<SwipeCard>() {
            @Override
            public int compare(SwipeCard sc1, SwipeCard sc2) {
                return Double.compare(getFOneScore(sc1.getJob()), getFOneScore(sc2.getJob()));
            }
        });
    }

    private double getFOneScore(Job jobToComputeScore) {
        double distanceInKm;
        double userRatingForScore;

        try {
            try {
                distanceInKm = mapsUtil.computeDistanceBetween(new LatLng(Double.valueOf((String) jobToComputeScore.get("latitude")), Double.valueOf((String) jobToComputeScore.get("longitude"))),
                        new LatLng(userCurentLocation.getLatitude(), userCurentLocation.getLongitude()))/1000;
            } catch (NullPointerException nullEx) {
                distanceInKm = 1;
//                nullEx.printStackTrace();
            }

        } catch (SecurityException secEx) {
            distanceInKm = 1;   // ignore distance in calcualtion
            secEx.printStackTrace();
        }

        double inverseDist = 1/distanceInKm;
        try {
            String retrievedRating = (String) ((ParseUser) jobToComputeScore.get("user")).fetchIfNeeded().get("rating");
            if (retrievedRating == null) {
                userRatingForScore = 0;
            } else {
                double ratingFraction = parseDouble(retrievedRating);
                userRatingForScore = ratingFraction*5;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Log.d((String) jobToComputeScore.get("title"), distanceInKm + ", " + inverseDist + ", " + userRatingForScore);
        double score = (inverseDist*userRatingForScore)/(inverseDist+userRatingForScore);
        return score;

    }

    double parseDouble(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProvidentClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
//            if (mLocationPermissionsGranted) {
            Task location = mFusedLocationProvidentClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            userCurentLocation = (Location) task.getResult();

                            double latitude = userCurentLocation.getLatitude();
                            double longitude = userCurentLocation.getLongitude();

                            StringBuilder result = new StringBuilder();

                            loadTopPosts();
                            try {
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    Log.d(TAG, "onComplete: found location! " + task.getResult());
//                                    result.append(address.getLocality());
//                                    result.append(address.getCountryName());
                                }
                            } catch (IOException e) {
                                Log.e("tag", e.getMessage());
                            }

//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }


    private void initializeMap(){

        categoryToIdxMap.put("accountancy qualified jobs", 0);
        categoryToIdxMap.put("accountancy jobs", 1);
        categoryToIdxMap.put("banking jobs", 2);
        categoryToIdxMap.put("finance jobs", 3);
        categoryToIdxMap.put("purchasing jobs", 4);
        categoryToIdxMap.put("sales jobs", 5);
        categoryToIdxMap.put("marketing jobs",  6);
        categoryToIdxMap.put("retail jobs", 7);
        categoryToIdxMap.put("fmcg jobs", 8);
        categoryToIdxMap.put("catering jobs", 9);
        categoryToIdxMap.put("social care jobs", 10);
        categoryToIdxMap.put("charity jobs", 11);
        categoryToIdxMap.put("leisure tourism jobs", 12);
        categoryToIdxMap.put("education jobs", 13);
        categoryToIdxMap.put("admin secretarial pa jobs", 14);
        categoryToIdxMap.put("graduate training internships jobs", 15);
        categoryToIdxMap.put("training jobs", 16);
        categoryToIdxMap.put("media digital creative jobs", 17);
        categoryToIdxMap.put("apprenticeships jobs", 18);
        categoryToIdxMap.put("security safety jobs", 19);
        categoryToIdxMap.put("construction property jobs", 20);
        categoryToIdxMap.put("motoring automotive jobs", 21);
        categoryToIdxMap.put("factory jobs", 22);
        categoryToIdxMap.put("science jobs", 23);
        categoryToIdxMap.put("energy job", 24);
        categoryToIdxMap.put("health jobs", 25);
        categoryToIdxMap.put("engineering jobs", 26);
        categoryToIdxMap.put("it jobs", 27);
        categoryToIdxMap.put("logistics jobs", 28);
        categoryToIdxMap.put("strategy consultancy jobs", 29);
        categoryToIdxMap.put("law jobs", 30);
        categoryToIdxMap.put("hr jobs", 31);
        categoryToIdxMap.put("general insurance jobs", 32);
        categoryToIdxMap.put("estate agent jobs", 33);
        categoryToIdxMap.put("recruitment consultancy jobs", 34);
        categoryToIdxMap.put("customer service jobs", 35);
        categoryToIdxMap.put("other jobs", 36);

    }

}