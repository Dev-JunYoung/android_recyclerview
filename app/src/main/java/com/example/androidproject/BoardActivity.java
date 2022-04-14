package com.example.androidproject;

import static com.example.androidproject.ProfileActivity.tempName;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_display_start,btn_record,btn_board,btn_profile,btn_board_all,btn_board_enjoy;
    private ImageButton btn_AddBoard;

    private ArrayList<BoardItemData> mArrayList;
    private BoardAdapter mAdapter;



    static final int REQUEST_CODE=777;
    static final int REQUEST_READ_CODE=666;
    static final int REQUEST_EDIT_CODE=555;
    static final int RESULT_DELETE_CODE=444;

    static int i=0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        btn_display_start=findViewById(R.id.btn_display_start);
        btn_record=findViewById(R.id.btn_record);
        btn_board=findViewById(R.id.btn_board);
        btn_profile=findViewById(R.id.btn_profile);

        btn_board_all=findViewById(R.id.btn_board_all);
        btn_board_enjoy=findViewById(R.id.btn_board_enjoy);
        btn_AddBoard=findViewById(R.id.btn_AddBoard);

        btn_display_start.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_board.setOnClickListener(this);
        btn_profile.setOnClickListener(this);

        btn_AddBoard.setOnClickListener(this);
        btn_board_all.setOnClickListener(this);
        btn_board_enjoy.setOnClickListener(this);

        /*-----------------------------------------------------------*/
        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_board_list);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList=new ArrayList<>();
        mAdapter=new BoardAdapter(mArrayList,this.getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        /*-----------------------------------------------------------*/
        //READ BOARD
        mAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Intent intent=new Intent(BoardActivity.this,BoardReadActivity.class);
                intent.putExtra("title",mArrayList.get(pos).getTitle());
                intent.putExtra("content",mArrayList.get(pos).getContent());


               intent.putExtra("img1",mArrayList.get(pos).getImgList().get(0));
                intent.putExtra("img2",mArrayList.get(pos).getImgList().get(1));
                intent.putExtra("img3",mArrayList.get(pos).getImgList().get(2));



                /*intent.putExtra("img3",mArrayList.get(pos).getImgList().get(2).toString());*/





                i=pos;
                startActivityForResult(intent,REQUEST_READ_CODE);


            }

            @Override
            public void OnDeleteClick(View v, int pos) {

            }
        });
        /*-----------------------------------------------------------*/
    }
    private long backpressedTime = 0;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 로그인페이지로 이동됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(BoardActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_display_start:
                Intent intent=new Intent(BoardActivity.this,DisplayStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_record:
                Intent intent2=new Intent(BoardActivity.this,RecordActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                break;
            case R.id.btn_board:
                Intent intent3=new Intent(BoardActivity.this,BoardActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
                break;
            case R.id.btn_profile:
                Intent intent4=new Intent(BoardActivity.this,ProfileActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent4);
                break;
            case R.id.btn_AddBoard:
                Intent intent5=new Intent(BoardActivity.this,CreateBoardActivity.class);
                startActivityForResult(intent5,REQUEST_CODE);
                break;
            case R.id.btn_board_all:
                Intent intent6=new Intent(BoardActivity.this,BoardActivity.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent6);
                break;
            case R.id.btn_board_enjoy:
                Intent intent7=new Intent(BoardActivity.this,BoardEnjoyActivity.class);
                intent7.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent7);
                break;
        }
    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("리퀘스트코드"+requestCode);
        System.out.println("리절트코드"+resultCode);
        if(resultCode== RESULT_OK){
            Toast.makeText(getApplicationContext(), "수신 성공", Toast.LENGTH_SHORT).show();
            if(requestCode==REQUEST_CODE){

                TimeZone tz;
                Date date=new Date();
                DateFormat df=new SimpleDateFormat("yy-MM-dd HH:mm");
                tz=TimeZone.getTimeZone("Asia/Seoul");
                df.setTimeZone(tz);
                String timeStamp = df.format(date);



                String title=data.getStringExtra("title");
                String content=data.getStringExtra("content");

                String img=data.getStringExtra("img");
                String img2=data.getStringExtra("img2");
                String img3=data.getStringExtra("img3");

                System.out.println("1 : " +img);
                System.out.println("2 : " +img2);
                System.out.println("3 : " +img3);

                //이미지 리스트 저장.
                ArrayList<String> imgList=new ArrayList<>();
                imgList.add(img);
                imgList.add(img2);
                imgList.add(img3);

                BoardItemData itemData=new BoardItemData(
                        title,content,imgList,tempName,timeStamp);

                mArrayList.add(itemData);
                mAdapter.notifyDataSetChanged();
            }
        }else {
            Toast.makeText(getApplicationContext(), "수신 실패", Toast.LENGTH_SHORT).show();
        }

        if(resultCode== RESULT_OK){
        if(requestCode==REQUEST_READ_CODE){
        //플래그값에 따라 읽고만 오는지 수정내용을 커밋해야되는지 조건문을 걸어서 확인.
            try {
                if(data.getStringExtra("flag")!=null){
                    String str=data.getStringExtra("flag");
                    if(str==str){
                        System.out.println("BoardActivity.requestCode == REQUEST_READ_CODE");

                        TimeZone tz;
                        Date date=new Date();
                        DateFormat df=new SimpleDateFormat("yy-MM-dd HH:mm");
                        tz=TimeZone.getTimeZone("Asia/Seoul");
                        df.setTimeZone(tz);
                        String timeStamp = df.format(date);


                        String title=data.getStringExtra("title");
                        String content=data.getStringExtra("content");

                       // String img=data.getStringExtra("img");
                        //BoardItemData itemData=new BoardItemData(
                        //        title,content,img,tempName,timeStamp);
                        ArrayList<String> imgList=new ArrayList<>();

                        String img=data.getStringExtra("img1");
                        String img2=data.getStringExtra("img2");
                        String img3=data.getStringExtra("img3");

                        System.out.println(img);
                        System.out.println(img2);
                        System.out.println(img3);
                        imgList.add(img);
                        imgList.add(img2);
                        imgList.add(img3);



                        BoardItemData itemData=new BoardItemData(
                               title,content,imgList,tempName,timeStamp);


                        mArrayList.set(i,itemData);
                        i=0;

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }catch (NullPointerException e){
                System.out.println("BoardActivity onActivityResult() null Exception");
            }

           }
        }
        if(resultCode==RESULT_DELETE_CODE){
            mArrayList.remove(i);
            i=0;
            mAdapter.notifyDataSetChanged();

        }

    }
}