package com.example.lkimberly.userstories.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.database.Cursor;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lkimberly.userstories.BitmapScaler;
import com.example.lkimberly.userstories.JobMatchInfo;
import com.example.lkimberly.userstories.R;
import com.example.lkimberly.userstories.activities.MapActivity;
import com.example.lkimberly.userstories.models.Job;
import com.example.lkimberly.userstories.models.User;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.lkimberly.userstories.fragments.ProfileFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.example.lkimberly.userstories.fragments.ProfileFragment.GET_FROM_GALLERY;
import static com.parse.ParseUser.getCurrentUser;

public class CreatePostFragment extends Fragment {

    private static final String TAG = "CreatePostFragment";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private FusedLocationProviderClient mFusedLocationProvidentClient;
    private Boolean mLocationPermissionsGranted = false;
    Button btnMap;

    ImageView ivPhoto;
    ImageView ivJobPhoto;
    EditText etTitle;
    EditText etDescription;
    EditText etDate;
    EditText etTime;
    //EditText etLocation;
    EditText etEstimation;
    EditText etMoney;

    ImageButton ibPhoto;
    Button bCreateJob;
    String photoFileName = "photo.jpg";
    File photoFile;

    Job newJob;
    ParseFile parseFile;
    String imagePath;

    ImageView iv_title_complete;
    ImageView iv_description_complete;
    ImageView iv_date_time_complete;
    ImageView iv_estimation_complete;
    ImageView iv_money_complete;
    ImageView iv_location_complete;

    // Calendar init
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "MM/dd/yy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private final int REQUEST_CODE = 123;

    static String jobTitle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        getLocationPermisison();

        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);

        //etLocation = view.findViewById(R.id.etLocation);
        etEstimation = view.findViewById(R.id.etEstimation);
        etMoney = view.findViewById(R.id.etMoney);

        ibPhoto = view.findViewById(R.id.ibPhoto);
        bCreateJob = view.findViewById(R.id.bCreateJob);
        ivPhoto = getActivity().findViewById(R.id.ivPhoto);
        ivJobPhoto = getActivity().findViewById(R.id.ivJobPhoto);

        iv_title_complete = view.findViewById(R.id.iv_title_complete);
        iv_description_complete = view.findViewById(R.id.iv_detail_complete);
        iv_date_time_complete = view.findViewById(R.id.iv_date_time_complete);
        iv_estimation_complete = view.findViewById(R.id.iv_hours_complete);
        iv_money_complete = view.findViewById(R.id.iv_fee_complete);
        iv_location_complete = view.findViewById(R.id.iv_location_complete);

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iv_title_complete.setVisibility(View.VISIBLE);

                if (i == 0 && i2 == 0) {
                    iv_title_complete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iv_description_complete.setVisibility(View.VISIBLE);

                if (i == 0 && i2 == 0) {
                    iv_description_complete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 0) {
                    iv_date_time_complete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etDate.equals("")) {
                    iv_date_time_complete.setVisibility(View.VISIBLE);
                }
            }
        });

        etEstimation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 0) {
                    iv_estimation_complete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                iv_estimation_complete.setVisibility(View.VISIBLE);
            }
        });

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0 && i2 == 0) {
                    iv_estimation_complete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                iv_money_complete.setVisibility(View.VISIBLE);
            }
        });

        newJob = new Job();

        bCreateJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTitleEmpty = false;
                boolean isDescriptionEmpty = false;
                boolean isTimeDateEmpty = false;
                boolean isEstimationEmpty = false;
                boolean isMoneyEmpty = false;
                boolean isImagePathEmpty = false;

                String title = etTitle.getText().toString();

                if (!title.equals("")) {
                    newJob.setTitle(title);
                    etTitle.setText("");
                } else {
                    isTitleEmpty = true;
                }

                String description = etDescription.getText().toString();

                if (!description.equals("")) {
                    newJob.setDescription(description);
                    etDescription.setText("");
                } else {
                    isDescriptionEmpty = true;
                }

                String time = etTime.getText().toString();
                String date = etDate.getText().toString();


                if (!time.equals("") && !date.equals("")) {
                    newJob.setTime(time);
                    newJob.setDate(date);
                } else {
                    isTimeDateEmpty = true;
                }

                String estimation = etEstimation.getText().toString();

                if (!estimation.equals("")) {
                    newJob.setEstimation(estimation);
                    etEstimation.setText("");
                } else {
                    isEstimationEmpty = true;
                }

                String money = etMoney.getText().toString();

                if (!money.equals("")) {
                    newJob.setMoney(money);
                    etMoney.setText("");
                } else {
                    isMoneyEmpty = true;
                }

                btnMap.setText("");

                //newJob.setLocation("1101 Dextor Ave. Seattle, WA98101");

                if (imagePath == null) {
                    isImagePathEmpty = true;
                }

                if (isTitleEmpty || isDescriptionEmpty || isTimeDateEmpty || isEstimationEmpty || isMoneyEmpty || isImagePathEmpty) {

                    String message = "Please enter a ";
                    if (isTitleEmpty) {
                        message += "title";
                    }

                    if (isDescriptionEmpty) {
                        if (isTitleEmpty) {
                            message += " and description";
                        } else {
                            message += "description";
                        }
                    }

                    if (isTimeDateEmpty) {
                        if (isTitleEmpty || isDescriptionEmpty) {
                            message += " and time or date";
                        } else {
                            message += "time or date";
                        }
                    }

                    if (isEstimationEmpty) {
                        if (isTitleEmpty || isDescriptionEmpty || isTimeDateEmpty) {
                            message += " and estimation";
                        } else {
                            message += "n estimation";
                        }
                    }

                    if (isMoneyEmpty) {
                        if (isTitleEmpty || isDescriptionEmpty || isTimeDateEmpty || isEstimationEmpty) {
                            message += " and fee";
                        } else {
                            message += "fee";
                        }
                    }

                    if (isImagePathEmpty) {
                        message = "Please give this job a photo";
                    }
                    message += "!";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {

                    newJob.setUser(ParseUser.getCurrentUser());

                    final ParseFile parseFile = new ParseFile(new File(imagePath));

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
                                            String objectId = newJob.getObjectId();
                                            jobTitle = newJob.getTitle();

                                            Log.d("CreatePostProject", "save job id = " + objectId);
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("JobMatchInfo")
                                                    .child(objectId).child("Details");
                                            myRef.setValue("");

                                            ValueEventListener listener = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String key = dataSnapshot.getKey();
                                                    Object value = dataSnapshot.getValue();

                                                    if (value == null) {
                                                        return;
                                                    }

                                                    String subscribedObjectId = value.toString();

                                                    Log.d("firebase listener", key + " and " + subscribedObjectId);
                                                    Toast.makeText(getContext(), subscribedObjectId + " subscribed " + jobTitle, Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            };

                                            myRef.addValueEventListener(listener);

                                            FeedFragment.ValueEventListenerList.add(listener);

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
            }
        });

        ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });

        // init - set date to current date
        long currentDate = System.currentTimeMillis();
        String dateString = sdf.format(currentDate);
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
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });

        // set the fee

        etMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeePickerFragment feePickerDialog = new FeePickerFragment();
                feePickerDialog.show(getFragmentManager(), "FeePickerDialog");
            }
        });

        // set the time of the job

        //Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat mdFormat = new SimpleDateFormat("hh:mm a");
        //String currentTime = mdFormat.format(calendar.getTime());

        etTime.setText("12:00 AM");

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

                        Log.d("time picker", "selectedHour = " + selectedHour + ", selectedMinute = " + selectedMinute);
                        String minStr = ":";
                        if (selectedMinute < 10) {
                            minStr = minStr + "0";
                        }

                        String periodStr = "AM";

                        if (selectedHour >= 12) {
                            periodStr = "PM";

                            if (selectedHour > 12) {
                                selectedHour -= 12;
                            }
                        }

                        String hourStr = String.valueOf(selectedHour);
                        if (selectedHour < 10) {
                            hourStr = "0" + hourStr;
                        }

                        etTime.setText(hourStr + minStr + selectedMinute + " " +  periodStr);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // time estimation

        etEstimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerFragment numberPickerDialog = new NumberPickerFragment();
                numberPickerDialog.show(getFragmentManager(), "NumberPickerDialog");
            }
        });

        if (isServicesOK()) {
            init();
        }

    }


    private void updateDate() {
        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void init() {
        if (mLocationPermissionsGranted) {
                getDeviceLocation();
        }

        btnMap = getActivity().findViewById(R.id.btnMap);
        btnMap.setAllCaps(false);
        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("newJob", Parcels.wrap(newJob));
                startActivityForResult(intent, REQUEST_CODE);
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

        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                User user = (User) getCurrentUser();

                imagePath = photoFile.getAbsolutePath();
                Bitmap rawTakenImage = BitmapFactory.decodeFile(imagePath);
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 400);
                Bitmap rotatedBitmap = rotate(resizedBitmap, imagePath);
                ivPhoto.setImageBitmap(rotatedBitmap);
            }

            if (requestCode == GET_FROM_GALLERY) {
                User user = (User) getCurrentUser();

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                Bitmap rawTakenImage = BitmapFactory.decodeFile(imagePath);
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 400);

                Bitmap rotatedBitmap = rotate(resizedBitmap, imagePath);
                ivPhoto.setImageBitmap(rotatedBitmap);
                cursor.close();
            }
        } else { // Result was a failure
            Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }

//        Log.d("Back from MapActivity", "1");
//        if (requestCode == REQUEST_CODE) {
//            Log.d("Back from MapActivity", "2");
//            if (resultCode == RESULT_OK) {
//                Log.d("Back from MapActivity", "HELLO");
//                newJob = Parcels.unwrap(data.getParcelableExtra("newJob"));
//                btnMap.setText(newJob.getLocation());
//            } else {
//                Log.d("Something went wrong", "sad");
//            }
//        }
    }

    public void updateLocation(Job job) {
        Log.d("CreatePostFragment", "trying to update location");
        newJob = job;
        btnMap.setText(job.getLocation());
        iv_location_complete.setVisibility(View.VISIBLE);
    }

    // make a function get map info --> does what code above does

    public Bitmap rotate(Bitmap bitmap, String imagePath) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void getLocationPermisison() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
//                initMap();
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    // initialize our map
//                    initMap();
                }
            }
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProvidentClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
//            if (mLocationPermissionsGranted) {
            Task location = mFusedLocationProvidentClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location currentLocation = (Location) task.getResult();

                            double latitude = currentLocation.getLatitude();
                            double longitude = currentLocation.getLongitude();

                            StringBuilder result = new StringBuilder();
                            try {
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    btnMap.setText(address.getAddressLine(0));
                                    iv_location_complete.setVisibility(View.VISIBLE);
//                                    result.append(address.getLocality());
//                                    result.append(address.getCountryName());
                                }
                            } catch (IOException e) {
                                Log.e("tag", e.getMessage());
                            }

//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }
}