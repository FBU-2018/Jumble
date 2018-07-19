package com.example.lkimberly.userstories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.Matches;
import com.example.lkimberly.userstories.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    Button signInButton;
    Button signupButton;
    EditText username_et;
    EditText password_et;

    List<Job> jobs = new ArrayList<>();

    ParseUser poster = new User();
    ParseUser subscriber = new User();

    Matches match;

    Matches m2 = new Matches();

    Job job = new Job();

    boolean one;
    boolean two;
    boolean three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Log.d("LoginActivity", "Login successful");
            final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Continue to show the signup or login screen
        }

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

        // Create job w/ me as user
//        ParseQuery userQuery1 = ParseUser.getQuery();
//        userQuery1.whereEqualTo("objectId", "269f6ODGHQ").findInBackground(new FindCallback<User>() {
//            @Override
//            public void done(List<User> objects, ParseException e) {
//                if (e == null) {
//                    job.setUser((ParseUser) objects.get(0));
//                    job.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            Log.d("we made it", "yakwtfgo");
//                        }
//                    });
//
//
//                } else {
//                    Log.d("Doesn't exist", "Nadda");
//                }
//            }
//        });

        // Check if match has been properly added
//        final Matches.Query query = new Matches.Query();
//        query.getTop()
//                .withJobPoster()
//                .withJobSubscriber()
//                .findInBackground(new FindCallback<Matches>() {
//                                      @Override
//                                      public void done(List<Matches> objects, ParseException e) {
//                                          if (e == null) {
//                                              try {
//
//                                                  Log.d("Please work", objects.toString() + objects.get(0).fetchIfNeeded().get("objectId"));
//                                              } catch (ParseException e1) {
//                                                  e1.printStackTrace();
//                                              }
//                                          }
//                                      }
//                                  });

        // Create new matches

//        final Matches.Query query = new Matches.Query();
//        query.getTop()
//                .findInBackground(new FindCallback<Matches>() {
//                    @Override
//                    public void done(List<Matches> objects, ParseException e) {
//                        if (e == null) {
//                            //match = (Matches) objects.get(0);
//
//
//                            final Job.Query query = new Job.Query();
//                            query.getTop()
//                                    .findInBackground(new FindCallback<Job>() {
//                                        @Override
//                                        public void done(List<Job> objects, ParseException e) {
//                                            if (e == null) {
//                                                jobs.addAll(objects);
//                                                Log.d("Jobs", jobs.toString());
//
//
//                                                ParseQuery userQuery1 = ParseUser.getQuery();
//                                                userQuery1.whereEqualTo("objectId", "269f6ODGHQ").findInBackground(new FindCallback<User>() {
//                                                    @Override
//                                                    public void done(List<User> objects, ParseException e) {
//                                                        if (e == null) {
//                                                            m2.setJobPoster((ParseUser) objects.get(0));
//                                                            Log.d("Round 1", "We gud");
//
//                                                            ParseQuery userQuery1 = ParseUser.getQuery();
//                                                            userQuery1.whereEqualTo("objectId", "wWfWsGamjo").findInBackground(new FindCallback<User>() {
//                                                                @Override
//                                                                public void done(List<User> objects, ParseException e) {
//                                                                    if (e == null) {
//                                                                        m2.setJobSubscriber((ParseUser) objects.get(0));
//                                                                        Log.d("Round 1", "We gud");
//                                                                        m2.setJob(jobs.get(1));
//                                                                        m2.saveInBackground(new SaveCallback() {
//                                                                            @Override
//                                                                            public void done(ParseException e) {
//                                                                                Log.d("we made it", "yakwtfgo");
//                                                                            }
//                                                                        });
////                                                                        job.saveInBackground();
////                                                                        m2.saveInBackground();
//
//
//                                                                    } else {
//                                                                        Log.d("Doesn't exist", "Nadda");
//                                                                    }
//                                                                }
//                                                            });
//
//                                                        } else {
//                                                            Log.d("Doesn't exist", "Nadda");
//                                                        }
//                                                    }
//                                                });
//                                            } else {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//
//
//                        } else {
//
//                        }
//                    }
//                });



//
//
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
//
//        homeButton = findViewById(R.id.home_btn);
//
//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });


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
