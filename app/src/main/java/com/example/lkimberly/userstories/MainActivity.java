package com.example.lkimberly.userstories;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button signinBtn;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Uri number = Uri.parse("tel:");

        Intent intent = new Intent(Intent.ACTION_SEND, number);
        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, "7049099021");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi");
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
        if (isIntentSafe){
            startActivity(intent);
        } else {
            Log.d("Intent is safe", "NOT SAFE MY GUY");
        }

        signinBtn = findViewById(R.id.sign_in_btn);
        signupBtn = findViewById(R.id.sign_up_btn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}
