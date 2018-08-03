package com.example.lkimberly.userstories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUp Activity";

    private EditText usernameInput;
    private EditText passwordInput;
    private Button createBtn;

    ImageView iv_username_correct;
    ImageView iv_password_correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.signup_username_et);
        passwordInput = findViewById(R.id.signup_password_et);
        iv_username_correct = findViewById(R.id.iv_username_correct);
        iv_password_correct = findViewById(R.id.iv_password_correct);

        final boolean[] isEmptyUsernameText = {false};
        final boolean[] isEmptyPasswordText = {false};
        final String[] username = {""};
        final String[] password = {""};

        usernameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String usernameInputStr = usernameInput.getText().toString();

                if (!usernameInputStr.equals("")) {
                    isEmptyUsernameText[0] = false;
                    username[0] = usernameInputStr;
                    iv_username_correct.setVisibility(View.VISIBLE);
                } else {
                    isEmptyUsernameText[0] = true;
                    iv_username_correct.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });

        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String passwordInputStr = passwordInput.getText().toString();

                if (!passwordInputStr.equals("")) {
                    password[0] = passwordInputStr;
                    iv_password_correct.setVisibility(View.VISIBLE);
                } else {
                    isEmptyPasswordText[0] = true;
                    iv_password_correct.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });

        createBtn = findViewById(R.id.signup_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmptyUsernameText[0] || isEmptyPasswordText[0]) {
                    String requirement = "Please enter a";
                    if (isEmptyUsernameText[0]) {
                        requirement += " username";
                    }

                    if (isEmptyPasswordText[0]) {
                        if (isEmptyUsernameText[0]) {
                            requirement += " and password";
                        } else {
                            requirement += " password";
                        }
                    }

                    requirement += "!";
                    Toast.makeText(getApplicationContext(), requirement, Toast.LENGTH_LONG).show();
                } else {
                    signUp(username[0], password[0]);
                }
            }
        });
    }

    private void signUp(String username, String password) {
        // Create the ParseUser
        User user = new User();
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
                    Log.d("SignupActivity","Signup successful!");
                    final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("SignupActivity","Signup failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}