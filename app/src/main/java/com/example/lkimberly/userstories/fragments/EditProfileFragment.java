package com.example.lkimberly.userstories.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.User;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import static com.parse.ParseUser.getCurrentUser;

import com.example.lkimberly.userstories.R;

public class EditProfileFragment extends Fragment {

    Button saveProfileBtn;
    private ViewPager viewPager;

    TextView tv_name;
    TextView tv_institution;
    TextView tv_phoneNumber;
    TextView tv_link;

    EditText et_name;
    EditText et_institution;
    EditText et_phoneNumber;
    EditText et_link;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_edit_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        // Grab a reference to our view pager.
        viewPager = getActivity().findViewById(R.id.pager);
        saveProfileBtn = getActivity().findViewById(R.id.save_profile_btn);

        tv_name = getActivity().findViewById(R.id.tv_profile_name);
        tv_institution = getActivity().findViewById(R.id.tv_profile_institution);
        tv_phoneNumber = getActivity().findViewById(R.id.tv_profile_phone_number);
        tv_link = getActivity().findViewById(R.id.tv_profile_link);

        et_name = getActivity().findViewById(R.id.profile_name);
        et_institution = getActivity().findViewById(R.id.profile_institution);
        et_phoneNumber = getActivity().findViewById(R.id.profile_phone_number);
        et_link = getActivity().findViewById(R.id.profile_link);

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = (User) ParseUser.getCurrentUser();

                String name = et_name.getText().toString();
                if (!name.equals("")) {
                    tv_name.setText(name);
                    user.setName(name);
                    et_name.setText("");
                }

                String institution = et_institution.getText().toString();
                if (!institution.equals("")) {
                    tv_institution.setText(institution);
                    user.setInstitution(institution);
                    et_institution.setText("");
                }

                String phoneNumber = et_phoneNumber.getText().toString();
                if (!phoneNumber.equals("")) {
                    tv_phoneNumber.setText(phoneNumber);
                    user.setPhoneNumber(phoneNumber);
                    et_phoneNumber.setText("");
                }

                String link = et_link.getText().toString();
                if (!link.equals("")) {
                    tv_link.setText(link);
                    user.setLinkedIn(link);
                    et_link.setText("");
                }

                user.saveInBackground();
                
                viewPager.setCurrentItem(3);
            }
        });
    }
}
