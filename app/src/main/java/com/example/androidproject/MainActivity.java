package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import java.io.File;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    private EditText et_id,et_pass;
    private Button btn_login,btn_signup,btn_kakao;
    final String TAG="MainActivity";
    SharedPreferences sharedPreferences;
    static boolean loginCheck=true;
    ImageView profileImageView;
    public static User user;
    static String kakaoImg;
    static boolean kakaoCheck=false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            System.out.println("키"+getKeyHash(this));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        btn_login=findViewById(R.id.btn_login);
        btn_signup=findViewById(R.id.btn_signup);
        btn_kakao=findViewById(R.id.btn_kakao);



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

                if(data!=null){
                    for (int i=0;i<data.size();i++){

                        //아이디 비밀번호확인.
                        System.out.println("아이디 비밀번호확인.");
                        if(data.get(i).getId().equals(et_id.getText().toString())){
                            if(data.get(i).getPass().equals(et_pass.getText().toString())){
                                Intent intent=new Intent(MainActivity.this,DisplayStartActivity.class);
                                et_id.setText("");
                                et_pass.setText("");
                                user=new User(data.get(i).getId(),data.get(i).getPass(),data.get(i).getName(),data.get(i).getAge());

                                SharedPreferences current=getSharedPreferences("currentUser",MODE_PRIVATE);
                                SharedPreferences.Editor editor=current.edit();
                                String inputJson=gson.toJson(user);
                                editor.putString("current",inputJson);
                                editor.commit();

                                startActivity(intent);
                            }else {

                            }
                        }else {

                        }

                    }
                }
               if(user==null){
                    Toast.makeText(MainActivity.this, "아이디, 비밀번호를 확인 하세요. ", Toast.LENGTH_SHORT).show();
                }



            }
        });
        Function2<OAuthToken,Throwable,Unit> callback =new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드 ,
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG,"CallBack Method");
                //oAuthToken != null 로그인 성공공
                if(oAuthToken!=null){
                    //Login Ok
                    updateKakaoLoginUi();

                }else {
                    //Login fail
                }

                return null;
            }
        };
        // 카카오 로그인
        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기기에 카카오 설치 유무
                //LoginClient
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,callback );
                }else {
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,callback);
                }
            }
        });




    }

    private void updateKakaoLoginUi() {
        // 로그인 확인
        UserApiClient.getInstance().me(new Function2<com.kakao.sdk.user.model.User, Throwable, Unit>() {
            @Override
            public Unit invoke(com.kakao.sdk.user.model.User kakaoUser, Throwable throwable) {
                if(kakaoUser !=null){
                    //로그인 ok
                    Log.e(TAG,"user.getId() : "+kakaoUser.getId());
                   // Log.e(TAG,"user.getName() : "+ user.getKakaoAccount().getName());
                    Log.e(TAG,"user.getNickname() : "+kakaoUser.getKakaoAccount().getProfile().getNickname());
                    Log.e(TAG,"user.getThumbnailImageUrl() : "+kakaoUser.getKakaoAccount().getProfile().getThumbnailImageUrl());
                    Log.e(TAG,"user.getAgeRange() : "+kakaoUser.getKakaoAccount().getAgeRange());
                    Log.e(TAG,"user.getBirthday() : "+kakaoUser.getKakaoAccount().getBirthday());
                    kakaoImg= kakaoUser.getKakaoAccount().getProfile().getThumbnailImageUrl();
                    user=new User(String.valueOf(kakaoUser.getId())
                            ,String.valueOf(kakaoUser.getId())
                            ,kakaoUser.getKakaoAccount().getProfile().getNickname()
                            ,String.valueOf(kakaoUser.getKakaoAccount().getAgeRange()));

                    //현재 접속 유저 셰어드저장--------------------------------------------------------------------------------
                    Gson gson=new Gson();
                    SharedPreferences current=getSharedPreferences("currentUser",MODE_PRIVATE);
                    SharedPreferences.Editor editor=current.edit();
                    String inputJson=gson.toJson(user);
                    editor.putString("current",inputJson);
                    editor.commit();
                    //--------------------------------------------------------------------------------
                    // set 프로필
                    // id 키값으로 존재 유무 판단
                    // 있으면 가져와서 set Image, 없으면 카카오계정 프로필 사용
                    SharedPreferences preferences=getSharedPreferences("userProfile",0);
                    SharedPreferences.Editor imgEditor=preferences.edit();

                    File file=new File(
                            "/data/data/com.example.androidproject/shared_prefs/userProfile.xml"
                    );


                    kakaoCheck=true;
                    Intent intent=new Intent(MainActivity.this,DisplayStartActivity.class);
                    startActivity(intent);


                    //Log.e(TAG,user.getKakaoAccount().getName());
                    //  Log.e(TAG,user.getKakaoAccount().getProfile().getNickname());
                    //Log.e(TAG,user.getKakaoAccount().getProfile().getProfileImageUrl());


                }else {

                }
                return null;
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
        user=null;
        kakaoCheck=false;
        //user
    }
    public String getKeyHash(final Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getPackageManager()
                .getPackageInfo("com.example.androidproject", PackageManager.GET_SIGNATURES);//Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "디버그 keyHash" + signature, e);
            }
        }
        return null;
    }


}
