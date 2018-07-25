package com.example.lkimberly.userstories.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.Matches;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class JobDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Activity to provide information for user on the specific job they

    public MapView mapView;
    public ImageView ivJobImage;
    public TextView tvJobName;
    public TextView tvJobDescription;
    public CalendarView calendarView;
    public TextView tvTime;
    public TextView matchName;
    public RatingBar ratingBar;
    private GoogleMap gmap;
    public ParseUser userWhoMatchedWithMe;

    public TextView ratingTitle;
    public TextView matchTitle;
    public Job job;
    public Matches match;

    public Button endInteractionButton;

    public ImageButton backButton;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mv_jobDetailsMap);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        ivJobImage = (ImageView) findViewById(R.id.iv_jobDetailsJobPicture);
        tvJobName = (TextView) findViewById(R.id.tv_jobDetailsJobTitle);
        tvJobDescription = (TextView) findViewById(R.id.tv_jobDetailsJobDescription);
        calendarView = (CalendarView) findViewById(R.id.cv_jobDetailsCalendar);
        tvTime = (TextView) findViewById(R.id.tv_jobDetailsTime);
        matchName = (TextView) findViewById(R.id.tv_jobDetailsMatchName);
        ratingBar = (RatingBar) findViewById(R.id.rb_jobDetailsRatingsBar);
        endInteractionButton = (Button) findViewById(R.id.btn_jobDetailsEndJob);
        matchTitle = (TextView) findViewById(R.id.tv_jobDetailsMatchTitle);
        ratingTitle = (TextView) findViewById(R.id.tv_jobDetailsRatingTitle);
        backButton = (ImageButton) findViewById(R.id.ib_jobDetailsBackButton);

        job = Parcels.unwrap(getIntent().getParcelableExtra("job"));
        match = (Matches) job.get("match");


        try {
            Glide.with(JobDetailsActivity.this).load(((ParseFile) job.get("image")).getUrl()).into(ivJobImage);
        } catch(NullPointerException e) {
            Log.d("ProfileFragment", "No Profile Pic");
        }
        tvJobName.setText(job.get("title").toString());
        tvJobDescription.setText(job.get("description").toString());
        // calendar
        tvTime.setText(job.get("time").toString());

        userWhoMatchedWithMe = job.getParseUser("userWhoMatched");
        if (userWhoMatchedWithMe == null) {
            ratingBar.setVisibility(View.GONE);
            matchName.setVisibility(View.GONE);
            matchTitle.setVisibility(View.GONE);
            ratingTitle.setVisibility(View.GONE);
        } else {
            try {
                matchName.setText(userWhoMatchedWithMe.fetchIfNeeded().get("name").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ratingBar.setRating((float) (parseDouble(userWhoMatchedWithMe.get("rating").toString()) * 5));
        }


        endInteractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userWhoMatchedWithMe == null) {
                    handlEndJob();
                } else {
                    endJobOrMatchDialogue();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMatchesFeed();
            }
        });



    }

    public void returnToMatchesFeed() {
        Intent i = new Intent();
        // Pass relevant data back as a result
        // Activity finished ok, return the data
        setResult(RESULT_OK, i); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void returnToMatchesFeedRefresh() {
        Intent i = new Intent();
        // Pass relevant data back as a result
        // Activity finished ok, return the data
        i.putExtra("refresh", true);
        setResult(RESULT_OK, i); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void endJobOrMatchDialogue() {
        final AlertDialog.Builder endJobAlertBuilder = new AlertDialog.Builder(this);
        endJobAlertBuilder.setMessage("Would you lke to end the job or just the match?")
                .setCancelable(false)
                .setPositiveButton("Job (including the match)", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        endJobAction();
                        // ^^ in this function, on call back, endMatchAction() will be called
                    }
                })
                .setNegativeButton("Match", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Matches matchToDelete = (Matches) job.get("match");
                        job.remove("match");
                        job.remove("userWhoMatched");
                        job.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                endMatchAction(matchToDelete);
                            }
                        });


                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        android.app.AlertDialog alert = endJobAlertBuilder.create();
        alert.show();
    }

    public void handlEndJob() {
        final AlertDialog.Builder endJobAlertBuilder = new AlertDialog.Builder(this);

        endJobAlertBuilder.setMessage("Are you sure you want to end this job?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things

                        endJobAction();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        android.app.AlertDialog alert = endJobAlertBuilder.create();
        alert.show();
    }


    public void endJobAction() {
        final Matches matchToDelete = (Matches) job.get("match");
        job.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                            if (matchToDelete != null) {
                                endMatchAction(matchToDelete);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                //TODO: Go back to match feed
            }
        });
        //TODO: Refresh job match feed!!!
    }

    public void endMatchAction(Matches matchToDelete){
        matchToDelete.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
//                TODO:Go back to main feed
                returnToMatchesFeedRefresh();

            }
        });
    }


    double parseDouble(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }


}
