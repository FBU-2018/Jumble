package com.example.lkimberly.userstories.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkimberly.userstories.JobMatchInfo;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.HomeActivity;
import com.example.lkimberly.userstories.adapters.SwipeCardAdapter;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.Matches;
import com.example.lkimberly.userstories.models.SwipeCard;
import com.example.lkimberly.userstories.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;

public class FeedFragment extends Fragment {

    ArrayList<SwipeCard> al;
    SwipeCardAdapter swipeCardAdapter;

    SwipeFlingAdapterView flingContainer;

    ParseUser currentUser;
    ArrayList<Job> jobs;

    TextView tvNoMoreJobs;
    boolean isFull = true;

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

        jobs = new ArrayList<>();

        currentUser = getCurrentUser();

        final Job.Query postsQuery = new Job.Query();
        postsQuery.getTop().withUser();

        al = new ArrayList<SwipeCard>();

        if (isFull) {
            loadTopPosts();
        }

        swipeCardAdapter = new SwipeCardAdapter(getContext(), getLayoutInflater(), al);

        flingContainer = getActivity().findViewById(R.id.frame);

        tvNoMoreJobs = getActivity().findViewById(R.id.tv_no_more_jobs);

        flingContainer.setAdapter(swipeCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                SwipeCard temp = al.remove(0);
                swipeCardAdapter.notifyDataSetChanged();
                isFull = false;


                if (al.size() == 0) {
                    tvNoMoreJobs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(getContext(), "Left!");
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
                        .setContentTitle("Your job has a new match!")
//                        .setContentText("Check who it is")
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
        final Job.Query postsQuery = new Job.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Job>() {
            @Override
            public void done(List<Job> objects, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < objects.size(); ++i) {
                        Job job = objects.get(i);
                        Log.d("Matched job id ", job.getObjectId());
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
                                    Toast.makeText(getContext(), subscribedObjectId + " subscribed ", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            myRef.addValueEventListener(listener);
                            ValueEventListenerList.add(listener);
                        }

                        try {
                            al.add(new SwipeCard(job.getTitle(), job.getDescription(), job.getImage().getUrl(), job));
                        } catch (NullPointerException e2) {
                            al.add(new SwipeCard("EMPTY", "EMPTY", "EMPTY", null));
                        }
                        swipeCardAdapter.notifyDataSetChanged();
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}