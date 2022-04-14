package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText et_id,et_pass,et_name,et_age;
    private Button btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        et_name=findViewById(R.id.et_name);
        et_age=findViewById(R.id.et_age);

        btn_signup=findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력되어있는 값을 get해온다.
                String userID=et_id.getText().toString();
                String userPass=et_pass.getText().toString();
                String userName=et_name.getText().toString();
                int userAge=Integer.parseInt(et_age.getText().toString());

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+"respon");
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            //회원가입 성공
                            if(success){
                                Toast.makeText(getApplicationContext(),"회원등록에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignupActivity.this, com.example.androidproject.LoginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),"회원등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 Volley를 이용해서 요청을 함
                //com.example.androidproject.SignupRequest signupRequest=new com.example.androidproject.SignupRequest(userID,userPass,userName,userAge,responseListener);
                SignupRequest signupRequest=new SignupRequest(userID,userPass,userName,userAge,responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignupActivity.this);
                queue.add(signupRequest);
            }
        });
    }
}