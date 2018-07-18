package com.example.lkimberly.userstories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Button signInButton;
    Button signupButton;
    EditText username_et;
    EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Uri number = Uri.parse("tel:");
//
//        Intent intent = new Intent(Intent.ACTION_SEND, number);
//        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, "7049099021");
//        intent.putExtra(Intent.EXTRA_TEXT, "Hi");
//        PackageManager packageManager = getPackageManager();
//        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//        boolean isIntentSafe = activities.size() > 0;
//
//        startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
//        if (isIntentSafe){
//            startActivity(intent);
//        } else {
//            Log.d("Intent is safe", "NOT SAFE MY GUY");
//        }

        signInButton = findViewById(R.id.sign_in_btn);
        signupButton = findViewById(R.id.sign_up_button);
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = username_et.getText().toString();
                final String password = password_et.getText().toString();
                login(username, password);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("MainActivity", "Login was successful!");
                } else {
                    Log.d("MainActivity", "Login failed!");
                    e.printStackTrace();
                }
            }
        });
    }
}
