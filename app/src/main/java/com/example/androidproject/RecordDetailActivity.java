package com.example.androidproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class RecordDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView tv_distance,tv_time,tv_step,tv_MaxHeight,tv_MinHeight,tv_avg;
    Button btn_back;
    String distance,time,step,maxHeight,minHeight,avg,location;
    private GoogleMap googleMap;
    SupportMapFragment mapFragment;

    final String TAG="RecordDetailActivity";
    ArrayList<UserLocation> userLocations;
    ArrayList<LatLng> userLocationsArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        tv_distance=findViewById(R.id.tv_distance);
        tv_time=findViewById(R.id.tv_time);
        tv_step=findViewById(R.id.tv_step);
        tv_MaxHeight=findViewById(R.id.tv_MaxHeight);
        tv_MinHeight=findViewById(R.id.tv_MinHeight);
        tv_avg=findViewById(R.id.tv_avg);

        btn_back=findViewById(R.id.btn_back);



        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(RecordDetailActivity.this);


        userLocations=new ArrayList<>();
        userLocationsArr=new ArrayList<>();
        Intent intent=getIntent();

        distance=intent.getStringExtra("distance");
        time=intent.getStringExtra("time");
        step=intent.getStringExtra("step");
        maxHeight=intent.getStringExtra("maxHeight");
        minHeight=intent.getStringExtra("minHeight");
        avg=intent.getStringExtra("avg");

        userLocations=(ArrayList<UserLocation>) getIntent().getSerializableExtra("location");




        for (int i = 0; i < userLocations.size(); i++){

            Log.e(TAG,"latitude : "+ userLocations.get(i).latitude);
            Log.e(TAG,"longitude : "+userLocations.get(i).longitude);
            userLocationsArr.add(new LatLng(userLocations.get(i).latitude,userLocations.get(i).longitude));
            Log.e(TAG,"userLocationsArr_latitude : "+ userLocationsArr.get(i).latitude);
            Log.e(TAG,"userLocationsArr_longitude : "+  userLocationsArr.get(i).longitude);

        }

        System.out.println(avg);
        tv_distance.setText(distance+"km");
        tv_time.setText(time);
        tv_step.setText(step);
        tv_MaxHeight.setText(maxHeight+"m");
        tv_MinHeight.setText(minHeight+"m");
        tv_avg.setText(avg+"km");





        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecordDetailActivity.this,RecordActivity.class);
                startActivity(intent);
            }
        });

    }


    public void onBackPressed() {
            Intent intent=new Intent(RecordDetailActivity.this,RecordActivity.class);
            finish();
            startActivity(intent);
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
        PolylineOptions polylineOptions=new PolylineOptions();
        for (int i = 0; i < userLocations.size(); i++){
            polylineOptions.add(new LatLng(userLocations.get(i).latitude,userLocations.get(i).longitude));
        }
        if(userLocationsArr.size()>2){
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocationsArr.get(userLocationsArr.size()-1)));
        }
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        Polyline polyline=googleMap.addPolyline(polylineOptions);
        polyline.setColor(Color.RED);
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        //googleMap.setMyLocationEnabled(true);
  ;



    }
}

