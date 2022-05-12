package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MountainReadActivity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG="MountainReadActivity";

    TextView tv_mntName,tv_mntSubName,tv_ntHeight,tv_mntLocation,tv_mntCourse,tv_mnt_text,tv_mntPickReason,tv_mntDetail,tv_mntOverview,tv_mntTransport,tv_mntTourInfo;
    Button btn_mnt_pickReason,btn_mnt_detail,btn_mnt_overview,btn_mnt_trans,btn_mnt_tour;


    //산이름
    private String mntName;
    //별칭
    private String mntSubName;
    //해발고도
    private String mntHeight;
    //소재지
    private String mntLocation;
    //등산코스
    private String mntCourse;
    //선정이유
    private String mntPickReason;
    //상세설명
    private String mntDetail;
    //개요
    private String mntOverview;
    //오시는 길(
    private String mntTransport;
    //숙소정보
    private String mntTourInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_read);

        tv_mntName=findViewById(R.id.read_mnt_name);
        tv_mntSubName=findViewById(R.id.read_mnt_subName);
        tv_ntHeight=findViewById(R.id.read_mnt_height);
        tv_mntLocation=findViewById(R.id.read_mnt_location);
        tv_mntCourse=findViewById(R.id.read_mnt_course);


        btn_mnt_pickReason=findViewById(R.id.btn_mnt_pickReason);
        btn_mnt_detail=findViewById(R.id.btn_mnt_detail);
        btn_mnt_overview=findViewById(R.id.btn_mnt_overview);
        btn_mnt_trans=findViewById(R.id.btn_mnt_trans);
        btn_mnt_tour=findViewById(R.id.btn_mnt_tour);

        tv_mnt_text=findViewById(R.id.mnt_text);

        mntName=getIntent().getStringExtra("mntName");
        mntSubName=getIntent().getStringExtra("mntSubName");
        mntHeight=getIntent().getStringExtra("mntHeight");
        mntLocation=getIntent().getStringExtra("mntLocation");
        mntCourse=getIntent().getStringExtra("mntCourse");
        mntPickReason=getIntent().getStringExtra("mntPickReason");
        mntDetail=getIntent().getStringExtra("mntDetail");
        mntOverview=getIntent().getStringExtra("mntOverview");
        mntTransport=getIntent().getStringExtra("mntTransport");
        mntTourInfo=getIntent().getStringExtra("mntTourInfo");

        tv_mntName.setText(mntName);
        tv_mntSubName.setText(mntSubName);
        tv_ntHeight.setText(mntHeight+"m");
        tv_mntLocation.setText("-"+mntLocation.replace(",","\n-"));
        tv_mntCourse.setText(mntCourse.replace("<BR>","\n").replace("<br>","\n"));

        tv_mnt_text.setText(mntOverview.replace("&lt;","<").replace("<BR>","\n").replace("&gt;",">").replace("<br>","\n"));

        btn_mnt_detail.setOnClickListener(this);
        btn_mnt_overview.setOnClickListener(this);
        btn_mnt_pickReason.setOnClickListener(this);
        btn_mnt_trans.setOnClickListener(this);
        btn_mnt_tour.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_mnt_overview:
                tv_mnt_text.setText(mntOverview.replace("&lt;","<").replace("&gt;",">").replace("<BR>","\n").replace("<br>","\n"));
                break;
            case R.id.btn_mnt_detail:
                tv_mnt_text.setText(mntDetail.replace("&lt;","<").replace("&gt;",">").replace("<BR>","\n").replace("<br>","\n"));
                break;
            case R.id.btn_mnt_pickReason:
                tv_mnt_text.setText(mntPickReason.replace("&lt;","<").replace("&gt;",">").replace("<BR>","\n").replace("<br>","\n"));
                break;
            case R.id.btn_mnt_trans:
                tv_mnt_text.setText(mntTransport.replace("&lt;","<").replace("&gt;",">").replace("<BR>","\n").replace("<br>","\n"));
                break;
            case R.id.btn_mnt_tour:
                tv_mnt_text.setText(mntTourInfo.replace("&lt;","<").replace("&gt;",">").replace("<BR>","\n").replace("<br>","\n"));
                break;


        }
    }
}