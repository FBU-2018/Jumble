package com.example.lkimberly.userstories;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
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
        View v = inflater.inflate(R.layout.activity_create_post_fragment, container, false);
        ivPhoto = v.findViewById(R.id.ivPhoto);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        
        ibPhoto = view.findViewById(R.id.ibPhoto);
        bCreateJob = view.findViewById(R.id.bCreateJob);



    }
}