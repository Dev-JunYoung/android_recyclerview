package com.example.androidproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

//FusedLocationProviderApi로부터 알림을 수신하는 데 사용됩니다.

public class DisplayStartActivity extends AppCompatActivity implements View.OnClickListener {



    private Button btn_display_start, btn_record, btn_board, btn_profile, btn_start;

    private long backpressedTime = 0;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 로그인페이지로 이동됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(DisplayStartActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override //menu버튼 객체화
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override //menu 버튼 클릭 choose
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                Toast.makeText(this, "지도보기", Toast.LENGTH_SHORT).show();
                mapView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapView() {
        Uri location = Uri.parse("geo:0,0?q=서울특별시청");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        String title = "앱을 선택하시오";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //System.out.println(User.getInstance().getId());
        //위치 업데이트 콜백 정의
        //콜백 객체 생성
        /*locationCallback = new LocationCallback() {
            @Override// 장치 위치 정보를 사용할 수 있는지 확인
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    //없으면 Return
                    return;
                }else { //있으면 get정보
                    for (Location location : locationResult.getLocations()) {
                        // Update UI with location data
                        // 위도,경도 밒 타임스탬프를 표시
                    }
                }

            }
        };*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_start);


        btn_display_start = findViewById(R.id.btn_display_start);
        btn_record = findViewById(R.id.btn_record);
        btn_board = findViewById(R.id.btn_board);
        btn_profile = findViewById(R.id.btn_profile);
        btn_start = findViewById(R.id.btn_start);

        btn_display_start.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_board.setOnClickListener(this);
        btn_profile.setOnClickListener(this);

        btn_start.setOnClickListener(this);

    }

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
            case R.id.btn_start:
                Intent intent7 = new Intent(DisplayStartActivity.this, StartActivity.class);
                startActivity(intent7);
                break;
        }
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            //위치 update 요청 메서드
            startLocationUpdates();
        }
    }
    private void startLocationUpdates() {
        //권환 확인
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED && 
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) 
                        != PackageManager.PERMISSION_GRANTED) {
            //지정된 Looper 스레드에 대한 콜백을 사용하여 위치 업데이트를 요청합니다
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper());
            return;
        }
    }

    //locationRequest : 위치 업데이트를 위한 서비스 품질을 요청
    //locationCallback: FusedLocationProviderApi로부터 알림을 수신하는 데 사용됩니다
    //Looper.getMainLooper(): 기본 스레드에 기본 루터를 반환.
    //Looper: 스레드에 대한 메시지 루프를 실행하는 데 사용되는 클래스입니다

    @Override
    protected void onPause() {
        super.onPause();
        //위치 업데이트 중지
        stopLocationUpdates();
    }
    private void stopLocationUpdates() {
        //지정된 위치 결과 수신기에 대한 모든 위치 업데이트를 제거합니다.
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //데이터 임시저장
    }*/
}