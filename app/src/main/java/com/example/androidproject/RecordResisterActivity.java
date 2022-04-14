package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RecordResisterActivity extends AppCompatActivity {
//저장 버튼 리스너
//
    private Button btn_register;
    private EditText et_distance;
    private EditText et_time;
    private EditText et_step;
    private EditText et_MaxHeight;
    private EditText et_MinHeight;
    private EditText et_avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_resister);
        btn_register=findViewById(R.id.btn_register);

        et_distance=findViewById(R.id.et_distance);
        et_time=findViewById(R.id.et_time);
        et_step=findViewById(R.id.et_step);
        et_MaxHeight=findViewById(R.id.et_MaxHeight);
        et_MinHeight=findViewById(R.id.et_MinHeight);
        et_avg=findViewById(R.id.et_avg);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                try {
                    int i=Integer.parseInt(et_MaxHeight.getText().toString());
                    int j=Integer.parseInt(et_MinHeight.getText().toString());
                    String height= String.valueOf(i-j);
                    intent.putExtra("distance",et_distance.getText().toString());
                    intent.putExtra("time",et_time.getText().toString());
                    intent.putExtra("step",et_step.getText().toString());
                    intent.putExtra("height",height);
                    intent.putExtra("MaxHeight",et_MaxHeight.getText().toString());
                    intent.putExtra("MinHeight",et_MinHeight.getText().toString());
                    intent.putExtra("avg",et_avg.getText().toString());
                    setResult(RESULT_OK,intent);
                }catch (NullPointerException e){
                    System.out.println("RecordResisterActivity.btn_registerOnclick NullPointException");
                    setResult(RESULT_CANCELED,intent);
                }catch (NumberFormatException e){
                    System.out.println("RecordResisterActivity.btn_registerOnclick NumberFormatException");
                    setResult(RESULT_CANCELED,intent);
                }


                finish();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
/*    String resultDistance=data.getStringExtra("distance");
            String resultTime=data.getStringExtra("time");
            String resultHeight=data.getStringExtra("height");
            String resultStep=data.getStringExtra("step");


            String resultAvg=data.getStringExtra("avg");
            String resultMaxHeight=data.getStringExtra("MaxHeight");
            String resultMinHeight=data.getStringExtra("MinHeight");*/