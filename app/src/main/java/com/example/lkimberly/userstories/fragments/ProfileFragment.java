package com.example.lkimberly.userstories.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.MainActivity;
import com.example.lkimberly.userstories.models.Ratings;
import com.example.lkimberly.userstories.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    Button editProfileBtn;
    Button logOutBtn;
    private ViewPager viewPager;

    ImageView ivProfile;
    TextView tvUsername;
    TextView tvInstitution;
    TextView tvPhoneNumber;

    ImageButton facebook_ib;
    ImageButton linkedIn_ib;
    ImageButton twitter_ib;

    User user;

    private String imagePath = "";
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public static final int GET_FROM_GALLERY = 3;
    File photoFile;

    RatingBar ratingBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        user = (User) ParseUser.getCurrentUser();

        // Grab a reference to our view pager.
        viewPager = getActivity().findViewById(R.id.pager);
        editProfileBtn = getActivity().findViewById(R.id.edit_profile_btn);
        logOutBtn = getActivity().findViewById(R.id.log_out_btn);

        ivProfile = view.findViewById(R.id.profile_iv);
        tvUsername = view.findViewById(R.id.tv_profile_name);
        tvInstitution = view.findViewById(R.id.tv_profile_institution);
        tvPhoneNumber = view.findViewById(R.id.tv_profile_phone_number);
        ratingBar = view.findViewById(R.id.rb_profile_ratings_bar);

        if (user.getName() == null) {
            // a new user, has not set name yet
            // set username as name
            String username = user.getUsername();
            tvUsername.setText(username);
            user.setName(username);
        } else {
            String name = user.getName();
            tvUsername.setText(name);
            //user.setName(name);
        }

        if (user.getInstitution() == null) {
            // a new user, has not set institution yet
            String defaultInstitution = "Unemployed";
            tvInstitution.setText(defaultInstitution);
            user.setInstitution(defaultInstitution);
        } else {
            String institution = user.getInstitution();
            tvInstitution.setText(institution);
            //user.setInstitution(institution);
        }

        if (user.getPhoneNumber() == null) {
            // a new user, has not set phone number yet
            String defaultNumber = "111-111-1111";
            tvPhoneNumber.setText(defaultNumber);
            user.setPhoneNumber(defaultNumber);
        } else {
            String phoneNumber = user.getPhoneNumber();
            tvPhoneNumber.setText(phoneNumber);
            //user.setPhoneNumber(phoneNumber);
        }

        user.saveInBackground();

        try {
            Glide.with(ProfileFragment.this)
                    .load(user.getImage().getUrl())
                    .into(ivProfile);
        } catch (NullPointerException e) {
            Log.d("ProfileFragment", "No Profile Pic");
            e.printStackTrace();

            Glide.with(ProfileFragment.this)
                    .load(R.drawable.default_avatar)
                    .into(ivProfile);

            Bitmap bitMap = BitmapFactory.decodeResource(getResources(),R.drawable.default_avatar);

            File file1 = Environment.getExternalStorageDirectory();

            String fileName ="default_avatar.png";

            File file2 = new File(file1,fileName);
            try {
                FileOutputStream outStream;

                outStream = new FileOutputStream(file2);

                bitMap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                outStream.flush();

                outStream.close();

            } catch (FileNotFoundException f) {
                f.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }

            String sdPath = file1.getAbsolutePath() + "/" + fileName;

            Log.d("imagePath", "imagePath is = " + sdPath);

            ParseFile parseFile = new ParseFile(new File(sdPath));
            user.put("profilePicture", parseFile);
            user.saveInBackground();
        }

        try {
            Ratings myRating = null;
            try {
                myRating = ((Ratings)  user.get("myRating")).fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double rating = parseDouble(myRating.getRating().toString()) * 5;
            ratingBar.setRating((float) (rating));
        } catch (NullPointerException noRatingError) {}

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        facebook_ib = view.findViewById(R.id.facebook_ib);
        linkedIn_ib = view.findViewById(R.id.linkedIn_ib);
        twitter_ib = view.findViewById(R.id.twitter_ib);

        facebook_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getFacebook() != null) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getFacebook()));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "Please provide a complete link (including 'https')", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getContext(), "Please provide a link to your Facebook!", Toast.LENGTH_LONG).show();
                }
            }
        });

        linkedIn_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkedInLink = user.getLinkedIn();
                if (linkedInLink != null) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedInLink));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "Please provide a complete link (including 'https')" + linkedInLink, Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getContext(), "Please provide a link to your LinkedIn!", Toast.LENGTH_LONG).show();
                }
            }
        });

        twitter_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitterLink = user.getTwitter();
                if (twitterLink != null) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "Please provide a complete link (including 'https')", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please provide a link to your Twitter!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void logOut() {
        Log.d("Logout", "Logged out");
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    final Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Log.e("Log Out Error!", "User wasn't logged out!");
                }
            }
        });

//        // Log out of Firebase account
//        FirebaseAuth.getInstance().signOut();

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
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
