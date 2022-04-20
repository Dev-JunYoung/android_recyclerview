package com.example.androidproject;

import static com.example.androidproject.BoardActivity.REQUEST_READ_CODE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class BoardEnjoyActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_display_start,btn_record,btn_board,btn_profile,btn_board_all,btn_board_enjoy;
    private ImageButton btn_AddBoard;

    private ArrayList<BoardItemData> mArrayList;
    private BoardAdapter mAdapter;
    String photo;
    static int i=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList boardAll=new ArrayList();

    private long backpressedTime = 0;
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 로그인페이지로 이동됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(BoardEnjoyActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_enjoy);

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

        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_board_list);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList=new ArrayList<BoardItemData>();
        mAdapter=new BoardAdapter(mArrayList,this.getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        loadData();

        sharedPreferences = getSharedPreferences("board",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        //READ BOARD
        mAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Intent intent=new Intent(BoardEnjoyActivity.this,BoardReadActivity.class);
                intent.putExtra("title",mArrayList.get(pos).getTitle());
                intent.putExtra("content",mArrayList.get(pos).getContent());

                //수정
                intent.putExtra("photo",mArrayList.get(pos).getImgList().toString());
                /*intent.putExtra("img2",mArrayList.get(pos).getImgList().get(1));
                intent.putExtra("img3",mArrayList.get(pos).getImgList().get(2));*/
                System.out.println("Board photo"+mArrayList.get(pos).getImgList().toString());
                /*intent.putExtra("img3",mArrayList.get(pos).getImgList().get(2).toString());*/


                i=pos;
                startActivityForResult(intent,REQUEST_READ_CODE);


            }

            @Override
            public void OnDeleteClick(View v, int pos) {

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_display_start:
                Intent intent=new Intent(BoardEnjoyActivity.this,DisplayStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_record:
                Intent intent2=new Intent(BoardEnjoyActivity.this,RecordActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                break;
            case R.id.btn_board:
                Intent intent3=new Intent(BoardEnjoyActivity.this,BoardActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
                break;
            case R.id.btn_profile:
                Intent intent4=new Intent(BoardEnjoyActivity.this,ProfileActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent4);
                break;
            case R.id.btn_AddBoard:
                Intent intent5=new Intent(BoardEnjoyActivity.this,CreateBoardActivity.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent5);
                break;
            case R.id.btn_board_all:
                Intent intent6=new Intent(BoardEnjoyActivity.this,BoardActivity.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent6);
                break;
            case R.id.btn_board_enjoy:
                Intent intent7=new Intent(BoardEnjoyActivity.this,BoardEnjoyActivity.class);
                intent7.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent7);
                break;
        }
    }

    private void loadData() {
        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("board", MODE_PRIVATE);
        Type type=new TypeToken<ArrayList<BoardItemData>>(){}.getType();
        Map<String,?> keys = sharedPreferences.getAll();
        Gson gson=new Gson();
        //board 테이블의 전체값을 불러온다.
        for(Map.Entry<String,?> entry : keys.entrySet()){
            String json=entry.getValue().toString();
            ArrayList<BoardItemData> data=gson.fromJson(json,type);
            boardAll.add(data);
            for(int i=0;i<data.size();i++){
                mArrayList.add(data.get(i));
            }

        }
        /*System.out.println(mArrayList.get(0).getName());*/
    }
}