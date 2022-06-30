package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private EditText et_id,et_pass,et_name,et_age;
    private Button btn_signup;

    String id,pass,name,age;

    SharedPreferences sharedPreferences;
    Gson gson;
    Context context;

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

                id=et_id.getText().toString();
                pass=et_pass.getText().toString();
                name=et_name.getText().toString();
                age=et_age.getText().toString();

                context=getApplicationContext();

                // 1. 입력 받은 값으로 유저 객체 생성
                //
                        // 회원 중복 확인
                        // 1. 리스트불러와서 for 문으로 id값 일치하는지 확인.
                        // 2. 없으면 회원가입, 있으면 다시 값 입력.

                // 2. 셰어드의 id를 키값으로 저장
                // 3. 회원 리스트에 추가.
                // 4. 회원 리스트 저장소 1개, id를 키값으로 회원 정보를 저장 하는 저장소 1개. ( 회원 정보안에는 회원 정보와 회원이 작성한 게시글이 있다.)

                //1
                //1
                User user=new User(id,pass,name,age);
                Intent intent=new Intent(SignupActivity.this,MainActivity.class);
                //회원 정보 가져오기.
                //저장소 불러오기
                SharedPreferences userListSharedPreferences = getSharedPreferences("userList",MODE_PRIVATE);
                SharedPreferences.Editor editor=userListSharedPreferences.edit();
                Gson gson=new Gson();
                //"키값" get 데이터 -> 모든 키 값 가져오기.

                // 기존
                String json=userListSharedPreferences.getString("userList",null);

                Type type=new TypeToken<ArrayList<User>>(){}.getType();
                // userArrayList == 저장소에 있던 userList 가져온 것.
                ArrayList<User> userArrayList=gson.fromJson(json,type);
                System.out.println("회원 가입 전 : "+userArrayList);
                boolean check=true;
                if (userArrayList == null) {
                    //최초 실행
                    userArrayList = new ArrayList<>();
                    //바로 add
                    userArrayList.add(user);
                    //userArrayList JSON 으로 변환
                    String jsonUserList=gson.toJson(userArrayList);
                    // 키값 : userList 으로 저장
                    editor.putString("userList",jsonUserList);
                    // 커밋
                    editor.commit();
                    Toast.makeText(SignupActivity.this, "회원 가입에 성공 하셨습니다.", Toast.LENGTH_SHORT).show();
                    //스타트
                    startActivity(intent);
                }else {
                    for(int i=0;i<userArrayList.size();i++){
                        //중복체크
                        if(userArrayList.get(i).getId().equals(user.getId())){
                            check=false;
                        }
                    }
                    if(check){ //중복체크확인
                        //userArrayList.add(user);
                        //값 생성
                        //리스트에 추가
                        userArrayList.add(user);
                        //JSON 변환
                        String jsonUserList=gson.toJson(userArrayList);
                        //변환된 데이터 키값 "userList" 에 넣기.
                        editor.putString("userList",jsonUserList);
                        editor.commit();
                        startActivity(intent);
                        Toast.makeText(SignupActivity.this, "회원 가입에 성공 하셨습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignupActivity.this, "중복 ID입니다..", Toast.LENGTH_SHORT).show();
                    }
                }//else


            }
        });
    }

    // 중복확인
    // 셰어드저장
    // 유저리스트 저장.
    
}