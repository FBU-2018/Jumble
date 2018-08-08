package com.example.lkimberly.userstories.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.Matches;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileDetailsActivity extends AppCompatActivity {

    public Activity activity;
    public ImageView ivProfileImage;
    public TextView tvName;
    public TextView tvInstitution;
    public TextView tvPreviousJobs;
    public TextView tvRating;
    public TextView tvPhoneNumber;
    public ImageView backButton;
    public static Button matchButton;
    public ParseUser user;
    public Job job;

    public ImageButton facebookIcon;
    public ImageButton twitterIcon;
    public ImageButton linkedinIcon;

    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        user = Parcels.unwrap(getIntent().getParcelableExtra("User"));
        job = Parcels.unwrap(getIntent().getParcelableExtra("job"));
        ivProfileImage = (ImageView) findViewById(R.id.iv_profilePicDetailsPage);
        tvName = (TextView) findViewById(R.id.tv_potentialMatchNameDetailsPage);
        tvInstitution = (TextView) findViewById(R.id.tv_institutionValue2DetailsPage);
//        tvPreviousJobs = (TextView) findViewById(R.id.tv_PreviousJobsValueDetailsPage);
//        tvRating = (TextView) findViewById(R.id.tv_ratingValueDetailsPage);
        tvPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumberValueDetailsPage);
        matchButton = (Button) findViewById(R.id.bv_matchButton);
        facebookIcon = findViewById(R.id.facebook_ib3);
        twitterIcon = findViewById(R.id.twitter_ib3);
        linkedinIcon = findViewById(R.id.linkedIn_ib3);
        ratingBar = findViewById(R.id.rb_profile_ratings_bar3);



        int round_radius = getBaseContext().getResources().getInteger(R.integer.radius);
        int round_margin = getBaseContext().getResources().getInteger(R.integer.margin);

        final RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(round_radius, round_margin);

        final RequestOptions requestOptions = RequestOptions.bitmapTransform(
                roundedCornersTransformation
        );


        // get the correct place holder and image view for the current orientation
        int placeholderId = R.drawable.ic_instagram_profile;
        try {
            try
            {
                Glide.with(ivProfileImage.getContext())
                        .load(user.fetchIfNeeded().getParseFile("profilePicture").getUrl())
                        .apply(
                                RequestOptions.placeholderOf(placeholderId)
                                        .error(placeholderId)
                                        .fitCenter()
                        )
                        .apply(requestOptions)
                        .into(ivProfileImage);
            } catch (NullPointerException noProfilePic){
                Glide.with(ivProfileImage.getContext())
                        .load(placeholderId)
                        .apply(
                                RequestOptions.placeholderOf(placeholderId)
                                        .error(placeholderId)
                                        .fitCenter()
                        )
                        .apply(requestOptions)
                        .into(ivProfileImage);
            }

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


        tvName.setText(user.get("name").toString());
        tvInstitution.setText(user.get("institution") != null ? user.get("institution").toString() : "None");
//        tvPreviousJobs.setText(user.get("previousJobs") != null ? user.get("previousJobs").toString() : "None");
//        tvRating.setText(user.get("rating") != null ? user.get("rating").toString() : "I'm a beginer!");
        tvPhoneNumber.setText(user.get("phoneNumber") != null ? user.get("phoneNumber").toString() : "None");

        try {
            ratingBar.setRating((float) (parseDouble(user.get("rating").toString()) * 5));
        } catch (NullPointerException e) {
            ratingBar.setRating((float) (0));
        }

        backButton = (ImageView) findViewById(R.id.iv_backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        if (job.get("match") != null) {
//            ParseUser userWhoMatched = (ParseUser) job.get("userWhoMatched");
//            if (userWhoMatched != null) {
//                if (userWhoMatched.getObjectId().equals(user.getObjectId())) {
//                    matchButton.setText("You have a match for this job");
//                }
//            }
            Log.d("Whats the job?", job.get("title").toString());
            matchButton.setText("You have a match for this job");

        }


        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (matchButton.getText().toString().equals("Hire!")) {
                    final Matches newMatch = new Matches();

                    // default ACLs for User object
                    ParseACL parseACL = new ParseACL(ParseUser.getCurrentUser());
                    parseACL.setPublicReadAccess(true);

                    ParseUser.getCurrentUser().setACL(parseACL);

                    job.put("userWhoMatched", user);

                    // Get match (object containing person who swiped on this job)
                    Matches.Query getMatchQuery = new Matches.Query();
                    getMatchQuery.getTop().whereEqualTo("job", job).findInBackground(new FindCallback<Matches>() {
                        @Override
                        public void done(List<Matches> objects, ParseException e) {
                            Log.d("Match existing", objects.get(0).toString());
                            job.put("match", objects.get(0));

                            List<ParseObject> objList = new ArrayList<ParseObject>();
                            // Add new ParseObject instances to the list
                            objList.add(job);

                            // Save all created ParseObject instances at once
                            ParseObject.saveAllInBackground(objList, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();
                                        matchButton.setText("You have matched for this job");
                                        // TODO: Transition to message the person to hire

                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setData(Uri.parse("smsto:" + user.get("phoneNumber")));  // This ensures only SMS apps respond
                                        intent.putExtra("sms_body", "Hi! You matched with my job on Jumble and I'd like to hire you.");
                                        if (intent.resolveActivity(getPackageManager()) != null) {

                                            // End this current intent first
                                            Intent i = new Intent(view.getContext(), JobDetailsActivity.class);
                                            i.putExtra("User", Parcels.wrap(user));
                                            i.putExtra("job", Parcels.wrap(job));
                                            i.putExtra("match", Parcels.wrap(newMatch));
                                            i.putExtra("refresh", true);
                                            setResult(RESULT_OK, i); // set result code and bundle data for response
                                            finish();

                                            startActivity(intent);
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });

        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.get("facebook") != null) {
                    browseSocialMedia(user.get("facebook").toString());
                } else {
                    Toast.makeText(getBaseContext(), "I'm sorry, this user doesn't have a Facebook link.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.get("twitter") != null) {
                    browseSocialMedia(user.get("twitter").toString());
                } else {
                    Toast.makeText(getBaseContext(), "I'm sorry, this user doesn't have a Twitter link.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        linkedinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.get("linkedin") != null) {
                    try {
                        browseSocialMedia(user.get("linkedin").toString());
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getBaseContext(), "I'm sorry, this user seems to have an error in this social media link", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "I'm sorry, this user doesn't have a LinkedIn link.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set heading bar
//        getSupportActionBar().setTitle("Potential Match Profile Page");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }


    public void browseSocialMedia(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        try {
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(), "I'm sorry, this user seems to have an error in this social media link", Toast.LENGTH_SHORT).show();
        }
    }

    double parseDouble(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

}
