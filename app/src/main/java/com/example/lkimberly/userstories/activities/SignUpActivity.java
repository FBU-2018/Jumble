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
import com.example.lkimberly.userstories.models.User;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button createBtn;

    ImageView iv_username_correct;
    ImageView iv_password_correct;
    ImageView iv_email_correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.signup_username_et);
        passwordInput = findViewById(R.id.signup_password_et);
        emailInput = findViewById(R.id.signup_email_et);
        iv_username_correct = findViewById(R.id.iv_username_correct);
        iv_password_correct = findViewById(R.id.iv_password_correct);
        iv_email_correct = findViewById(R.id.iv_email_correct);

        createBtn = findViewById(R.id.signup_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isEmptyUsernameText = false;
                boolean isEmptyPasswordText = false;
                boolean isEmptyEmailText = false;

                String username = "";
                String usernameInputStr = usernameInput.getText().toString();

                if (!usernameInputStr.equals("")) {
                    username = usernameInputStr;
                } else {
                    isEmptyUsernameText = true;
                }

                String password = "";
                String passwordInputStr = passwordInput.getText().toString();

                if (!passwordInputStr.equals("")) {
                    password = passwordInputStr;
                } else {
                    isEmptyPasswordText = true;
                }

                String email = "";
                String emailInputStr = emailInput.getText().toString();

                if (!emailInputStr.equals("")) {
                    email = emailInputStr;
                } else {
                    isEmptyEmailText = true;
                }

                if (isEmptyUsernameText || isEmptyPasswordText || isEmptyEmailText) {
                    String requirement = "Please enter a";
                    if (isEmptyUsernameText) {
                        requirement += " username";
                        iv_username_correct.setVisibility(View.INVISIBLE);
                    } else {
                        iv_username_correct.setVisibility(View.VISIBLE);
                    }

                    if (isEmptyPasswordText) {
                        if (isEmptyUsernameText) {
                            requirement += " and password";
                        } else {
                            requirement += " password";
                        }
                        iv_password_correct.setVisibility(View.INVISIBLE);
                    } else {
                        iv_password_correct.setVisibility(View.VISIBLE);
                    }

                    if (isEmptyEmailText) {
                        if (isEmptyUsernameText || isEmptyPasswordText) {
                            requirement += " and email";
                        } else {
                            requirement += "n email";
                        }
                        iv_email_correct.setVisibility(View.INVISIBLE);
                    } else {
                        iv_email_correct.setVisibility(View.VISIBLE);
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
        User user = new User();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
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