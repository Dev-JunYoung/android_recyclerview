package com.example.androidproject;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class CreateBoardActivity extends AppCompatActivity implements View.OnClickListener {

Button btn_register,btn_image;
EditText et_title,et_content;
ImageView iv_board_img,iv_board2_img,iv_board3_img;
String imgStr,imgStr2,imgStr3;

    static final int REQUEST_IMAGE_CODE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int CAMERA = 100; // 카메라 선택시 인텐트로 보내는 값
    final int GALLERY = 101; // 갤러리 선택 시 인텐트로 보내는 값
    private final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        btn_register=findViewById(R.id.btn_register);
        et_title=findViewById(R.id.et_title);
        et_content=findViewById(R.id.et_content);
        btn_image=findViewById(R.id.btn_image);

        iv_board_img=findViewById(R.id.iv_board_img);
        iv_board2_img=findViewById(R.id.iv_board2_img);
        iv_board3_img=findViewById(R.id.iv_board3_img);

        iv_board_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                select(1);
            }
        });
        iv_board_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(iv_board_img,1);
                return true;
            }
        });
        iv_board2_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                select(2);
            }
        });
        iv_board2_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(iv_board2_img,2);
                return true;
            }
        });
        iv_board3_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                select(3);
            }
        });
        iv_board3_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(iv_board3_img,3);
                return true;
            }
        });


        sharedPreferences = getSharedPreferences("project", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btn_image.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                TimeZone tz;
                Date date=new Date();
                DateFormat df=new SimpleDateFormat("yy-MM-dd HH:mm");
                tz=TimeZone.getTimeZone("Asia/Seoul");
                df.setTimeZone(tz);
                String timeStamp = df.format(date);

                //System.out.println("이미지투스트링 : "+imgStr);
                Intent intent=new Intent(CreateBoardActivity.this,BoardActivity.class);
                if(et_title!=null && et_content!=null && imgStr!=null ){

                    String title=et_title.getText().toString();
                    String content=et_title.getText().toString();
                    ArrayList photo=new ArrayList();

                    System.out.println("Create btn_register imgStr : "+ imgStr);
                    System.out.println("Create btn_register imgStr : "+ imgStr2);
                    System.out.println("Create btn_register imgStr : "+ imgStr3);

                    photo.add(imgStr);
                    photo.add(imgStr2);
                    photo.add(imgStr3);



                    intent.putExtra("title",et_title.getText().toString());
                    intent.putExtra("content",et_content.getText().toString());
                    intent.putExtra("img",imgStr);
                    intent.putExtra("img2",imgStr2);
                    intent.putExtra("img3",imgStr3);


                    Toast.makeText(this, "등록완료.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    Toast.makeText(this, "입력하지 않는 항목이 있습니다..", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_image:
                //   iv_board_img.setImageURI(Uri.parse(result));
                AlertDialog.Builder ad = new AlertDialog.Builder(CreateBoardActivity.this);
                boolean hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean hasWritePerm = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


                if (!hasCamPerm || !hasWritePerm) {  // 권한 없을 시  권한설정 요청
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ad.setIcon(R.mipmap.ic_launcher);
                }
                ad.setTitle("프로필 사진 변경");
                ad.setMessage("사진 or 카메라");

                ad.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent(1);
                    }
                });

                ad.setNegativeButton("사진", new DialogInterface.OnClickListener() {
                    @Override //Look up 시작.
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(CreateBoardActivity.this,PhotoActivity.class);
                        startActivityForResult(intent,REQUEST_IMAGE_CODE);
                    }
                });
                ad.show();

                break;
        }


    }

    private void dispatchTakePictureIntent(int k) {
        PackageManager manager = getPackageManager();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /* boolean pm = takePictureIntent.resolveActivity(getPackageManager()) != null;
        if (pm) {
            Log.e("pm : ", "true");
        } else {
            Log.e("pm : ", "false");
        }*/

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
            iv_board_img.setImageBitmap(imageBitmap);
            imgStr=getImageUri(this.getApplicationContext(),imageBitmap).toString();


        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            iv_board_img.setImageURI(selectedImageUri);
            imgStr=selectedImageUri.toString();
        }
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){
            //String 으로 받은 Path 값 Uri.parse해서 셋팅.
            String result=data.getStringExtra("0");
            String result2=data.getStringExtra("1");
            String result3=data.getStringExtra("2");

            try {
                iv_board_img.setImageURI(Uri.parse(result));
            }catch (NullPointerException e){
                System.out.println("CreateBoard .setImageURI 1");
            }
            try {
                iv_board2_img.setImageURI(Uri.parse(result2));
            }catch (NullPointerException e){
                System.out.println("CreateBoard .setImageURI 2");
            }
            try {
                iv_board3_img.setImageURI(Uri.parse(result3));
            }catch (NullPointerException e){
                System.out.println("CreateBoard .setImageURI 3");
            }




            imgStr=result.toString();
            if(result2!=null){
                imgStr2=result2.toString();
            }
            if (result3!=null){
                imgStr3=result3.toString();
            }



        }


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

    @RequiresApi(api = Build.VERSION_CODES.M)
    void select(int k){
        AlertDialog.Builder ad = new AlertDialog.Builder(CreateBoardActivity.this);
        boolean hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasCamPerm || !hasWritePerm) {  // 권한 없을 시  권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ad.setIcon(R.mipmap.ic_launcher);
        }
        ad.setTitle("프로필 사진 변경");
        ad.setMessage("사진 or 카메라");

        ad.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dispatchTakePictureIntent(k);
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
                if(k==1){
                    startActivityForResult(intent, GALLERY);
                }else if(k==2){
                    startActivityForResult(intent, GALLERY+1);
                } else if (k == 3) {
                    startActivityForResult(intent, GALLERY+2);
                }
                // final int GALLERY = 101;
            }
        });

        ad.show();

    }
    void delete(ImageView iv,int i){
        AlertDialog.Builder ad = new AlertDialog.Builder(CreateBoardActivity.this);
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("DELETE");
        ad.setMessage("삭제하시겠습니까");

        ad.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                iv.setImageBitmap(null);
                iv.setVisibility(View.GONE);
                if(i==1){
                    imgStr="";
                }else if(i==2){
                    imgStr2="";
                }else if(i==3){
                    imgStr3="";
                }

            }
        });
        ad.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {

            }
        });
        ad.show();

    }


}