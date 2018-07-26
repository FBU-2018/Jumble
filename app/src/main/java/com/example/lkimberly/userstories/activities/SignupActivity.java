package com.example.lkimberly.userstories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.User;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.signup_username_et);
        passwordInput = findViewById(R.id.signup_password_et);
        emailInput = findViewById(R.id.signup_email_et);
        createBtn = findViewById(R.id.signup_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username =  usernameInput.getText().toString();
                final String password =  passwordInput.getText().toString();
                final String email = emailInput.getText().toString();

                signUp(username, password, email);
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
                    final Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
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
