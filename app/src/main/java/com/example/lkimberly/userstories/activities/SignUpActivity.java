package com.example.lkimberly.userstories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Ratings;
import com.example.lkimberly.userstories.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUp Activity";

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button createBtn;

    ImageView iv_username_correct;
    ImageView iv_password_correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.signup_username_et);
        passwordInput = findViewById(R.id.signup_password_et);
        emailInput = findViewById(R.id.sign_up_email_et);
        iv_username_correct = findViewById(R.id.iv_username_correct);
        iv_password_correct = findViewById(R.id.iv_password_correct);

        createBtn = findViewById(R.id.signup_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isEmptyUsernameText = false;
                boolean isEmptyPasswordText = false;

                String username = "";
                String usernameInputStr = usernameInput.getText().toString();

                if (!usernameInputStr.equals("")) {
                    username = usernameInputStr;
                    iv_username_correct.setVisibility(View.VISIBLE);
                } else {
                    isEmptyUsernameText = true;
                    iv_username_correct.setVisibility(View.INVISIBLE);
                }

                String password = "";
                String passwordInputStr = passwordInput.getText().toString();

                if (!passwordInputStr.equals("")) {
                    password = passwordInputStr;
                    iv_password_correct.setVisibility(View.VISIBLE);
                } else {
                    isEmptyPasswordText = true;
                    iv_password_correct.setVisibility(View.INVISIBLE);
                }

                String email = "";
                String emailInputStr = emailInput.getText().toString();

                if (!emailInputStr.equals("")) {
                    email = emailInputStr;
//                    iv_password_correct.setVisibility(View.VISIBLE);
                } else {
//                    isEmptyPasswordText = true;
//                    iv_password_correct.setVisibility(View.INVISIBLE);
                }

                if (isEmptyUsernameText || isEmptyPasswordText) {
                    String requirement = "Please enter a";
                    if (isEmptyUsernameText) {
                        requirement += " username";
                    }

                    if (isEmptyPasswordText) {
                        if (isEmptyUsernameText) {
                            requirement += " and password";
                        } else {
                            requirement += " password";
                        }
                    }

                    requirement += "!";
                    Toast.makeText(getApplicationContext(), requirement, Toast.LENGTH_LONG).show();
                } else {
                    signUp(username, password, email);
                }
            }
        });
    }

    private void signUp(String username, String password, String email) {
        // Create the ParseUser
        final User user = new User();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Set custom properties
        // user.put("phone", "650-253-0000");
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    user.setRating("0/5");
                    user.setTotalScore("0");
                    user.setTotalTimesRated("0");

                    final Ratings myRating = new Ratings();
                    myRating.setRating("0/5");
                    myRating.setTimesRated("0");
                    myRating.setTotalScore("0");

                    // save rating first, then update user with rating and other info and save that
                    myRating.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                user.put("myRating", myRating);

                                Integer arr[] = new Integer[37];
                                for(int i=0;i<arr.length;i++)
                                    arr[i] = 0;
                                List<Integer> categorySwipeCount = Arrays.asList(arr);
                                user.put("categorySwipeCount", categorySwipeCount);

                                List<String> jP = new ArrayList<>();
                                user.put("jobPreferences", jP);

                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null){
                                            Log.d("SignupActivity","Signup successful!");
                                            final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    });


                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("SignupActivity","Signup failure.");
                    e.printStackTrace();
                }
            }
        });

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password);

    }
}