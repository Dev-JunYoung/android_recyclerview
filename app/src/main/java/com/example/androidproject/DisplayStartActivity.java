package com.example.androidproject;

import static com.example.androidproject.MainActivity.loginCheck;
import static com.example.androidproject.MainActivity.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//FusedLocationProviderApi????????? ????????? ???????????? ??? ???????????????.

public class DisplayStartActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    final String TAG="DisplayStartActivity";
    public static final int PERMISSIONS_FINE_LOCATION=99;
    // service
    // static TextView tv_ing_time,tv_ing_step,tv_ing_speed,tv_ing_height,tv_ing_distance,tv_current_speed;
    TextView tv_ing_time,tv_ing_step,tv_ing_speed,tv_ing_height,tv_ing_distance,tv_current_speed;
    RecordItemData data;
    String distance;
    String step;
    String time, height, speed, minHeight, maxHeight, speedAvg;
    String address;
    String makerName;

    Intent startFlagIntent;
    double latitude;
    double longitude;

    int hour;
    int minute;
    int second;

    int resultSecond;
    int resultMinute;
    int resultHour;


    double total_distance;
    int total_step;

    public static String profile_distance;
    public static String profile_time;
    public static String profile_step;
    public static String profile_cnt;


    private Notification mNotification;
    private NotificationManager mNotificationManager;

    // bottomSheet
    View bottomView;
    BottomSheetDialog bottomSheetDialog;


    boolean serviceInit=false;

    PolylineOptions polylineOptions;
    private GoogleMap googleMap;
    private LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    Handler handler;
    ArrayList<RecordItemData> mArrayList;

    private Button btn_display_start, btn_record, btn_board, btn_profile, btn_mnt_list,btn_detail,btn_finish,btn_open_bt_sheet,btn_weather;
    private ToggleButton btn_flag;
    // ????????? ???????????? ????????????
    private RecordService mService;
    private ServiceConnection mConnection;
    // ????????? ?????? ?????? ??????
    private boolean mBound;
    private boolean initLocation;
    private boolean serviceFlag;


    boolean firstStart=true;
    // null Error ?????? ????????? ?????? ????????? ??????
    SupportMapFragment mapFragment;

    //ArrayList<UserLocation> userLocationsArr=new ArrayList<>();
    ArrayList<LatLng> userLocationsArr;

    LocationManager locationManager;
    LocationListener locationListener;

    private long backpressedTime = 0;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ????????????????????? ???????????????.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(DisplayStartActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override //menu?????? ?????????
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override //menu ?????? ?????? choose
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
                // ?????? ??????, ????????????.
                AlertDialog.Builder ad = new AlertDialog.Builder(DisplayStartActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("????????? ?????? ??????");
                ad.setMessage("????????? ?????? ??????");

                final EditText et = new EditText(DisplayStartActivity.this);
                ad.setView(et);
                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //onVisibleBehindCanceled();
                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getCurrentLocation();
                        String result = et.getText().toString();
                        //tv_name.setText("?????? : "+result);
                        dialogInterface.dismiss();
                        Log.e(TAG,"Maker latitude : "+latitude);
                        Log.e(TAG,"Maker longitude : "+longitude);
                        ArrayList<Marker> list=new ArrayList();
                        Marker a=googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(et.getText().toString()));
                        list.add(a);


                    }
                });
                ad.show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }

   /* private void mapView() {
        Uri location = Uri.parse("geo:0,0?q=??????????????????");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        String title = "?????? ???????????????";
// Create intent to show chooser
        Intent chooser = mapIntent.createChooser(mapIntent, title);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
// Try to invoke the intent.
        try {
            startActivity(chooser);
        } catch (ActivityNotFoundException e) {
        }
    }
*/


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate");
        if(loginCheck==true){
            SharedPreferences sharedPreferences=getSharedPreferences("currentUser",MODE_PRIVATE);
            String json=sharedPreferences.getString("current",null);
            Gson gson=new Gson();
            Type type=new TypeToken<User>(){}.getType();
            User data=gson.fromJson(json,type);
            Toast.makeText(DisplayStartActivity.this, data.getName()+"?????? ????????? ???????????????. ", Toast.LENGTH_SHORT).show();
            loginCheck=false;
        }
        initLocation=true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_start);

        bottomView= LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.bottome_sheet,(ConstraintLayout)findViewById(R.id.bottomSheetContainer));

        tv_ing_time=bottomView.findViewById(R.id.tv_ing_time);
        tv_ing_step=bottomView.findViewById(R.id.tv_ing_step);
        tv_ing_speed=bottomView.findViewById(R.id.tv_ing_speed);
        tv_ing_height=bottomView.findViewById(R.id.tv_ing_height);
        tv_ing_distance=bottomView.findViewById(R.id.tv_ing_distance);
        tv_current_speed=findViewById(R.id.tv_current_speed);

        btn_flag = findViewById(R.id.btn_flag);
        btn_finish= findViewById(R.id.btn_finish);
        btn_detail= findViewById(R.id.btn_detail);
        btn_mnt_list=findViewById(R.id.btn_mnt_list);
        btn_mnt_list.setOnClickListener(this);
        btn_weather=findViewById(R.id.btn_weather);
        btn_weather.setOnClickListener(this);

        btn_display_start = findViewById(R.id.btn_display_start);
        btn_record = findViewById(R.id.btn_record);
        btn_board = findViewById(R.id.btn_board);
        btn_profile = findViewById(R.id.btn_profile);
        // ?????? ??? -> ??? btn_flag
        // btn_start = findViewById(R.id.btn_start);
        // btn_flag.setOnClickListener(this);

        //check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start the program if permission is granted
            //doStuff();
        }

        btn_flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e(TAG,"isMyServiceRunning(RecordService.class)"+isMyServiceRunning(RecordService.class));
                if(!isChecked){
                    if(mService!=null){
                        mService.setFlag(true);
                    }

                    userLocationsArr=new ArrayList<>();
                    googleMap.clear();
                    for (int i = 0; i < userLocationsArr.size(); i++){
                        Log.e(TAG," userLocationsArr : "+userLocationsArr.get(i).latitude);
                        Log.e(TAG," userLocationsArr : "+userLocationsArr.get(i).longitude);
                    }
                    //userLocationsArr.clear();
                    //doStuff();
                    Log.e(TAG,"onCheckedChanged");
                    Log.e(TAG,"????????????");
                    Log.e("RecordService","????????????");
                    // ??????
                    btn_detail.setEnabled(true);
                    btn_finish.setEnabled(true);
                    if(serviceInit){ //??? ?????? ?????? ??? why mService.setServiceLive() ??? ????????? ????????? null
                        mService.setServiceLive(true);
                    }
                    if(firstStart) {
                        userLocationsArr.clear();
                        firstStart=false;
                        Log.e(TAG,"????????????????????? ??????.");
                    }
                    serviceInit=true;
                    Log.e(TAG,"isMyServiceRunning : "+isMyServiceRunning(RecordService.class));
                    /*if(isMyServiceRunning(RecordService.class)!=true){
                        Log.e("DisplayStartActivity","????????? ?????? ?????? ??? ?????????");
                        bindService();
                    }*/
                    if(serviceFlag!=true){
                        Log.e("DisplayStartActivity","????????? ?????? ?????? ??? ?????????");
                        bindService();
                        serviceFlag=true;
                    }
                    bottomSheetDialog.setContentView(bottomView);
                    bottomSheetDialog.show();
                    // ????????? ?????? ( ??????????????? ????????? ??? ?????????, UI??? 1????????? setText ????????? ????????? )
                    handler.postDelayed(recordRun,100);
                    // handler.postDelayed(mapRun,1000);
                    //doStuff();
                }else {
                    // ?????? ??????
                    mService.setFlag(false);
                    Log.e(TAG,"????????????");
                    Log.e("RecordService","????????????");
                  //  mService.setServiceLive(false);
                    mService.getTimeTh().interrupt();
                    mService.getTextTh().interrupt();
                  //  locationManager.removeUpdates(locationListener);
                    handler.removeCallbacks(recordRun);
                    //  handler.removeCallbacks(mapRun);
                }
            }
        });

        btn_finish.setEnabled(false);
        btn_detail.setEnabled(false);
        btn_detail.setOnClickListener(this);
        btn_finish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // ????????? ?????? ??????????????????
                Log.e(TAG,"????????????");
                Log.e("RecordService","????????????");
                Log.e(TAG,"btn_finish.onLongClick");
                AlertDialog.Builder ad = new AlertDialog.Builder(DisplayStartActivity.this);
                ad.setTitle("finish");
                ad.setMessage("????????? ????????????????????????");
                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        Log.e(TAG,"onLongClick.??????Click");
                        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        // TimeStamp --------------------------------------------------------------------------------
                        TimeZone tz;
                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat("yy-MM-dd HH???");
                        tz = TimeZone.getTimeZone("Asia/Seoul");
                        df.setTimeZone(tz);
                        String timeStamp = df.format(date);
                        // TimeStamp --------------------------------------------------------------------------------
                        if (distance == null) {
                            distance = "0";
                        }
                        if (step == null) {
                            step = "0";
                        }
                        if (height == null) {
                            height = "0";
                        }
                        if (speedAvg == null) {
                            speedAvg = "0";
                        }
                        if (maxHeight == null) {
                            maxHeight = "0";
                        } if (minHeight == null) {
                            minHeight = "0";
                        }
                        data = new RecordItemData(distance, time, height, step, maxHeight, minHeight, speedAvg, timeStamp,userLocationsArr);


                        mArrayList.clear();
                        loadData();
                        mArrayList.add(data);
                        String json = gson.toJson(mArrayList);
                        editor.putString(user.getId(), json);
                        editor.commit();


                        // ????????? ??????
                        // ????????? ??????
                        mService.getTimeTh().interrupt();
                        mService.getTextTh().interrupt();
                        mService.setServiceLive(false);


                        serviceFlag=false;
                        mBound=false;
                        firstStart=true;
                        // ???????????? - ?????? ????????? ???????????? ????????????????????? ????????????.
                      /*  if(mConnection!=null){

                        }*/


                        unbindService(mConnection);
                        //mService.unbindService(mConnection);
                        stopService(startFlagIntent);

                        mConnection=null;

                        // ????????? ?????????
                        //handler.removeCallbacks(mapRun);
                        handler.removeCallbacks(recordRun);

                        //?????????, ???????????? ?????????
                        googleMap.clear();
                        userLocationsArr.clear();

                        // TV ?????????
                        tv_ing_time.setText("0:0:00");
                        tv_ing_step.setText("0");
                        tv_ing_speed.setText("0");
                        tv_ing_height.setText("0");
                        tv_ing_distance.setText("0.000");

                        //
                        Intent intent=new Intent(DisplayStartActivity.this,RecordActivity.class);
                        startActivity(intent);
                        btn_flag.setChecked(true);
                        Log.e(TAG,"//btn_finish.onLongClick");

                        btn_detail.setEnabled(false);
                        btn_finish.setEnabled(false);

                       // finish();
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

        btn_display_start.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_board.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        // btn_start.setOnClickListener(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        handler =new Handler();

        // ?????? ??????
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        //  handler.postDelayed(mapRun,1000);
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DisplayStartActivity.this);

        bottomSheetDialog=new BottomSheetDialog(
                DisplayStartActivity.this
        );

        mArrayList = new ArrayList<>();
        loadData();

        mNotificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getCurrentLocation();

        Log.e(TAG,"//onCreate");
    }//onCreate



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_display_start:
                Intent intent = new Intent(DisplayStartActivity.this, DisplayStartActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_record:
                Intent intent2 = new Intent(DisplayStartActivity.this, RecordActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_board:
                Intent intent3 = new Intent(DisplayStartActivity.this, BoardActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_profile:
                Intent intent4 = new Intent(DisplayStartActivity.this, ProfileActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn_mnt_list:
                Intent intent7 = new Intent(DisplayStartActivity.this, MountainListActivity.class);
                startActivity(intent7);
                //??????
                break;
            case R.id.btn_detail:
                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();
                break;
            case R.id.btn_weather:
                TransLocalPoint transLocalPoint=new TransLocalPoint();
                //RecordService service=new RecordService();
                //latitude=service.getLatitude();
                //longitude=service.getLongitude();
                TransLocalPoint.LatXLngY tmp=transLocalPoint.convertGRID_GPS(0,latitude,longitude);
                Log.e(TAG,"tmp.x : "+tmp.x+"    tmp.y : "+tmp.y);
                Intent intent10=new Intent(DisplayStartActivity.this,WeatherActivity.class);
                intent10.putExtra("latitude",tmp.x);
                intent10.putExtra("longitude",tmp.y);
                intent10.putExtra("address",address);
                startActivity(intent10);
                break;

        }
    }
    // ????????? ????????? ???????????? setText ????????? ?????????
    public Runnable recordRun=new Runnable(){
        @Override
        public void run() {
            Log.e(TAG,"recordRun");
            if(mBound){
                //Log.e(TAG,"recordRun_if(mBound)True");
                Log.e(TAG,"mService.getSendTime() : "+mService.getSendTime());
                Log.e(TAG,"mService.getSendTime() : "+mService.getSendStep());
                Log.e(TAG,"mService.getSendTime() : "+mService.getSendSpeed());
                Log.e(TAG,"mService.getSendTime() : "+mService.getSendAltitude());
                mService.getSendTime();
                tv_ing_time.setText(mService.getSendTime());
                tv_ing_step.setText(mService.getSendStep());
                tv_ing_speed.setText(mService.getSendSpeed());
                tv_ing_height.setText(mService.getSendAltitude());
                tv_ing_distance.setText(mService.getSendDistance());
                tv_current_speed.setText(mService.getSendCurrent()+"km");

                distance=mService.getSendDistance();
                step=mService.getSendStep();
                time=mService.getSendTime();
                height=mService.getSendHeight();
                speed=mService.getSendSpeed();
                minHeight=mService.getSendMinHeight();
                maxHeight=mService.getSendMaxHeight();
                speedAvg=mService.getSendSpeedAvg();

                latitude=mService.getLatitude();
                longitude=mService.getLongitude();

              /*  Log.e(TAG,"mService.latitude : "+latitude);
                Log.e(TAG,"mService.longitude : "+longitude);*/

                //userLocationsArr.add(new LatLng(latitude,longitude));


                if(mService.getUserLocationsArr()!=null){
                    userLocationsArr=mService.getUserLocationsArr();
                    Polyline polyline=googleMap.addPolyline(new PolylineOptions().addAll(userLocationsArr));
                    polyline.setColor(Color.RED);
                }
                handler.postDelayed(this, 100);
            }else {
                //Log.e(TAG,"recordRun_if(mBound)false");
            }
        }
    };
    // ????????? - ???????????? ????????? ?????????
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bindService(){
        Log.e(TAG,"bindService() ??????");
        Intent intent=new Intent(this,RecordService.class);
        startFlagIntent=intent;
        intent.setAction("startForeground");
        if(mConnection==null){
            Log.e(TAG,"mConnection==null, mConnection = new ServiceConnection");
            mConnection=new ServiceConnection(){
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    RecordService.MyBinder binder=(RecordService.MyBinder) iBinder;
                    // ???????????? ?????? ????????? ??????, get ????????????
                    mService=binder.getService();
                    mBound=true;
                    System.out.println("????????????");
                    System.out.println("mBound : "+mBound);
                    Log.e(TAG,"onServiceConnected ???????????? In bindService()");
                    handler.postDelayed(recordRun,0);
                }
                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                }
            };
            bindService(intent,mConnection,BIND_AUTO_CREATE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                Log.e(TAG,"startForegroundService");
                startForegroundService(intent);
            }
        }else {
            Log.e(TAG,"mConnection!=null");
        }
    }

    // ????????? ?????? ?????? ?????????
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*  public Runnable mapRun=new Runnable(){
          @Override
          public void run() {
              mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
              mapFragment.getMapAsync(DisplayStartActivity.this);
              handler.postDelayed(this, 1000);
          }
      };
  */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
        //LatLng latLng=new LatLng(37.4828617,126.9748708);

        // ?????? ?????? ????????? ?????? ?????? ??????, ?????? ??? ???????????? ??????
        // 2.???????????? -> ?????? ????????? - ?????????????????? ???????????? ?????? ??????
        // ????????? ????????? ?????????????????? ????????? ??????????????? ??????
        // 3.(2) ?????? ?????? ?????? ?????? : ??? ??????
        // (3) / ??? ?????? : ?????? ??????.
        Log.e(TAG,"onMapReady");
        getCurrentLocation();

        Log.e(TAG,"latitude In onMapReady: "+latitude);
        Log.e(TAG,"longitude In onMapReady: "+longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));

        //polylineOptions=new PolylineOptions();
   /*     for (int i = 0; i < userLocationsArr.size(); i++){
            polylineOptions.add(new LatLng(userLocationsArr.get(i).latitude,userLocationsArr.get(i).longitude));
        }
        Polyline polyline=googleMap.addPolyline(polylineOptions);
        polyline.setColor(Color.RED)*/;

/*        if(userLocationsArr.size()>2){
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocationsArr.get(userLocationsArr.size()-1)));
        }*/
        //?????? ?????? ??????
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //. ??? ??????/?????? ?????????
        googleMap.getUiSettings().setZoomControlsEnabled(true);



        //?????? ??????????????????
        /*MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("2?????????");
        googleMap.addMarker(markerOptions);*/
        //my location
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED
                &&ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

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
                    Toast.makeText(DisplayStartActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(DisplayStartActivity.this, 2);
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
    }// turn on gps
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
    private void getCurrentLocation() {
        // ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DisplayStartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // gps ?????? ?????? ?????? ??????
                if (isGPSEnabled()) {
                    // LocationServices :?????? ????????? ????????? ?????? ?????? ?????? ???????????????
                    // getFusedLocationProviderClient : Activity ??? ????????? FusedLocationProviderClient ??? ??? ??????????????? ????????????

                    LocationServices.getFusedLocationProviderClient(DisplayStartActivity.this)
                            // LocationCallback() : ????????? ?????? ?????? ????????? ???????????? ?????? ??????????????? ???????????????.
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(DisplayStartActivity.this)
                                            .removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        // ??????

                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        // ??????
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                        Log.e(TAG,"latitude In getCurrent(): "+latitude);
                                        Log.e(TAG,"longitude getCurrent(): "+longitude);

                                        Geocoder geocoder=new Geocoder(DisplayStartActivity.this, Locale.getDefault());
                                        try {
                                            System.out.println("?????? :"+geocoder.getFromLocation(latitude,longitude,1).stream().findFirst().get().getAddressLine(0));
                                            address=geocoder.getFromLocation(latitude,longitude,1).stream().findFirst().get().getAddressLine(0);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                 /*      Log.e(TAG,"latitude In onLocationResult(): "+latitude);
                                         Log.e(TAG,"longitude In onLocationResult(): "+longitude);
*/
                                        if(initLocation){
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16));
                                            initLocation=false;
                                        }

                                        //userLocationsArr.add(new LatLng(latitude,longitude));
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

    private void updateGPS(){
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(DisplayStartActivity.this);

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


    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
        Gson gson = new Gson();

        SharedPreferences sharedPreferencesCurrent=getSharedPreferences("currentUser",MODE_PRIVATE);
        String jsonCurrent=sharedPreferencesCurrent.getString("current",null);
        Type typeCurrent=new TypeToken<User>(){}.getType();
        User dataCurrent=gson.fromJson(jsonCurrent,typeCurrent);

        String json = sharedPreferences.getString(dataCurrent.getId(), null);
        Type type = new TypeToken<ArrayList<RecordItemData>>() {
        }.getType();
        ArrayList<RecordItemData> data = gson.fromJson(json, type);
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

        float currentDistance=0;
        //????????? ??????.
        for (int i = 0; i < mArrayList.size(); i++){
            //?????? ??????
            String[] intTime=mArrayList.get(i).getTime().split(":");
            hour+=Integer.parseInt(intTime[0]);
            minute+=Integer.parseInt(intTime[1]);
            second+=Integer.parseInt(intTime[2]);

            resultSecond=second%60;
            resultMinute=(minute+second/60)%60;
            resultHour=hour+((minute+second /60)/60);

            //???????????? ??? ??????
            //  ?????? ??? ?????? = ????????? ?????? ????????? + ?????? ?????????/60 ( ??? ?????? )

            // ?????? ??? ?????? = ????????? ?????? ??? ?????? + ?????? ?????????/60

            //System.out.println("???????????? : "+resultHour+":"+resultMinute+":"+resultSecond);

            currentDistance= Float.parseFloat(mArrayList.get(i).getDistance());

            total_distance+=currentDistance;
            //Log.e(TAG,"total_distance : "+total_distance);
            if(mArrayList.get(i).getStep()!=null){
                total_step+=Integer.parseInt(mArrayList.get(i).getStep());
            }

            // 60???, 60??? ???????????? +1??????, +1??? ????????? ?????? ???????????? ??????????????? ????????????, ??????
            // ???????????? : 0.000; ??? float ?????? ???????????? ???????????????.
            // ???????????? ????????? ??? ???????????? ????????? ?????????.
        }

        profile_distance= String.valueOf((double)((int)(total_distance*10))/10);
        profile_time=resultHour+":"+resultMinute+":"+resultSecond;
        profile_cnt= String.valueOf(mArrayList.size());
        profile_step= String.valueOf(total_step);

        Log.e(TAG,"profile_distance : "+profile_distance);

    }// loadData

    private void reLoadData(){

    }


    @Override
    protected void onStop() {
        super.onStop();
//        locationManager.removeUpdates(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onRestart() {
        super.onRestart();
        // ????????? ????????? ????????????
        //doStuff();
        getCurrentLocation();

        Log.e(TAG,"latitude In onMapReady: "+latitude);
        Log.e(TAG,"longitude In onMapReady: "+longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
        reLoadData();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doStuff() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            //commented, this is from the old version
            // this.onLocationChanged(null);
        }
        Toast.makeText(this,"Waiting for GPS connection!", Toast.LENGTH_SHORT).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //doStuff();
            } else {

                /*finish();*/
            }

        }
    }


}
