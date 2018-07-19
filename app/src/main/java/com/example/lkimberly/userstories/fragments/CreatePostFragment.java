package com.example.lkimberly.userstories.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreatePostFragment extends Fragment {

    ImageView ivPhoto;

    TextView tvTitle;
    TextView tvDescription;
    TextView tvTime;
    TextView tvDate;
    TextView tvLocation;

    EditText etTitle;
    EditText etDescription;
    EditText etTime;
    EditText etDate;
    EditText etLocation;

    ImageButton ibPhoto;
    Button bCreateJob;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_post, container, false);
        ivPhoto = v.findViewById(R.id.ivPhoto);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etTime = view.findViewById(R.id.etTime);
        etDate = view.findViewById(R.id.etDate);
        etLocation = view.findViewById(R.id.etLocation);

        ibPhoto = view.findViewById(R.id.ibPhoto);
        bCreateJob = view.findViewById(R.id.bCreateJob);

        bCreateJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Job newJob = new Job();
                newJob.setTitle(etTitle.getText().toString());
                newJob.setDescription(etDescription.getText().toString());
                newJob.setTime(etTime.getText().toString());
                newJob.setDate(etDate.getText().toString());
                newJob.setLocation(etLocation.getText().toString());

                etTitle.setText("");
                etDescription.setText("");
                etTime.setText("");
                etDate.setText("");
                etLocation.setText("");

                newJob.setUser(ParseUser.getCurrentUser());

                //final ParseFile parseFile = new ParseFile(photoFile);

                Log.d("newJobSave", "1. Success!");

                newJob.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("CreatePostProject", "save job success!");
                            Toast.makeText(getContext(), "Job saved", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("CreatePostProject", "save job failed!");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

//        etTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etTitle.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//
//        etDescription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etDescription.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etDescription, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//
//        etTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etTime.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etTime, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//
//        etDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etDate.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etDate, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//
//        etLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etLocation.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etLocation, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
    }
}