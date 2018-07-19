package com.example.lkimberly.userstories.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lkimberly.userstories.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileDetailsActivity extends AppCompatActivity {

    public Activity activity;
    public ImageView ivProfileImage;
    public TextView tvName;
    public TextView tvInstitution;
    public TextView tvPreviousJobs;
    public TextView tvRating;
    public TextView tvPhoneNumber;
    public TextView tvLinksToSocialMedia;
    public ImageView backButton;
    public ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        user = Parcels.unwrap(getIntent().getParcelableExtra("User"));

        ivProfileImage = (ImageView) findViewById(R.id.iv_profilePicDetailsPage);
        tvName = (TextView) findViewById(R.id.tv_potentialMatchNameDetailsPage);
        tvInstitution = (TextView) findViewById(R.id.tv_institutionValue2DetailsPage);
        tvPreviousJobs = (TextView) findViewById(R.id.tv_PreviousJobsValueDetailsPage);
        tvRating = (TextView) findViewById(R.id.tv_ratingValueDetailsPage);
        tvPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumberValueDetailsPage);
        tvLinksToSocialMedia = (TextView) findViewById(R.id.tv_socialMediaLinksValueDetailsPage);



        int round_radius = getBaseContext().getResources().getInteger(R.integer.radius);
        int round_margin = getBaseContext().getResources().getInteger(R.integer.margin);

        final RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(round_radius, round_margin);

        final RequestOptions requestOptions = RequestOptions.bitmapTransform(
                roundedCornersTransformation
        );


        // get the correct place holder and image view for the current orientation
        int placeholderId = R.drawable.ic_instagram_profile;

        try {
            Glide.with(ivProfileImage.getContext())
                    .load(user.fetchIfNeeded().getParseFile("profilePicture").getUrl())
                    .apply(
                            RequestOptions.placeholderOf(placeholderId)
                                    .error(placeholderId)
                                    .fitCenter()
                    )
                    .apply(requestOptions)
                    .into(ivProfileImage);
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


        tvName.setText(user.get("name").toString());
        tvInstitution.setText(user.get("institution") != null ? user.get("institution").toString() : "None");
        tvPreviousJobs.setText(user.get("previousJobs") != null ? user.get("previousJobs").toString() : "None");
        tvRating.setText(user.get("rating") != null ? user.get("rating").toString() : "I'm a beginer!");
        tvPhoneNumber.setText(user.get("phoneNumber") != null ? user.get("phoneNumber").toString() : "None");
        String socialMediaString = "";
        socialMediaString += user.get("facebook") != null ? user.get("facebook").toString() + ", ": "";
        socialMediaString += user.get("twitter") != null ? user.get("twitter").toString() + ", ": "";
        socialMediaString += user.get("linkedin") != null ? user.get("linkedin").toString() + ", ": "";

        int socialMediaLength = socialMediaString.length();
        if (socialMediaLength != 0) {
            socialMediaString = socialMediaString.substring(0, socialMediaLength - 2);
        }
        tvLinksToSocialMedia.setText(socialMediaString);



        backButton = (ImageView) findViewById(R.id.iv_backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });



    }


}
