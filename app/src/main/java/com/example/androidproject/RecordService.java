package com.example.androidproject;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class RecordService  extends Service implements SensorEventListener, LocationListener {

    final String TAG="RecordService";
    Thread textTh;
    Thread timeTh;
    // 걸음 수, 시간, 고도
    // 시간
    int Seconds, Minutes, hour, totalSeconds;
    int numHeight, numMinHeight, numMaxHeight;
    double speedAvg;
    double distance;
    // 현재 걸음수 측정.
    int currentSteps;
    int moveDistance;

    double latitude;
    double longitude;

    boolean flag=true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    RemoteViews remoteViews;
    NotificationCompat notificationCompat;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;


    private IBinder mBinder = new MyBinder();
    // sensor
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private Sensor stepDetectorSensor;
    private SensorEventListener listener;

    // Activity 에서 가져온 Messenger
    public static final int MSG_REGISTER_CLIENT = 1;
    private Messenger mClient = null;
    ArrayList<LatLng> userLocationsArr=new ArrayList<>();
    // 위치 요청
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private LocationListener locationListener;

    // 액티비티로 보낼 데이터 변수
    String sendTime;
    String sendSpeed;
    String sendStep;
    String sendAltitude;
    String sendMinHeight;
    String sendMaxHeight;
    String sendHeight;
    String sendSpeedAvg;
    String sendCurrent;
    String sendDistance;

    TextView tv_distance,tv_time,tv_step;


    boolean serviceLive = true;
    int oldTime;

    public class MyBinder extends Binder {
        public RecordService getService() {

            return RecordService.this;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"onBind");
        // 콜백 메서드
        // 백그라운드에서 실행 되어야 할 코드 작성
        // 1. 시간
        //
        // 2. 스텝 센서
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }
        if (stepCountSensor != null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
            Toast.makeText(this, "Ready Step Sensor", Toast.LENGTH_SHORT).show();
        }

        // Location
        locationRequest = LocationRequest.create();
        // PRIORITY_HIGH_ACCURACY -> 고정밀 위치가 필요한 경우
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //기존
        // locationRequest.setInterval(5000);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }
        Toast.makeText(this,"Waiting for GPS connection!", Toast.LENGTH_SHORT).show();
        /*-------------------------------------------------------------------------------------------------------------*/
        // 이동거리
        double distance= SphericalUtil.computeLength(userLocationsArr);
        sendDistance= String.valueOf((int)distance/1000);

        //시간 스레드 시작

        // 시간스레드

        timeTh=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"timeTh.run");
                while(isServiceLive()){
                    while(flag){
                    Seconds=totalSeconds;
                    Minutes = Seconds / 60;
                    hour = Minutes / 60;
                    Seconds = Seconds % 60;
                /*sendTime=hour + ":" + Minutes + ":"
                        + String.format("%02d", Seconds);*/
                    sendTime=String.format("%02d", hour) + ":" + String.format("%02d", Minutes) + ":"
                            + String.format("%02d", Seconds);
                    //remoteViews.setTextViewText(R.id.tv_remote_time,sendTime);
                    try {
                        Thread.sleep(1000);
                        totalSeconds++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG ,"In Service currentSpeed : "+sendSpeed+"");
                    Log.e(TAG,"In Service totalSeconds : " +totalSeconds+"");
                    Log.e(TAG ,"In Service distance : "+distance+"");
                    Log.e(TAG,"결과 --------------------------------------------------------------");
                    Log.e(TAG ,"In Service speedAvg"+speedAvg+"");
                }
                }
            }
        });
        timeTh.start();



        return mBinder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"In onStartCommand");
        if(intent!=null){
            if(intent.getAction()!=null){
                if("startForeground".equals(intent.getAction())){
                    Log.e(TAG,"In onStartCommand_\"startForeground\".equals(intent.getAction())");
                    startForegroundService();
                }
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }
    void startForegroundService(){

        Log.e(TAG,"In startForegroundService()");
//        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.appicon);
        remoteViews = new RemoteViews(getPackageName(),
                R.layout.custom_notification);
        builder=new NotificationCompat
                .Builder(this,"default")
                .setContent(remoteViews)
                .setSmallIcon(R.drawable.appicon)
                .setAutoCancel(true);


        Intent notificationIntent=new Intent(this,DisplayStartActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        builder.setContentIntent(pendingIntent);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_LOW));
        }


        textTh=new Thread(new Runnable() {
            @Override
            public void run() {
                while(isServiceLive()){
                    Log.e(TAG,"textTh.run");
                    remoteViews.setTextViewText(R.id.tv_notify_distance,sendDistance);
                    remoteViews.setTextViewText(R.id.tv_notify_time,sendTime);
                    remoteViews.setTextViewText(R.id.tv_notify_step,sendStep);
                    notificationManager.notify(1,builder.build());
                    Log.e(TAG,"remoteViews.sendDistance : "+sendDistance);
                    Log.e(TAG,"remoteViews.sendTime : "+sendTime);
                    Log.e(TAG,"remoteViews.sendStep : "+sendStep);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
       /* if(textTh.getState()== Thread.State.TERMINATED){

        }*/
        textTh.start();
        startForeground(1,builder.build());
    }
    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        Log.e(TAG,"In startForegroundService()");
        return super.startForegroundService(service);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // 센서 리스너
        if(isServiceLive()){
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                if (sensorEvent.values[0] == 1.0f) {
                    // 센서 이벤트가 발생할때 마다 걸음수 증가
                    currentSteps++;
                    moveDistance++;
                    sendStep= String.valueOf(currentSteps);
                }
            }

        }else {
            //Log.e(TAG,"onSensorChanged isServiceLive()==false");
        }
        Log.e(TAG,"onSensorChanged");
        /*
         * onLocationChanged
         * onSensorChanged
         * */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationListener=this.locationListener;

        // Location 리스너
        if(isServiceLive()){
            if (location == null) {
            } else {
                //float nCurrentSpeed = location.getSpeed() * 3.6f;
                latitude=location.getLatitude();
                longitude=location.getLongitude();

                double nCurrentSpeed=Math.ceil(location.getSpeed()*100*3.6)/100;
                Log.d(TAG, "nCurrentSpeed : "+nCurrentSpeed);
                userLocationsArr.add(new LatLng(location.getLatitude(),location.getLongitude()));
                // 이동거리
                distance=(int) SphericalUtil.computeLength(userLocationsArr);
                // 고도
                double longAltitude=location.getAltitude();
                if (numHeight == 0) {
                    numMinHeight = (int) Math.floor(longAltitude);
                    numMaxHeight = (int) Math.floor(longAltitude);
                }
                numHeight = (int) Math.floor(longAltitude);
                // 저장되어 있던 최저 고도보다 낮은 값이 들어오면,
                if (numMinHeight > longAltitude) {
                    //해당 값 저장
                    numMinHeight = (int) Math.floor(longAltitude);
                }
                // 저장되어 있던 최고 고도보다 높은 값이 들어오면,
                if (numMaxHeight < longAltitude) {
                    numMaxHeight = (int) Math.floor(longAltitude);
                }

                sendDistance= String.valueOf(distance/1000);
                sendAltitude= String.valueOf((int)location.getAltitude());
                sendMaxHeight= String.valueOf(numMaxHeight);
                sendMinHeight= String.valueOf(numMinHeight);
                // 고도차
                sendHeight= String.valueOf(numMaxHeight-numMinHeight);
                // 평균 속도
                speedAvg=(Math.ceil((distance/totalSeconds)*100))/100*3.6;

                sendSpeedAvg= String.valueOf(speedAvg);
                //sendCurrent=String.valueOf( (Math.ceil(nCurrentSpeed*10000))/10000);
                sendCurrent=String.valueOf(nCurrentSpeed);




            } //location == null
            //Log.e(TAG,"isServiceLive()==true // LocationListener");
            //확인 완료

        }else {
            //Log.e(TAG,"isServiceLive()==false // LocationListener");
            //확인 완료
        }
        Log.e(TAG,"onLocationChanged");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundService(int i, Notification build) {
    }



    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"onUnbind");
        // 위치, 센서 리스너 해제

        timeTh.interrupt();
        stopForeground(true);
        onDestroy();
        return super.onUnbind(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override // 값 들 초기화
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
        sensorManager.unregisterListener(this.listener);
        locationManager.removeUpdates(this);


        // 서비스 종료시 스레드 종료
        timeTh.interrupt();
        textTh.interrupt();
        serviceLive=false;
        stopSelf();

   /*     sendTime="00:00:00";
        sendSpeed="0.0";
        sendStep="0";
        sendAltitude="0";
        sendMinHeight="0";
        sendMaxHeight="0";
        sendHeight="0";
        sendSpeedAvg="0";
        sendCurrent="0";
        sendDistance="0";*/
        Seconds=0;
        Minutes=0;
        hour=0;
        totalSeconds=0;
        Seconds=0;
        Minutes=0;
        hour=0;
        totalSeconds=0;
        numHeight=0;
        numMinHeight=0;
        numMaxHeight=0;
        speedAvg=0;
        distance=0;
        currentSteps=0;
        moveDistance=0;



    }

    public boolean isServiceLive() {
        return serviceLive;
    }

    public void setServiceLive(boolean serviceLive) {
        this.serviceLive = serviceLive;
        if(isServiceLive()){
        }else {
            timeTh.interrupt();
            System.out.println("timeTh.getState() : "+timeTh.getState());
        }

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    public int getSeconds() {
        return Seconds;
    }

    public int getMinutes() {
        return Minutes;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getSendSpeed() {
        return sendSpeed;
    }

    public String getSendStep() {
        return sendStep;
    }

    public String getSendAltitude() {
        return sendAltitude;
    }

    public String getSendMinHeight() {
        return sendMinHeight;
    }

    public String getSendMaxHeight() {
        return sendMaxHeight;
    }

    public String getSendDistance() {
        return sendDistance;
    }

    public String getSendHeight() {
        return sendHeight;
    }

    public String getSendSpeedAvg() {
        return sendSpeedAvg;
    }


    public Thread getTextTh() {
        return textTh;
    }

    public void setTextTh(Thread textTh) {
        this.textTh = textTh;
    }

    public Thread getTimeTh() {
        return timeTh;
    }

    public void setTimeTh(Thread timeTh) {
        this.timeTh = timeTh;
    }

    public String getSendCurrent() {
        return sendCurrent;
    }

    public ArrayList<LatLng> getUserLocationsArr() {
        return userLocationsArr;
    }

    public void setUserLocationsArr(ArrayList<LatLng> userLocationsArr) {
        this.userLocationsArr = userLocationsArr;
    }
}
