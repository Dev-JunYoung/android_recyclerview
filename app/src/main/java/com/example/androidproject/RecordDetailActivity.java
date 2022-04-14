package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordDetailActivity extends AppCompatActivity {
TextView tv_distance,tv_time,tv_step,tv_MaxHeight,tv_MinHeight,tv_avg;
Button btn_back;
String distance,time,step,maxHeight,minHeight,avg;


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



        Intent intent=getIntent();

        distance=intent.getStringExtra("distance");
        time=intent.getStringExtra("time");
        step=intent.getStringExtra("step");
        maxHeight=intent.getStringExtra("maxHeight");
        minHeight=intent.getStringExtra("minHeight");
              avg=intent.getStringExtra("avg");
        System.out.println(avg);
        tv_distance.setText("  거리  : "+distance);
        tv_time.setText("  시간  : "+time);
        tv_step.setText("  걸음  : "+step);
        tv_MaxHeight.setText("최고고도 : "+maxHeight);
        tv_MinHeight.setText("최저고도 : "+minHeight);
        tv_avg.setText("평균시간 : "+avg);





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
}

