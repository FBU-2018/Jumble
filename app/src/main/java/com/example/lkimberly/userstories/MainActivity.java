package com.example.lkimberly.userstories;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {


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

    }
}
