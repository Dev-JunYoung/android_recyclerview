package com.example.androidproject;

import static com.example.androidproject.ProfileActivity.profile_cnt;
import static com.example.androidproject.ProfileActivity.profile_distance;
import static com.example.androidproject.ProfileActivity.profile_step;
import static com.example.androidproject.ProfileActivity.profile_time;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
//AppCompatActivity

public class RecordActivity extends AppCompatActivity implements View.OnClickListener,
        CustomItemTouchHelperCallback.OnItemTouchListener, View.OnLongClickListener {

    ArrayList<RecordItemData> selectionList=new ArrayList<>();
    int counter=0;

    boolean isContexualModeEnable=false;
    Toolbar toolbar;

    //리사이클러뷰 사용 준비
    ArrayList<RecordItemData> mArrayList;
    private RecordAdapter mAdapter;
    private int count;


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
Menu menu2;
    @Override //menu버튼 객체화
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_record,menu);
        getMenuInflater().inflate(R.menu.normal_menu,menu);
    menu2=menu;
        return true;
    }


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
                    for(int i=0;i<selectionList.size();i++){
                        delete(i);
                    }
                    mAdapter.RemoveItem(selectionList);
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
                Sharing_intent.putExtra("distance",selectionList.get(k).getDistance().toString());
                Sharing_intent.putExtra("time",selectionList.get(k).getTime().toString());
                Sharing_intent.putExtra("step",selectionList.get(k).getStep().toString());
                Sharing_intent.putExtra("maxHeight",selectionList.get(k).getMaxHeight().toString());
                Sharing_intent.putExtra("minHeight",selectionList.get(k).getMinHeight().toString());
                Sharing_intent.putExtra("avg",selectionList.get(k).getAvg().toString());
            }



            Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
            startActivity(Sharing);
        }
        return true;
    }
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

            //리사이클러뷰로 리팩토링해야될 네가지. -> 뷰홀더
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
                profile_time=totalTime;
                profile_step=totalStep;
                profile_cnt=totalCnt;
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






        //이부분이 뷰홀더로 묶여져야함. 아이템의 속성으로 들어갈 애들. 리사이클러뷰로 리팩토링
       /* tv_distance=findViewById(R.id.tv_distance);
        tv_time=findViewById(R.id.tv_time);
        tv_height=findViewById(R.id.tv_height);
        tv_step=findViewById(R.id.tv_step);*/

        /*---------------------------------------------------------------------*/
        RecyclerView mRecyclerView=(RecyclerView) findViewById(R.id.recyclerview_record_list);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList=new ArrayList<>();

        mAdapter=new RecordAdapter(mArrayList,RecordActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        //역순정렬.
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        CustomItemTouchHelperCallback customItemTouchHelperCallback = null;


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
                        totalCnt=totalCnt-1;

                        totalTime=totalTime-Integer.parseInt(mArrayList.get(pos).getTime());
                        totalStep=totalStep-Integer.parseInt(mArrayList.get(pos).getStep());


                        profile_distance-= Integer.parseInt(mArrayList.get(pos).getDistance());


                        profile_time=totalTime;
                        profile_step=totalStep;
                        profile_cnt=totalCnt;

                        mArrayList.remove(pos);

                        String cnt= String.valueOf(totalCnt);
                        String step= String.valueOf(totalStep);
                        String time= String.valueOf(totalTime);

                        tv_totalCnt.setText(cnt);
                        tv_totalTime.setText(time);
                        tv_totalStep.setText(step);


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

        tv_totalCnt=findViewById(R.id.tv_totalCnt);
        tv_totalTime=findViewById(R.id.tv_totalTime);
        tv_totalStep=findViewById(R.id.tv_totalStep);

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
    protected void onPause() {

        super.onPause();



        totalTime=profile_time;
        totalStep=profile_step;
        totalCnt=profile_cnt;


        String cnt= String.valueOf(totalCnt);
        String step= String.valueOf(totalStep);
        String time= String.valueOf(totalTime);

        tv_totalCnt.setText(cnt);
        tv_totalTime.setText(time);
        tv_totalStep.setText(step);




        Log.e("record로그퍼즈","record로그퍼즈");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("record로그스탑","record로그스탑");
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Log.e("record로그리스타트","record로그리스타트");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("record로그디스트로이","record로그디스트로이");
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
                position=식별;
                System.out.println(position+"액티비티");
                totalCnt=totalCnt-1;
                totalTime=totalTime-Integer.parseInt(mArrayList.get(position).getTime());
                totalStep=totalStep-Integer.parseInt(mArrayList.get(position).getStep());



                profile_distance-= Integer.parseInt(mArrayList.get(position).getDistance());
                profile_time=totalTime;
                profile_step=totalStep;
                profile_cnt=totalCnt;
                //여기
                mArrayList.remove(position);

                String cnt= String.valueOf(totalCnt);
                String step= String.valueOf(totalStep);
                String time= String.valueOf(totalTime);

                tv_totalCnt.setText(cnt);
                tv_totalTime.setText(time);
                tv_totalStep.setText(step);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override //롱클릭 시 체크박스 활성화
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
//        itemCounter.setText(counter+ " Item Selected");

    }
    void delete(int pos){
        totalCnt=totalCnt-1;
        totalTime=totalTime-Integer.parseInt(mArrayList.get(pos).getTime());
        totalStep=totalStep-Integer.parseInt(mArrayList.get(pos).getStep());
        profile_distance-= Integer.parseInt(mArrayList.get(pos).getDistance());
        profile_time=totalTime;
        profile_step=totalStep;
        profile_cnt=totalCnt;
        //여기
       // mArrayList.remove(pos);

        String cnt= String.valueOf(totalCnt);
        String step= String.valueOf(totalStep);
        String time= String.valueOf(totalTime);

        tv_totalCnt.setText(cnt);
        tv_totalTime.setText(time);
        tv_totalStep.setText(step);
    }

}