package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class temporaryActivity extends AppCompatActivity {
    private EditText et_TempId,et_TempPass,et_TempName,et_TempAge;
    private Button btn_TempLogin;
    String tempId,tempPass,tempName;
    String tempAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);
        et_TempId=findViewById(R.id.et_TempId);
        et_TempPass=findViewById(R.id.et_TempPass);
        et_TempName=findViewById(R.id.et_TempName);
        et_TempAge=findViewById(R.id.et_TempAge);

        btn_TempLogin=findViewById(R.id.btn_TempLogin);
        btn_TempLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(temporaryActivity.this,ProfileActivity.class);
                tempId=et_TempId.getText().toString();
                tempPass=et_TempPass.getText().toString();
                tempName=et_TempName.getText().toString();
                tempAge=et_TempAge.getText().toString();

                intent.putExtra("tempId",tempId);
                intent.putExtra("tempPass",tempPass);
                intent.putExtra("tempName",tempName);
                intent.putExtra("tempAge",tempAge);
                if(tempId.length()<3){
                    Toast.makeText(temporaryActivity.this, "ID,Password는 세 글자 이상 작성하시오.", Toast.LENGTH_SHORT).show();
                    if(tempPass.length()<3 ){
                        if(tempName.length()<2){
                            Toast.makeText(temporaryActivity.this, "Name은 두 글자 이상 작성하시오.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(temporaryActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }//onClick

    @Override
    protected void onPause() {
        et_TempId.setText("");
        et_TempPass.setText("");
        et_TempName.setText("");
        et_TempAge.setText("");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}