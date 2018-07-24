package com.example.lkimberly.userstories.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.MainActivity;
import com.example.lkimberly.userstories.models.User;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.File;

public class ProfileFragment extends Fragment {

    Button editProfileBtn;
    Button logOutBtn;
    private ViewPager viewPager;

    ImageView ivProfile;
    TextView tvUsername;
    TextView tvInstitution;
    TextView tvPhoneNumber;
    TextView tvSocialMedia;

    private String imagePath = "";
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public static final int GET_FROM_GALLERY = 3;
    File photoFile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Log.d("fragment", "on Create is called!");
        User user = (User) ParseUser.getCurrentUser();

        // Grab a reference to our view pager.
        viewPager = getActivity().findViewById(R.id.pager);
        editProfileBtn = getActivity().findViewById(R.id.edit_profile_btn);
        logOutBtn = getActivity().findViewById(R.id.log_out_btn);

        ivProfile = view.findViewById(R.id.profile_iv);
        tvUsername = view.findViewById(R.id.tv_profile_name);
        tvInstitution = view.findViewById(R.id.tv_profile_institution);
        tvPhoneNumber = view.findViewById(R.id.tv_profile_phone_number);
        tvSocialMedia = view.findViewById(R.id.tv_profile_link);

        tvUsername.setText(user.getName());
        tvInstitution.setText(user.getInstitution());
        tvPhoneNumber.setText(user.getPhoneNumber());
        tvSocialMedia.setText(user.getLinkedIn());

        try {
            Log.d("testing", "visited!");
            Glide.with(ProfileFragment.this)
                    .load(user.getImage().getUrl())
                    .into(ivProfile);
        } catch (NullPointerException e) {
            Log.d("ProfileFragment", "No Profile Pic");
        }

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(4);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
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
    }
}