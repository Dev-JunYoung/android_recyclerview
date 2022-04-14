package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BoardEnjoyActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_display_start,btn_record,btn_board,btn_profile,btn_board_all,btn_board_enjoy;
    private ImageButton btn_AddBoard;

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
}