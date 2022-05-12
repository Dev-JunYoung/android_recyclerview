package com.example.androidproject;
import static com.example.androidproject.DisplayStartActivity.profile_cnt;
import static com.example.androidproject.DisplayStartActivity.profile_distance;
import static com.example.androidproject.DisplayStartActivity.profile_step;
import static com.example.androidproject.DisplayStartActivity.profile_time;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
public class RecordActivity extends AppCompatActivity implements View.OnClickListener,
        CustomItemTouchHelperCallback.OnItemTouchListener, View.OnLongClickListener {

    public final String TAG="RecordActivity";
    ArrayList<RecordItemData> selectionList=new ArrayList<>();
    int counter=0;

    boolean isContexualModeEnable=false;
    Toolbar toolbar;

    double total_distance;
    int total_step;
    int hour;
    int minute;
    int second;
    int resultSecond;
    int resultMinute;
    int resultHour;
    //리사이클러뷰 사용 준비
    ArrayList<RecordItemData> mArrayList;
    private RecordAdapter mAdapter;

    ItemTouchHelper itemTouchHelper;

    private Button btn_display_start,btn_record,btn_board,btn_profile;
    private static final int REQUEST_CODE=777;
    private TextView tv_totalCnt,tv_totalTime,tv_totalStep;
    static int totalStep=0;
    static int totalTime=0;
    static int totalCnt=0;

    String detailDistance;
    String detailTime;
    String detailMaxHeight;
    String detailMinHeight;
    String detailStep;
    String detailAvg;
    private long backpressedTime = 0;

    Menu menu2;
    @Override //menu버튼 객체화
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_record,menu);
        getMenuInflater().inflate(R.menu.normal_menu,menu);
    menu2=menu;
        return true;
    }

    // 다중삭제
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_addRecordd:
                Intent intent=new Intent(getApplicationContext(),RecordResisterActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
        if(item.getItemId()==R.id.action_delete){
            AlertDialog.Builder ad = new AlertDialog.Builder(RecordActivity.this);
            ad.setIcon(R.mipmap.ic_launcher);
            ad.setTitle("DELETE");
            ad.setMessage("삭제하시겠습니까");
            ad.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(DialogInterface dialogInterface, int k) {
                /*    for(int i=0;i<selectionList.size();i++){
                        delete(i);
                    }*/

                    Log.e(TAG, "onOptionsItemSelected:onClick ");
                    //리사이클러뷰 아이템 삭제
                    mAdapter.RemoveItem(selectionList);
                    for(int i=0;i<mArrayList.size();i++){
                        Log.e(TAG, mArrayList.get(i).getAvg());
                        Log.e(TAG, mArrayList.get(i).getDistance());
                        Log.e(TAG, mArrayList.get(i).getStep());
                        Log.e(TAG, mArrayList.size()+"");
                        System.out.println("getAvg : "+mArrayList.get(i).getAvg());
                        System.out.println("getDistance : "+mArrayList.get(i).getDistance());
                        System.out.println("getStep : "+mArrayList.get(i).getStep());
                        System.out.println("count : "+mArrayList.size());
                    }

                    sharedDelete();
                    saveData(mArrayList);
                    reLoadData();

                    RemoveContextualActionMode(menu2);

                }
            });


            ad.show();

        }else if(item.getItemId()==android.R.id.home){
            RemoveContextualActionMode(menu2);
            mAdapter.notifyDataSetChanged();
        }else if(item.getItemId()==R.id.share){
            Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
            Sharing_intent.setType("text/plain");
            //공유

            for (int k=0; k<selectionList.size();k++){
                Sharing_intent.putExtra("distance",selectionList.get(k).getDistance());
                Sharing_intent.putExtra("time",selectionList.get(k).getTime());
                Sharing_intent.putExtra("step",selectionList.get(k).getStep());
                Sharing_intent.putExtra("maxHeight",selectionList.get(k).getMaxHeight());
                Sharing_intent.putExtra("minHeight",selectionList.get(k).getMinHeight());
                Sharing_intent.putExtra("avg",selectionList.get(k).getAvg());
            }



            Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
            startActivity(Sharing);
        }
        return true;
    }
    // 다중 삭제 후 원래대로 돌아가는 메서드
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void RemoveContextualActionMode(Menu menu) {
        isContexualModeEnable=false;
        toolbar.getMenu().clear();

        toolbar.inflateMenu(R.menu.normal_menu);

        getMenuInflater().inflate(R.menu.add_record,menu);

        toolbar.setBackgroundColor(getColor(R.color.main));
        counter=0;
        selectionList.clear();
        mAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            Toast.makeText(getApplicationContext(), "수신 성공", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "수신 실패", Toast.LENGTH_SHORT).show();
        }

        if(requestCode==REQUEST_CODE&&resultCode== RESULT_OK){

            String resultDistance=data.getStringExtra("distance");
            String resultTime=data.getStringExtra("time");
            String resultHeight=data.getStringExtra("height");
            String resultStep=data.getStringExtra("step");

            String resultAvg=data.getStringExtra("avg");

            String resultMaxHeight=data.getStringExtra("MaxHeight");
            String resultMinHeight=data.getStringExtra("MinHeight");

            TimeZone tz;
            Date date=new Date();
            DateFormat df=new SimpleDateFormat("yy-MM-dd HH시");
            tz=TimeZone.getTimeZone("Asia/Seoul");
            df.setTimeZone(tz);
            String timeStamp = df.format(date);
            RecordItemData itemData=new RecordItemData(
                    resultDistance,resultTime,resultStep,resultMaxHeight,resultMinHeight,resultAvg,resultHeight,timeStamp);

            mArrayList.add(itemData);

            mAdapter.notifyDataSetChanged();

            detailDistance=resultDistance;
            detailTime=resultTime;
            detailMaxHeight=resultMaxHeight;
            detailMinHeight=resultMinHeight;
            detailStep=resultStep;
            detailAvg=resultAvg;

            try {
                totalCnt  +=1;
                totalStep  = totalStep+Integer.parseInt(resultStep);
                totalTime  = totalTime+Integer.parseInt(resultTime);
                String cnt= String.valueOf(totalCnt);
                String step= String.valueOf(totalStep);
                String time= String.valueOf(totalTime);

                tv_totalCnt.setText(cnt);
                tv_totalTime.setText(time);
                tv_totalStep.setText(step);
                profile_distance+= Integer.parseInt(detailDistance);

                /*
                profile_time=totalTime;
                profile_step=totalStep;
                profile_cnt=totalCnt;*/
            }catch (NumberFormatException e){
                System.out.println("NumberFormatException In RecordActivity");
            }




        }else {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My App");


        btn_display_start=findViewById(R.id.btn_display_start);
        btn_record=findViewById(R.id.btn_record);
        btn_board=findViewById(R.id.btn_board);
        btn_profile=findViewById(R.id.btn_profile);
        //btn_detail=findViewById(R.id.btn_detail);
        tv_totalCnt=findViewById(R.id.tv_totalCnt);
        tv_totalTime=findViewById(R.id.tv_totalTime);
        tv_totalStep=findViewById(R.id.tv_totalStep);

        /*---------------------------------------------------------------------*/
        RecyclerView mRecyclerView=(RecyclerView) findViewById(R.id.recyclerview_record_list);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList=new ArrayList<>();

        for (int i = 0; i < mArrayList.size(); i++){
            System.out.println(mArrayList.get(i).getDistance());
        }
        if(mArrayList.size()==0){
            System.out.println("mArrayList.size() : 0");
        }

        mAdapter=new RecordAdapter(mArrayList,RecordActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        loadData();



        //역순정렬.
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        CustomItemTouchHelperCallback customItemTouchHelperCallback = null;
        System.out.println("size : "+mArrayList.size());
        //if(mArrayList.size()!=0){loadData();}


        //커스텀 리스너 객체생성 및 전달
        mAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Intent intent=new Intent(RecordActivity.this,RecordDetailActivity.class);
                intent.putExtra("distance",mArrayList.get(pos).getDistance().toString());
                intent.putExtra("time",mArrayList.get(pos).getTime().toString());
                intent.putExtra("step",mArrayList.get(pos).getStep().toString());
                intent.putExtra("maxHeight",mArrayList.get(pos).getMaxHeight().toString());
                intent.putExtra("minHeight",mArrayList.get(pos).getMinHeight().toString());
                intent.putExtra("avg",mArrayList.get(pos).getAvg().toString());

                Log.e(TAG,"OnItemClick_location : "+mArrayList.get(pos).getUserLocation());
                ArrayList<UserLocation> locationList=new ArrayList();
                for (int i = 0; i < mArrayList.get(pos).getUserLocation().size(); i++){
                    UserLocation userLocation=new UserLocation(mArrayList.get(pos).getUserLocation().get(i).latitude,mArrayList.get(pos).getUserLocation().get(i).longitude);
                    locationList.add(userLocation);
                }
                intent.putExtra("location",locationList);




                startActivity(intent);

                //startActivityForResult(intent,REQUEST_READ_CODE);

            }

            @Override
            public void OnDeleteClick(View v, int pos) {
                AlertDialog.Builder ad = new AlertDialog.Builder(RecordActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("DELETE");
                ad.setMessage("삭제하시겠습니까");
                ad.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG,"OnDeleteClick");
                        totalCnt=totalCnt-1;
                        totalTime=totalTime-Integer.parseInt(mArrayList.get(pos).getTime());
                        totalStep=totalStep-Integer.parseInt(mArrayList.get(pos).getStep());
                       /* profile_distance-= Integer.parseInt(mArrayList.get(pos).getDistance());*/


                        /*profile_time=totalTime;
                        profile_step=totalStep;
                        profile_cnt=totalCnt;*/

                        mArrayList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ad.show();
            }
        });
        /*---------------------------------------------------------------------*/

        btn_display_start.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_board.setOnClickListener(this);
        btn_profile.setOnClickListener(this);

//        btn_detail.setOnClickListener(this);



        //ItemTouchHelper 객체생성(동작해야 될 설정 객체)
        itemTouchHelper=new ItemTouchHelper(new CustomItemTouchHelperCallback(mAdapter));
        //생성된 ItemTouchHelper 객체를 리사이클러뷰에 붙힌다.
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }//onCreate
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_display_start:
                Intent intent=new Intent(RecordActivity.this,DisplayStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_record:
                Intent intent2=new Intent(RecordActivity.this,RecordActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                break;
            case R.id.btn_board:
                Intent intent3=new Intent(RecordActivity.this,BoardActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
                break;
            case R.id.btn_profile:
                Intent intent4=new Intent(RecordActivity.this,ProfileActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent4);
                break;
            case R.id.recyclerview_record_list:

                break;
        }
    }

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public boolean moveItem(int fromPosition, int toPosition) {
        RecordItemData data=mArrayList.get(fromPosition);
        mArrayList.remove(fromPosition);
        mArrayList.add(toPosition,data);
        mAdapter.notifyItemMoved(fromPosition,toPosition);
        return false;
    }

static int 식별;
    // 스와이프 삭제
    @Override
    public void removeItem(int position) {
        position=식별;
        System.out.println(식별);
        AlertDialog.Builder ad = new AlertDialog.Builder(RecordActivity.this);
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("DELETE");
        ad.setMessage("삭제하시겠습니까");
        ad.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                // 스와이프 삭제
                Log.e(TAG,"removeItem-onClick");
                position=식별;

                //totalCnt=totalCnt-1;

                //여기
                mArrayList.remove(position);


                mAdapter.notifyDataSetChanged();
                sharedDelete();
                saveData(mArrayList);
                reLoadData();
                dialogInterface.dismiss();
            }
        });

        ad.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }
    //롱클릭 시 체크박스 활성화, for 다중선택 메서드
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onLongClick(View view) {

        isContexualModeEnable=true;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.contexual_menu);

        getSupportActionBar().setTitle("0 item");
        toolbar.setBackgroundColor(getColor(R.color.choose));

        mAdapter.notifyDataSetChanged();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    // 아이템 다중 선택
    public void MakeSelection(View view, int absoluteAdapterPosition) {
        if(((CheckBox)view).isChecked()){
            System.out.println(absoluteAdapterPosition);
            try {
                selectionList.add(mArrayList.get(absoluteAdapterPosition));
            }catch (NullPointerException e){
            }
            counter++;
            UpdateCounter();
        }else {
            selectionList.remove(mArrayList.get(absoluteAdapterPosition));
            counter--;
            UpdateCounter();
        }
    }

    private void UpdateCounter() {
    }
    void delete(int pos){
        // 총 시간
        mArrayList.remove(pos);

    }
    // 전체 삭제.
    void sharedDelete(){
        Log.e(TAG,"sharedDelete");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);

        SharedPreferences sharedPreferencesCurrent=getSharedPreferences("currentUser",MODE_PRIVATE);
        String jsonCurrent=sharedPreferencesCurrent.getString("current",null);
        Type typeCurrent=new TypeToken<User>(){}.getType();
        User dataCurrent=gson.fromJson(jsonCurrent,typeCurrent);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Log.e(TAG,"sharedDelete, "+dataCurrent.getId());
        editor.remove(dataCurrent.getId());
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences sharedPreferencesCurrent=getSharedPreferences("currentUser",MODE_PRIVATE);
        String jsonCurrent=sharedPreferencesCurrent.getString("current",null);
        Type typeCurrent=new TypeToken<User>(){}.getType();
        User dataCurrent=gson.fromJson(jsonCurrent,typeCurrent);
        String json = sharedPreferences.getString(dataCurrent.getId(), null);

        //String json = sharedPreferences.getString(user.getId(), null);
        Type type = new TypeToken<ArrayList<RecordItemData>>() {}.getType();
        ArrayList<RecordItemData> data= gson.fromJson(json, type);
        System.out.println("data : "+data);
        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
        File file=new File(
                "/data/data/com.example.androidproject/shared_prefs/record.xml"
        );
        if(file.exists()){
            if(data!=null){
                for (int i=0;i<data.size();i++){
                    if(data.get(i)!=null){
                        mArrayList.add(data.get(i));
                    }
                }
            }
        }else {
            if(data!=null){
                for (int i=0;i<data.size();i++){
                    if(data.get(i)!=null){
                        mArrayList.add(data.get(i));
                    }
                }
            }
        }
        for (int i = 0; i < mArrayList.size(); i++){
            System.out.println(mArrayList.get(i).getDistance());
            //Log.e(TAG,"getUserLocation : "+mArrayList.get(i).getUserLocation());
        }

        float currentDistance=0;
        //데이터 총합.
        for (int i = 0; i < mArrayList.size(); i++){
            //거리 파싱
            String[] intTime=mArrayList.get(i).getTime().split(":");
            hour+=Integer.parseInt(intTime[0]);
            minute+=Integer.parseInt(intTime[1]);
            second+=Integer.parseInt(intTime[2]);

            resultSecond=second%60;
            resultMinute=(minute+second/60)%60;
            resultHour=hour+((minute+second /60)/60);

            //누적되는 거 확인
            //  전체 분 시간 = 리스트 전체 분시간 + 전체 초시간/60 ( 분 시간 )

            // 전체 시 시간 = 리스트 전체 시 시간 + 전체 분시간/60

            System.out.println("누적시간 : "+resultHour+":"+resultMinute+":"+resultSecond);


                currentDistance= Float.parseFloat(mArrayList.get(i).getDistance());
                total_distance+=currentDistance;





            if(mArrayList.get(i).getStep()!=null){
                total_step+=Integer.parseInt(mArrayList.get(i).getStep());
            }

            // 60분, 60초 넘어가면 +1시간, +1분 해주는 것만 작성해서 전체데이터 계산하고, 저장
            // 킬로미터 : 0.000; 는 float 으로 파싱해서 계산해보기.
            // 평균속도 소수점 뒤 두자리만 나오게 만들기.
        }

        profile_distance= String.valueOf((double)((int)(total_distance*10))/10);
        profile_time=resultHour+":"+resultMinute+":"+resultSecond;
        profile_cnt= String.valueOf(mArrayList.size());
        profile_step= String.valueOf(total_step);

        tv_totalTime.setText(profile_time);
        tv_totalStep.setText(profile_step);
        tv_totalCnt.setText(profile_cnt);

    }

    private void saveData(ArrayList<RecordItemData> list) {
        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);


        SharedPreferences sharedPreferencesCurrent=getSharedPreferences("currentUser",MODE_PRIVATE);
        String jsonCurrent=sharedPreferencesCurrent.getString("current",null);
        Type typeCurrent=new TypeToken<User>(){}.getType();
        User dataCurrent=gson.fromJson(jsonCurrent,typeCurrent);

        editor.putString(dataCurrent.getId(), json);
        editor.commit();
    }
    private void reLoadData() {

        hour =0;
        minute =0;
        second =0;
        total_step=0;

        SharedPreferences sharedPreferences = getSharedPreferences("record", MODE_PRIVATE);
        Gson gson = new Gson();

        SharedPreferences sharedPreferencesCurrent = getSharedPreferences("currentUser", MODE_PRIVATE);
        String jsonCurrent = sharedPreferencesCurrent.getString("current", null);
        Type typeCurrent = new TypeToken<User>() {
        }.getType();
        User dataCurrent = gson.fromJson(jsonCurrent, typeCurrent);


        String json = sharedPreferences.getString(dataCurrent.getId() + "", null);

        Type type = new TypeToken<ArrayList<RecordItemData>>() {
        }.getType();
        ArrayList<RecordItemData> data = gson.fromJson(json, type);
        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
        mArrayList.clear();

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                mArrayList.add(data.get(i));
            }
        }

        float currentDistance = 0;
        //데이터 총합.
        for (int i = 0; i < mArrayList.size(); i++) {
            //거리 파싱
            String[] intTime = mArrayList.get(i).getTime().split(":");
            hour += Integer.parseInt(intTime[0]);
            minute += Integer.parseInt(intTime[1]);
            second += Integer.parseInt(intTime[2]);

            resultSecond = second % 60;
            resultMinute = (minute + second / 60) % 60;
            resultHour = hour + ((minute + second / 60) / 60);

            //누적되는 거 확인
            //  전체 분 시간 = 리스트 전체 분시간 + 전체 초시간/60 ( 분 시간 )

            // 전체 시 시간 = 리스트 전체 시 시간 + 전체 분시간/60

            System.out.println("누적시간 : " + resultHour + ":" + resultMinute + ":" + resultSecond);


            currentDistance = Float.parseFloat(mArrayList.get(i).getDistance());

            total_distance += currentDistance;

            if (mArrayList.get(i).getStep() != null) {
                total_step += Integer.parseInt(mArrayList.get(i).getStep());
            }


        }
        profile_distance= String.valueOf((double)((int)(total_distance*10))/10);
        profile_time=resultHour+":"+resultMinute+":"+resultSecond;
        profile_cnt= String.valueOf(mArrayList.size());
        profile_step= String.valueOf(total_step);



        tv_totalTime.setText(profile_time);
        tv_totalStep.setText(profile_step);
        tv_totalCnt.setText(profile_cnt);
    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.e(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop");

        System.out.println("mArrayList : "+ mArrayList);
        saveData(mArrayList);
        finish();
    }

    @Override
    protected void onRestart() {
        mArrayList.clear();
        loadData();
        super.onRestart();
        Log.e(TAG,"onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");

    }

    @Override
    protected void onResume() {

        Log.e(TAG,"onResume()");
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 로그인페이지로 이동됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(RecordActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
