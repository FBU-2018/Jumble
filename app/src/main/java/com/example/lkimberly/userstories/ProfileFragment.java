package com.example.lkimberly.userstories;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    Button editProfileBtn;
    private ViewPager viewPager;

    ParseUser currentUser;

    ImageView ivProfile;
    TextView tvUsername;
    TextView tvInstution;
    TextView tvPhoneNumber;
    TextView tvSocialMedia;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Grab a reference to our view pager.
        viewPager = getActivity().findViewById(R.id.pager);
        editProfileBtn = getActivity().findViewById(R.id.edit_profile_btn);

        currentUser = ParseUser.getCurrentUser();

        ivProfile = view.findViewById(R.id.profile_iv);
        tvUsername = view.findViewById(R.id.profile_name);
        tvInstution = view.findViewById(R.id.profile_institution);
        tvPhoneNumber = view.findViewById(R.id.profile_phone_number);
        tvSocialMedia = view.findViewById(R.id.profile_social_media);

        tvUsername.setText(currentUser.getUsername());
        tvInstution.setText(currentUser.get("institution").toString());
        tvPhoneNumber.setText(currentUser.get("phoneNumber").toString());
        tvSocialMedia.setText(currentUser.get("facebook").toString());

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3);
            }
        });
    }
}
