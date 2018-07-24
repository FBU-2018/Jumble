package com.example.lkimberly.userstories.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lkimberly.userstories.BitmapScaler;
import com.example.lkimberly.userstories.activities.MapActivity;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.models.Job;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.lkimberly.userstories.fragments.ProfileFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.example.lkimberly.userstories.fragments.ProfileFragment.GET_FROM_GALLERY;

public class CreatePostFragment extends Fragment {

    ImageView ivPhoto;

    TextView tvTitle;
    TextView tvDescription;
    TextView tvDate;
    TextView tvTime;
    TextView tvLocation;

    EditText etTitle;
    EditText etDescription;
    EditText etDate;
    EditText etTime;
//    EditText etLocation;

    ImageButton ibPhoto;
    Button bCreateJob;
    String photoFileName = "photo.jpg";
    File photoFile;

    Job newJob;
    ParseFile parseFile;

    // Calendar init
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "MM/dd/yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

    private static final String TAG = "CreatePostFragment";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
//        etLocation = view.findViewById(R.id.etLocation);

        ibPhoto = view.findViewById(R.id.ibPhoto);
        bCreateJob = view.findViewById(R.id.bCreateJob);
        ivPhoto = getActivity().findViewById(R.id.ivPhoto);

        bCreateJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newJob = new Job();

                newJob.setTitle(etTitle.getText().toString());
                newJob.setDescription(etDescription.getText().toString());
                newJob.setTime(etTime.getText().toString());
                newJob.setDate(etDate.getText().toString());
//                newJob.setLocation(etLocation.getText().toString());
                newJob.put("image", parseFile);

                etTitle.setText("");
                etDescription.setText("");
                etTime.setText("");
                etDate.setText("");
//                etLocation.setText("");

                newJob.setUser(ParseUser.getCurrentUser());
                final ParseFile parseFile = new ParseFile(photoFile);

                Log.d("newJobSave", "1. Success!");

                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            newJob.setImage(parseFile);

                            newJob.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("CreatePostProject", "save job success!");
                                        Toast.makeText(getContext(), "Job saved", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d("CreatePostProject", "save job failed!");
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Log.d("CreatePostProject 2", "save job failed!");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });

        // init - set date to current date
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        etDate.setText(dateString);

        // set calendar date and update editDate
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };

        // onclick - popup datepicker
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedMinute < 10) {
                            if (selectedHour > 12) {
                                selectedHour -= 12;
                                etTime.setText(selectedHour + ":0" + selectedMinute + " PM");
                            } else {
                                etTime.setText(selectedHour + ":0" + selectedMinute + " AM");
                            }
                        } else {
                            if (selectedHour > 12) {
                                selectedHour -= 12;
                                etTime.setText(selectedHour + ":" + selectedMinute + " AM");
                            } else {
                                etTime.setText(selectedHour + ":" + selectedMinute + " PM");
                            }
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        if (isServicesOK()) {
            init();
        }
    }

    private void updateDate() {
        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("")
                    .setMessage("Take photo from:")
                    .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

                        }
                    })
                    .setNeutralButton("Go back to my profile", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EditProfileFragment");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("CreatePostFragment", "failed to create post");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String imagePath = photoFile.getAbsolutePath();
                Bitmap rawTakenImage = BitmapFactory.decodeFile(imagePath);
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 400);
                ivPhoto.setImageBitmap(resizedBitmap);
                parseFile = new ParseFile(new File(imagePath));
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        Button btnMap = getActivity().findViewById(R.id.btnMap);
        btnMap.setAllCaps(false);
        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}