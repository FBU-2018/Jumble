package com.example.lkimberly.userstories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private EditText emailInput;
    private Button createBtn;

    ImageView iv_username_correct;
    ImageView iv_password_correct;
    ImageView iv_email_correct;

    // Firebase setup
    private FirebaseAuth mAuth;

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
                    iv_email_correct.setVisibility(View.VISIBLE);
                } else {
                    isEmptyEmailText = true;
                    iv_email_correct.setVisibility(View.INVISIBLE);
                }

                if (isEmptyUsernameText || isEmptyPasswordText || isEmptyEmailText) {
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

                    if (isEmptyEmailText) {
                        if (isEmptyUsernameText || isEmptyPasswordText) {
                            requirement += " and email";
                        } else {
                            requirement += "n email";
                        }
                    }

                    requirement += "!";
                    Toast.makeText(getApplicationContext(), requirement, Toast.LENGTH_LONG).show();
                } else {
                    signUp(username, password, email);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}