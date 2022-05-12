package com.example.androidproject;

import static com.example.androidproject.DisplayStartActivity.profile_cnt;
import static com.example.androidproject.DisplayStartActivity.profile_distance;
import static com.example.androidproject.DisplayStartActivity.profile_step;
import static com.example.androidproject.DisplayStartActivity.profile_time;
import static com.example.androidproject.MainActivity.kakaoCheck;
import static com.example.androidproject.MainActivity.kakaoImg;
import static com.example.androidproject.MainActivity.user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG="ProfileActivity";

    private Button btn_display_start, btn_record, btn_board, btn_profile, btn_update;
    String tempId, tempPass;
    public static String tempName;
    String tempAge;



    Context context;
    String distance, time, step, cnt;

    TextView tv_id, tv_pass, tv_name, tv_age, tv_distance, tv_time, tv_step, tv_cnt;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int CAMERA = 100; // 카메라 선택시 인텐트로 보내는 값
    final int GALLERY = 101; // 갤러리 선택 시 인텐트로 보내는 값




    SharedPreferences sharedPreferences;
    Gson gson;


    private ArrayList<BoardItemData> mArrayList;
    private BoardAdapter mAdapter;



    ImageView iv_profile;

    private long backpressedTime = 0;
    String imgStr;
    private java.lang.Object Object;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 로그인페이지로 이동됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override //menu버튼 객체화
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup, menu);
        return true;
    }

    @Override //menu 버튼 클릭 choose
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop1:
                Toast.makeText(this, "정보수정", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder ad = new AlertDialog.Builder(ProfileActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("정보수정");
                ad.setMessage("이름변경");

                final EditText et = new EditText(ProfileActivity.this);
                ad.setView(et);
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //onVisibleBehindCanceled();
                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = et.getText().toString();
                        tv_name.setText("이름 : "+result);
                        dialogInterface.dismiss();
                    }
                });
                ad.show();
                break;
            case R.id.pop2:
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
                Sharing_intent.setType("text/plain");
                String Test_Message = tv_name.getText().toString();
                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);
                Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        iv_profile = findViewById(R.id.iv_profile);


        //이미생성된 액티비티가 존재한다면
        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        tv_name = findViewById(R.id.tv_name);
        tv_age = findViewById(R.id.tv_age);

        tv_distance = findViewById(R.id.tv_distance);
        tv_time = findViewById(R.id.tv_time);
        tv_step = findViewById(R.id.tv_step);
        tv_cnt = findViewById(R.id.tv_cnt);

        Intent intent = getIntent();



        SharedPreferences sharedPreferences=getSharedPreferences("currentUser",MODE_PRIVATE);
        String json=sharedPreferences.getString("current",null);
        Gson gson=new Gson();
        Type type=new TypeToken<User>(){}.getType();
        User data=gson.fromJson(json,type);

        tv_name.setText("이름 : " + data.getName());
        tv_age.setText("나이 : " + data.getAge());

        btn_display_start = findViewById(R.id.btn_display_start);
        btn_record = findViewById(R.id.btn_record);
        btn_board = findViewById(R.id.btn_board);
        btn_profile = findViewById(R.id.btn_profile);
        btn_update = findViewById(R.id.btn_update);
        //btn_menu=findViewById(R.id.btn_menu);


        btn_update.setOnClickListener(this);
        btn_display_start.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_board.setOnClickListener(this);
        btn_profile.setOnClickListener(this);

        File file=new File(
                "/data/data/com.example.androidproject/shared_prefs/userProfile.xml"
        );
        SharedPreferences preferences=getSharedPreferences("userProfile",0);



        String path;
        if(file.exists()){
            SharedPreferences profile=getSharedPreferences("userProfile",0);
            if(profile.getString(user.getId(),"")!=null){
                iv_profile.setImageURI(Uri.parse(profile.getString(user.getId(),"")));
            }
        }
        Log.e(TAG,kakaoCheck+"");
        if(kakaoCheck){
            Log.e(TAG,"kakaoCheck_Img");
            Glide.with(iv_profile).load(kakaoImg).into(iv_profile);
        }else {
            Log.e(TAG,"normalLogin_Img");
            path=preferences.getString(user.getId(),"");
            iv_profile.setImageURI(Uri.parse(path));
        }


        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_enjoy_board_list);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList=new ArrayList<>();
        mAdapter=new BoardAdapter(mArrayList,this.getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        loadData();



        tv_distance.setText(String.valueOf(profile_distance));
        tv_time.setText(String.valueOf(profile_time));
        tv_step.setText(String.valueOf(profile_step));
        tv_cnt.setText(String.valueOf(profile_cnt));





    } //onCreate

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_display_start:
                Intent intent = new Intent(ProfileActivity.this, DisplayStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_record:
                Intent intent2 = new Intent(ProfileActivity.this, RecordActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                break;
            case R.id.btn_board:
                Intent intent3 = new Intent(ProfileActivity.this, BoardActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
                break;
            case R.id.btn_profile:
                Intent intent4 = new Intent(ProfileActivity.this, ProfileActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent4);
                break;
            case R.id.btn_update:
                AlertDialog.Builder ad = new AlertDialog.Builder(ProfileActivity.this);
                boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (hasCamPerm) {
                    Log.e("hasCamPerm", "true");
                } else {
                    Log.e("hasCamPerm", "false");
                }
                if (hasWritePerm) {
                    Log.e("hasWritePerm", "true");
                } else {
                    Log.e("hasWritePerm", "false");
                }

                if (!hasCamPerm || !hasWritePerm) {  // 권한 없을 시  권한설정 요청
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ad.setIcon(R.mipmap.ic_launcher);
                }
                ad.setTitle("프로필 사진 변경");
                ad.setMessage("사진 or 카메라");

                ad.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                });

                ad.setNegativeButton("사진", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ACTION_PACK 데이터에서 항목을 선택하고 선택한 항목을 반환합니다.
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        //이 이미지 디렉토리의 MIME 유형입니다.
                        // 이 디렉토리의 각 항목은 적절한 표준 이미지 MIME 유형(예: 이미지/jpeg)을 가집니다.
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY); // final int GALLERY = 101;
                    }
                });
                ad.show();
        } //switch
    }//onClick

    @Override
    protected void onPause() {
        super.onPause();
        //Log.e("프로필로그퍼즈", "프로필로그퍼즈");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.e("프로필로그스탑", "프로필로그스탑");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tv_distance.setText(String.valueOf(profile_distance));
        tv_time.setText(String.valueOf(profile_time));
        tv_step.setText(String.valueOf(profile_step));
        tv_cnt.setText(String.valueOf(profile_cnt));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("프로필로그디스트로이", "프로필로그디스트로이");
        Toast.makeText(this, "로그인이 해제 되었습니다. ", Toast.LENGTH_SHORT).show();
    }

    private void dispatchTakePictureIntent() {
        PackageManager manager = getPackageManager();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean pm = takePictureIntent.resolveActivity(getPackageManager()) != null;
        if (pm) {
            Log.e("pm : ", "true");
        } else {
            Log.e("pm : ", "false");
        }

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_profile.setImageBitmap(imageBitmap);
            //여기
            imgStr=getImageUri(this.getApplicationContext(),imageBitmap).toString();
            System.out.println("imgStr : "+imgStr);
        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
          /*  Bundle extras = data.getEx Bitmap imageBitmaptras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_profile.setImageBitmap(imageBitmap);*/

            Uri selectedImageUri = data.getData();
            iv_profile.setImageURI(selectedImageUri);
            imgStr= String.valueOf(selectedImageUri);
        }
        //유저 프로필 저장.
        //에러 났다가 또 안나는...
        SharedPreferences userProfileSharedPreferences =
                getSharedPreferences("userProfile",MODE_PRIVATE);
        SharedPreferences.Editor editor=userProfileSharedPreferences.edit();
        editor.putString(user.getId(),imgStr);
        System.out.println(imgStr);
        editor.commit();
      /*  //유저 프로필 저장.
      //에러 나서 수정했다가 또 에러나서 위에로 교체
        SharedPreferences userProfileSharedPreferences =
                getSharedPreferences("userProfile",MODE_PRIVATE);
        SharedPreferences.Editor editor=userProfileSharedPreferences.edit();

        Cursor c=getContentResolver().query(Uri.parse("content://media/external/images/media/64"),null,null
                ,null,null);
        c.moveToNext();
        @SuppressLint("Range") String path=c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
        editor.putString(user.getId(),path);
        System.out.println("path : "+path);
        editor.commit();*/
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("boardEnjoy", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(user.getId()+"", null);
        System.out.println(json);
        Type type = new TypeToken<ArrayList<BoardItemData>>() {}.getType();
        //Type type = new TypeToken<BoardItemData>() {}.getType();
        ArrayList<BoardItemData> data= gson.fromJson(json, type);
        //BoardItemData data= gson.fromJson(json, type);
        System.out.println("EnjoyBoard : "+data);
        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
        File file=new File(
                "/data/data/com.example.androidproject/shared_prefs/boardEnjoy.xml"
        );
        if(file.exists()){
            if(data!=null){
                for (int i=0;i<data.size();i++){
                    mArrayList.add(data.get(i));
                    System.out.println(mArrayList.get(i));
                }
            }

        }else {
            if(data!=null){
                for (int i=0;i<data.size();i++){
                    mArrayList.add(data.get(i));
                    System.out.println(mArrayList.get(i));
                }
            }

        }
    }
    void delete(){
        SharedPreferences sharedPreferences = getSharedPreferences("boardEnjoy", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(user.getId()+"", null);
        Type type = new TypeToken<ArrayList<BoardItemData>>() {}.getType();
        //Type type = new TypeToken<BoardItemData>() {}.getType();
        ArrayList<BoardItemData> data= gson.fromJson(json, type);
    }

    private void saveData(ArrayList<BoardItemData> list) {
        SharedPreferences sharedPreferences = getSharedPreferences("boardEnjoy", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(user.getId()+"", json);
        editor.apply();
    }

}