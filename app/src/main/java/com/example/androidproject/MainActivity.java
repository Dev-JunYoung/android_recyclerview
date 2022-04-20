package com.example.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText et_id,et_pass;
    private Button btn_login,btn_signup,btn_temporary;

    SharedPreferences sharedPreferences;


    public static User user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        btn_login=findViewById(R.id.btn_login);
        btn_signup=findViewById(R.id.btn_signup);
        btn_temporary=findViewById(R.id.btn_temporary);


        //임시로그인
        btn_temporary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,temporaryActivity.class);
                startActivity(intent);
            }
        });







        //회원가입 버튼 클릭시 수행
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        //로그인 클릭시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("userList",MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("userList", null);
                Type type = new TypeToken<ArrayList<User>>() {}.getType();
                ArrayList<User> data= gson.fromJson(json, type);

                for (int i=0;i<data.size();i++){
                    //아이디 비밀번호확인.
                    System.out.println("아이디 비밀번호확인.");
                    if(data.get(i).getId().equals(et_id.getText().toString())){
                        System.out.println("아이디 일치");
                        if(data.get(i).getPass().equals(et_pass.getText().toString())){
                            System.out.println("비밀번호 일치");
                            Intent intent=new Intent(MainActivity.this,DisplayStartActivity.class);
                            et_id.setText("");
                            et_pass.setText("");
                            user=new User(data.get(i).getId(),data.get(i).getPass(),data.get(i).getName(),data.get(i).getAge());


                            Toast.makeText(MainActivity.this, data.get(i).getName()+"님이 로그인 하셨습니다. ", Toast.LENGTH_SHORT).show();
                            startActivity(intent);


                        }else {

                        }
                    }else {

                    }

                }
                Toast.makeText(MainActivity.this, "아이디, 비밀번호를 확인 하세요. ", Toast.LENGTH_SHORT).show();


            }
        });




    }


       /*         String userID=et_id.getText().toString();
                String userPass=et_pass.getText().toString();

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            //회원가입 성공
                            if(success){
                                String userID=jsonObject.getString("userID");
                                String userPass=jsonObject.getString("userPassword");
                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userPass",userPass);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest=new LoginRequest(userID,userPass,responseListener);
                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);*/

    @Override
    protected void onRestart() {
        super.onRestart();
        //user
    }
}
