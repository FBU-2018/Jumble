package com.example.lkimberly.userstories.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.fragments.CreatePostFragment;
import com.example.lkimberly.userstories.fragments.EditProfileFragment;
import com.example.lkimberly.userstories.fragments.FeedFragment;
import com.example.lkimberly.userstories.fragments.MatchPageFragment;
import com.example.lkimberly.userstories.fragments.ProfileFragment;
import com.example.lkimberly.userstories.models.Job;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    ParseUser user;
    static File photoFile;

    ImageButton ib_profile;

    /**
     * The list of fragments used in the view pager. They live in the activity and we pass them down
     * to the adapter upon creation.
     */
    private final List<Fragment> fragments = new ArrayList<>();


    /**
     * A reference to our view pager.
     */
    private ViewPager viewPager;

    /**
     * A reference to our bottom navigation view.
     */
    private BottomNavigationView bottomNavigation;

    /**
     * The adapter used to display information for our bottom navigation view.
     */

    public static JumbleFragmentAdapter adapter;

    CreatePostFragment myCreatePostFragment;
    MatchPageFragment myMatchPageFragment;

    //match data (job followed by list of users who matched with that job
    List<String> jobObjectIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        initFCM();

        //photoFile = getPhotoFileUri("photo.jpg");

//        createNotificationChannel();
        // Add fragments

        fragments.add(new ProfileFragment());
        fragments.add(new EditProfileFragment());
        fragments.add(new FeedFragment());
        myCreatePostFragment = new CreatePostFragment();
        fragments.add(myCreatePostFragment);
        myMatchPageFragment = new MatchPageFragment();
        fragments.add(myMatchPageFragment);

        // Grab a reference to our view pager.
        viewPager = findViewById(R.id.pager);

        // Instantiate our ExampleAdapter which we will use in our ViewPager
        adapter = new JumbleFragmentAdapter(getSupportFragmentManager(), fragments);

        // Attach our adapter to our view pager.
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.action_home);
                        //homePageRecycler.refresh();
                        //myMatchPageFragment.refresh();
                        break;
                    case 3:
                        bottomNavigation.setSelectedItemId(R.id.action_discover);
                        break;
                    case 4:
                        bottomNavigation.setSelectedItemId(R.id.action_comment);
                        //myMatchPageFragment.refresh();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Grab a reference to our bottom navigation view
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Handle the click for each item on the bottom navigation view.
        // we then delegate this out to the view pager adapter such that it can switch the
        // page which we are currently displaying
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Set the item to the first item in our list.
                        // This is the home placeholder fragment.

                        adapter.notifyDataSetChanged();
                        ib_profile.setSelected(false);

                        // reset the activation of icons after user goes to profile
                        bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.instagram_home_filled_24);
                        bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.instagram_new_post_outline_24);
                        bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_chat_bubble_outline_24dp);

                        viewPager.setCurrentItem(2);
//                        homePageRecycler.refresh();
                        return true;
                    case R.id.action_discover:
                        // Set the item to the first item in our list.
                        // This is the discovery placeholder fragment.

                        adapter.notifyDataSetChanged();
                        ib_profile.setSelected(false);

                        // reset the activation of icons after user goes to profile
                        bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.instagram_home_outline_24);
                        bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.instagram_new_post_filled_24);
                        bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_chat_bubble_outline_24dp);

                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.action_comment:
                        // Set the current item to the third item in our list
                        // which is the profile fragment placeholder


                        adapter.notifyDataSetChanged();
                        // myMatchPageFragment.refresh();
                        ib_profile.setSelected(false);

                        // reset the activation of icons after user goes to profile
                        bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.instagram_home_outline_24);
                        bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.instagram_new_post_outline_24);
                        bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_chat_bubble_filled_24dp);

                        viewPager.setCurrentItem(4);
                        return true;
                    default:
                        return false;
                }
            }
        });


        ib_profile = findViewById(R.id.ib_profile);

        ib_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigation.setSelectedItemId(-1);
                ib_profile.setSelected(true);
                viewPager.setCurrentItem(0);

                // other icons need to be blank when user is on the profile page
                bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.instagram_home_outline_24);
                bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.instagram_new_post_outline_24);
                bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_chat_bubble_outline_24dp);
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        viewPager.setCurrentItem(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//         Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * The example view pager which we use in combination with the bottom navigation view to make
     * a smooth horizontal sliding transition.
     */
    public class JumbleFragmentAdapter extends FragmentPagerAdapter {

        /**
         * The list of fragments which we are going to be displaying in the view pager.
         */
        private final List<Fragment> fragments;

        public JumbleFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);

            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
    }

    public  void setUser() {
        String userId = getIntent().getStringExtra("userId");
        ParseQuery query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId).getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    user = (ParseUser) object;
                }
            }
            @Override
            public void done(Object o, Throwable throwable) {
            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "HomeActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("HomeActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent potentialIntent = data;
        // REQUEST_CODE is defined above
        Log.d("HomeActivity", "checking HomeActivity onActivityResult");
        if (resultCode == RESULT_OK) {
            if (potentialIntent.getStringExtra("refresh") != null) {
                if (potentialIntent.getStringExtra("refresh").equals("true")) {
                    myMatchPageFragment.refresh();
                }
            }

            if (potentialIntent.getBooleanExtra("returnFromMap", false)) {
                Log.d("HomeActivity", "back from Map");
                Job job = Parcels.unwrap(data.getParcelableExtra("newJob"));
                // call create post fragment
                myCreatePostFragment.updateLocation(job);
            }
//            if (potentialIntent.getStringExtra("sendToMessage") != null) {
//                if (potentialIntent.getStringExtra("sendToMessage").equals("true")) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setData(Uri.parse("smsto:" + user.get("phoneNumber")));  // This ensures only SMS apps respond
//                    intent.putExtra("sms_body", "Hi! You matched with my job on Jumble and I'd like to hire you.");
//
//                    startActivity(intent);
//                }
//            }
        }
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

//    private void sendRegistrationToServer(String token) {
//        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child(getString(R.string.dbnode_users))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(getString(R.string.field_messaging_token))
//                .setValue(token);
//    }
//
//
//    private void initFCM(){
//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "initFCM: token: " + token);
//        sendRegistrationToServer(token);
//
//    }
}