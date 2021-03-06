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
    final int CAMERA = 100; // ????????? ????????? ???????????? ????????? ???
    final int GALLERY = 101; // ????????? ?????? ??? ???????????? ????????? ???




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
            Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ????????????????????? ???????????????.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override //menu?????? ?????????
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup, menu);
        return true;
    }

    @Override //menu ?????? ?????? choose
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop1:
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder ad = new AlertDialog.Builder(ProfileActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("????????????");
                ad.setMessage("????????????");

                final EditText et = new EditText(ProfileActivity.this);
                ad.setView(et);
                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //onVisibleBehindCanceled();
                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = et.getText().toString();
                        tv_name.setText("?????? : "+result);
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
                Intent Sharing = Intent.createChooser(Sharing_intent, "????????????");
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


        //??????????????? ??????????????? ???????????????
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

        tv_name.setText("?????? : " + data.getName());
        tv_age.setText("?????? : " + data.getAge());

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

                if (!hasCamPerm || !hasWritePerm) {  // ?????? ?????? ???  ???????????? ??????
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ad.setIcon(R.mipmap.ic_launcher);
                }
                ad.setTitle("????????? ?????? ??????");
                ad.setMessage("?????? or ?????????");

                ad.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                });

                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ACTION_PACK ??????????????? ????????? ???????????? ????????? ????????? ???????????????.
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        //??? ????????? ??????????????? MIME ???????????????.
                        // ??? ??????????????? ??? ????????? ????????? ?????? ????????? MIME ??????(???: ?????????/jpeg)??? ????????????.
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
        //Log.e("?????????????????????", "?????????????????????");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.e("?????????????????????", "?????????????????????");
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
        Log.e("??????????????????????????????", "??????????????????????????????");
        Toast.makeText(this, "???????????? ?????? ???????????????. ", Toast.LENGTH_SHORT).show();
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
            //??????
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
        //?????? ????????? ??????.
        //?????? ????????? ??? ?????????...
        SharedPreferences userProfileSharedPreferences =
                getSharedPreferences("userProfile",MODE_PRIVATE);
        SharedPreferences.Editor editor=userProfileSharedPreferences.edit();
        editor.putString(user.getId(),imgStr);
        System.out.println(imgStr);
        editor.commit();
      /*  //?????? ????????? ??????.
      //?????? ?????? ??????????????? ??? ???????????? ????????? ??????
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