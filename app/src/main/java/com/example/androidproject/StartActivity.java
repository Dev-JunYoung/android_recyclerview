package com.example.androidproject;

import static com.example.androidproject.MainActivity.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StartActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static final int PERMISSIONS_FINE_LOCATION = 99;
    // ??????
    // ??????
    // ??????
    // ????????????
    // ????????????
    // ????????????, ????????????
    TextView tv_ing_distance, tv_ing_step, tv_ing_time, tv_ing_height, tv_ing_speed, tv_ing_AccuracySpeed, tv_ing_maxHeight, tv_ing_minHeight;

    TextView tv_latitude, tv_longitude;

    String distance;
    String step;
    String time, height, speed, minHeight, maxHeight;

    int numHeight, numMinHeight, numMaxHeight, numTemp;

    FusedLocationProviderClient fusedLocationProviderClient;


    Button btn_finish;
    ToggleButton btn_flag;

    // sensor
    SensorManager sensorManager;
    Sensor stepCountSensor;
    // ?????? ????????? ??????.
    int currentSteps;
    int moveDistance;

    // ??????
    long MillisecondTime = 0L;  // ???????????? ?????? ????????? ????????? ?????? ??????
    long StartTime = 0L;        // ???????????? ?????? ?????? ????????? ??? ?????? ????????? ??????
    long TimeBuff = 0L;         // ???????????? ???????????? ?????? ????????? ?????? ??? ??????
    long UpdateTime = 0L;       // ???????????? ???????????? ?????? ????????? ?????? ??? ?????? + ?????? ?????? ????????? ??? ?????? ????????? ?????? = ??? ??????
    // ?????????
    Handler handler;            //
    // ??????
    int Seconds, Minutes, MilliSeconds, hour, totalSeconds;


    boolean stepFlag = true;

    RecordItemData data;
    ArrayList<RecordItemData> mArrayList;

    // ?????? ??????
    private LocationRequest locationRequest;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tv_ing_distance = findViewById(R.id.tv_ing_distance);
        tv_ing_step = findViewById(R.id.tv_ing_step);
        tv_ing_time = findViewById(R.id.tv_ing_time);
        tv_ing_height = findViewById(R.id.tv_ing_height);

        tv_ing_speed = findViewById(R.id.tv_ing_speed);
        tv_ing_AccuracySpeed = findViewById(R.id.tv_ing_AccuracySpeed);


        tv_ing_maxHeight = findViewById(R.id.tv_ing_maxHeight);
        tv_ing_minHeight = findViewById(R.id.tv_ing_minHeight);

        tv_longitude = findViewById(R.id.longitude);
        tv_latitude = findViewById(R.id.latitude);


        btn_flag = findViewById(R.id.btn_flag);
        btn_finish = findViewById(R.id.btn_finish);


        // ?????? ??????
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        mArrayList = new ArrayList<>();

        locationRequest = LocationRequest.create();
        // PRIORITY_HIGH_ACCURACY -> ????????? ????????? ????????? ??????
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //??????
        // locationRequest.setInterval(5000);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();


        handler = new Handler();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(TimeRun, 0);
        handler.postDelayed(altitudeRun, 0);
        handler.postDelayed(getSpeedRun, 0);

        loadData();
        // ?????? ??? ?????????
        numHeight = 0;

        btn_flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    //?????? ??? ???,
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(TimeRun, 0);
                    stepFlag = true;
                } else {
                    //???????????? ??? ???,
                    // ???????????? ???????????? ?????? ????????? ?????? ??? ??????
                    stepFlag = false;
                    TimeBuff += MillisecondTime;
                    // Runnable ?????? ??????
                    handler.removeCallbacks(TimeRun);
                    // ???????????? ?????? ?????? ??? ?????? ????????? ????????? ????????????.
                }
            }
        });


        btn_finish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // ????????? ?????? ??????????????????
                AlertDialog.Builder ad = new AlertDialog.Builder(StartActivity.this);
                ad.setTitle("finish");
                ad.setMessage("????????? ????????????????????????");

                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();

                        float a = (currentSteps * 6) / totalSeconds;
                        System.out.println("???????????? : " + a);
                        time = String.valueOf(tv_ing_time.getText());
                        ;
                        height = String.valueOf(numMaxHeight - numMinHeight);
                        speed = String.valueOf(a);
                        minHeight = String.valueOf(numMinHeight);
                        maxHeight = String.valueOf(numMaxHeight);
                        TimeZone tz;
                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat("yy-MM-dd HH???");
                        tz = TimeZone.getTimeZone("Asia/Seoul");
                        df.setTimeZone(tz);
                        String timeStamp = df.format(date);

                        if (step == null) {
                            step = "0";
                        }
                        if (distance == null) {
                            distance = "0";
                        }


                        data = new RecordItemData(distance, time, height, step, maxHeight, minHeight, speed, timeStamp);

                        mArrayList.add(data);
                        String json = gson.toJson(mArrayList);
                        editor.putString(user.getId(), json);
                        editor.commit();


                        MillisecondTime = 0L;
                        StartTime = 0L;
                        TimeBuff = 0L;
                        UpdateTime = 0L;
                        Seconds = 0;
                        Minutes = 0;
                        MilliSeconds = 0;


                        //?????? ??????.
                        handler.removeCallbacks(TimeRun);
                        // ?????? ???????????? TextView??? 0?????? ??????????????????.
                        tv_ing_time.setText("00:00:00");

                        Intent intent = new Intent(StartActivity.this, RecordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                    }
                });
                ad.show();
                return false;
            }
        });

    } // onCreate
    public void onStart() {
        super.onStart();
        if (stepCountSensor != null) {
            // ?????? ?????? ??????
            // * ??????
            // - SENSOR_DELAY_NORMAL: 20,000 ??? ?????????
            // - SENSOR_DELAY_UI: 6,000 ??? ?????????
            // - SENSOR_DELAY_GAME: 20,000 ??? ?????????
            // - SENSOR_DELAY_FASTEST: ????????? ??????
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    // Sensor Listener
    public void onSensorChanged(SensorEvent sensorEvent) {
        // ?????? ?????? ????????? ?????????
        if (stepFlag == true) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                if (sensorEvent.values[0] == 1.0f) {
                    // ?????? ???????????? ???????????? ?????? ????????? ??????
                    currentSteps++;
                    moveDistance++;
                    // 6 -> 0.00006
                    //int a=6;
//                    int a=currentSteps*6;
                    int a = currentSteps * 60;

                    String str = String.format("%06d", a);
                    StringBuffer stringBuffer = new StringBuffer(str);
                    tv_ing_step.setText(String.valueOf(currentSteps));

                    distance = String.valueOf(stringBuffer.insert(str.length() - 5, "."));
                    step = String.valueOf(tv_ing_step.getText());

                    tv_ing_distance.setText(distance);
                    System.out.println("currentSteps : " + currentSteps);


                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(user.getId(), null);
        Type type = new TypeToken<ArrayList<RecordItemData>>() {
        }.getType();
        ArrayList<RecordItemData> data = gson.fromJson(json, type);
        System.out.println("data : " + data);
        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
        File file = new File(
                "/data/data/com.example.androidproject/shared_prefs/record.xml"
        );
        if (file.exists()) {
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    mArrayList.add(data.get(i));
                }
            }
        } else {
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    mArrayList.add(data.get(i));

                }
            }
        }
    }

    // ?????? ????????? ?????????
    public Runnable TimeRun = new Runnable() {
        @Override
        public void run() {
            // ??????????????? ????????? ??? ?????? ???????????? ?????? - ?????? ????????? ?????? ??????
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            // ???????????? ???????????? ?????? ????????? ?????? ??? ?????? + ?????? ?????? ????????? ??? ?????? ????????? ?????? = ??? ??????
            UpdateTime = TimeBuff + MillisecondTime;

            totalSeconds = (int) (UpdateTime / 1000);
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            hour = Minutes / 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            // TextView??? UpdateTime??? ???????????????
            tv_ing_time.setText(hour + ":" + Minutes + ":"
                    + String.format("%02d", Seconds));

            getCurrentLocation();

            handler.postDelayed(this, 0);
        }
    };
    // ??????
    public Runnable altitudeRun = new Runnable() {
        @Override
        public void run() {
            getAltitude();
            handler.postDelayed(this, 10000);
        }
    };
    // ??????
    public Runnable getSpeedRun = new Runnable() {
        @Override
        public void run() {
            System.out.println("????????? ?????????");
            System.out.println("moveDistance : " + moveDistance);
            float speed = moveDistance / 5f;
            tv_ing_speed.setText(speed + "");
            handler.postDelayed(this, 5000);
            moveDistance = 0;
        }
    };


    // ?????? ????????? ?????????


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    getCurrentLocation();
                    doStuff();
                } else {
                    turnOnGPS();
                }
            }
        }
    }

    @Override // ?????? return ??????
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation();
            }
        }
    }

    // ?????? ?????? ???????????? ??????.
    private void getCurrentLocation() {
        // ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // gps ?????? ?????? ?????? ??????
                if (isGPSEnabled()) {
                    // LocationServices :?????? ????????? ????????? ?????? ?????? ?????? ???????????????
                    // getFusedLocationProviderClient : Activity ??? ????????? FusedLocationProviderClient ??? ??? ??????????????? ????????????
                    LocationServices.getFusedLocationProviderClient(StartActivity.this)
                            // LocationCallback() : ????????? ?????? ?????? ????????? ???????????? ?????? ??????????????? ???????????????.
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(StartActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        // ??????
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        // ??????
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        // ??????
                                        //double longAltitude=locationResult.getLocations().get(index).getAltitude();
                                        float speed = locationResult.getLocations().get(index).getSpeed();
                                        float getSpeedAccuracyMetersPerSecond = locationResult.getLocations().get(index).getSpeedAccuracyMetersPerSecond();


                                        //tv_ing_speed.setText((int)Math.ceil((speed*3600)/1000)+"");
                                        tv_ing_AccuracySpeed.setText((int) Math.ceil(getSpeedAccuracyMetersPerSecond) + "");
                                        tv_longitude.setText(longitude + "");
                                        tv_latitude.setText(latitude + "");

                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    // gps ??????
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    // ???????????????
    private void getAltitude() {
        // ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(StartActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(StartActivity.this)
                                            .removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        // ??????
                                        double longAltitude = locationResult.getLocations().get(index).getAltitude();
                                        if (numHeight == 0) {
                                            numMinHeight = (int) Math.floor(longAltitude);
                                            numMaxHeight = (int) Math.floor(longAltitude);
                                        }
                                        numHeight = (int) Math.floor(longAltitude);
                                        // ???????????? ?????? ?????? ???????????? ?????? ?????? ????????????,
                                        if (numMinHeight > longAltitude) {
                                            //?????? ??? ??????
                                            numMinHeight = (int) Math.floor(longAltitude);
                                        }
                                        // ???????????? ?????? ?????? ???????????? ?????? ?????? ????????????,
                                        if (numMaxHeight < longAltitude) {
                                            numMaxHeight = (int) Math.floor(longAltitude);
                                        }


                                        System.out.println("????????? ?????????");

                                        tv_ing_maxHeight.setText(numMaxHeight + "");
                                        tv_ing_minHeight.setText(numMinHeight + "");
                                        tv_ing_height.setText(Math.floor(longAltitude) + "");


                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    // gps ??????
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void getCurrentSpeer() {
        // ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // gps ?????? ?????? ?????? ??????
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(StartActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(StartActivity.this)
                                            .removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        float speed = locationResult.getLocations().get(index).getSpeed();
                                        float getSpeedAccuracyMetersPerSecond = locationResult.getLocations().get(index).getSpeedAccuracyMetersPerSecond();

                                        //tv_ing_speed.setText((int)Math.ceil((speed*3600)/1000)+"");
                                        tv_ing_AccuracySpeed.setText((int) Math.ceil(getSpeedAccuracyMetersPerSecond) + "");
                                    }
                                }
                            }, Looper.getMainLooper());
                } else {
                    // gps ??????
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    // gps ???????????????
    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(StartActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(StartActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    // gps ?????? ???????????? ??????
    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        //gps ????????? ????????????  true
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        return isEnabled;
    }


    private void updateGPS(){
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(StartActivity.this);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            // user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //we got permissions. Put the values of location. XXX into the UI components
                    updateUIValues(location);
                }
            });
        }else {
            //permissions not granted yet
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private  void  updateUIValues(Location location){

    }









    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location == null) {
            tv_ing_speed.setText("-.- km/h");
        } else {
            float nCurrentSpeed = location.getSpeed() * 3.6f;

            tv_ing_speed.setText(String.format("%.2f", nCurrentSpeed) + " km/h");
            System.out.println(String.format("%.2f", nCurrentSpeed) + " km/h");
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    private void doStuff() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //commented, this is from the old version
            // this.onLocationChanged(null);
        }
        Toast.makeText(this,"Waiting for GPS connection!", Toast.LENGTH_SHORT).show();


    }






}