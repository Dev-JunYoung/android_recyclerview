package com.example.androidproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class BoardEditActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_edit,btn_cancle,edit_btn_image;
    EditText edit_et_title,edit_et_content;
    ImageView edit_iv_board_img,edit_iv_board_img2,edit_iv_board_img3;
    String imgStr;
    String title,content,img;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int CAMERA = 100; // 카메라 선택시 인텐트로 보내는 값
    final int GALLERY = 101; // 갤러리 선택 시 인텐트로 보내는 값


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);

        edit_et_title=findViewById(R.id.edit_et_title);
        edit_et_content=findViewById(R.id.edit_et_content);
        edit_iv_board_img=findViewById(R.id.edit_iv_board_img);
        edit_iv_board_img2=findViewById(R.id.edit_iv_board_img2);
        edit_iv_board_img3=findViewById(R.id.edit_iv_board_img3);


        btn_edit=findViewById(R.id.btn_edit);
        edit_btn_image=findViewById(R.id.edit_btn_image);
        btn_cancle=findViewById(R.id.btn_cancle);

        btn_edit.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        edit_btn_image.setOnClickListener(this);



        Intent intent=getIntent();

        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");

        //img=intent.getStringExtra("img");

        String imgStr=intent.getStringExtra("img");
        String imgStr2=intent.getStringExtra("img2");
        String imgStr3=intent.getStringExtra("img3");


        edit_et_title.setHint(title);
        edit_et_content.setHint(content);


        try {
            edit_iv_board_img.setImageURI(Uri.parse(imgStr));
            edit_iv_board_img2.setImageURI(Uri.parse(imgStr2));
            edit_iv_board_img3.setImageURI(Uri.parse(imgStr3));
        }catch (NullPointerException e){
            System.out.println("BoardEditActivity.onStart Img NullPointerException");
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_edit:
                Intent intent=new Intent(BoardEditActivity.this,BoardReadActivity.class);
                intent.putExtra("title",edit_et_title.getText().toString());
                intent.putExtra("content",edit_et_content.getText().toString());
                intent.putExtra("img",imgStr);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.btn_cancle:
                Intent intent2=getIntent();
                finish();
                setResult(RESULT_OK,intent2);
                break;
            case R.id.edit_btn_image:
                AlertDialog.Builder ad = new AlertDialog.Builder(BoardEditActivity.this);
                boolean hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean hasWritePerm = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                break;
        }
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
            edit_iv_board_img.setImageBitmap(imageBitmap);
            imgStr=getImageUri(this.getApplicationContext(),imageBitmap).toString();

        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
          /*  Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_profile.setImageBitmap(imageBitmap);*/

            Uri selectedImageUri = data.getData();
            //System.out.println("이값   :  "+selectedImageUri);
            edit_iv_board_img.setImageURI(selectedImageUri);
            imgStr=selectedImageUri.toString();
            //System.out.println("크리에이트보드 이미지 스트링 : "+imgStr);
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
    public void onBackPressed() {
        /*title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");
        img=intent.getStringExtra("img");

        edit_et_title.setHint(title);
        edit_et_content.setHint(content);
        edit_iv_board_img.setImageURI(Uri.parse(img));*/

        //String title,content,img;
        Intent intent2=getIntent();
        finish();
        setResult(RESULT_OK,intent2);

        Intent intent=getIntent();
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("img",imgStr);
        finish();
        setResult(RESULT_CANCELED,intent);
    }
}