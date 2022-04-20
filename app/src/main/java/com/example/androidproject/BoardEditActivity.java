package com.example.androidproject;

import static com.example.androidproject.BoardActivity.REQUEST_EDIT_CODE;
import static com.example.androidproject.CreateBoardActivity.REQUEST_IMAGE_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
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
    String imgStr,imgStr2,imgStr3;
    String title,content,img;
    String str="";

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

        imgStr=intent.getStringExtra("photo");

        System.out.println("imgStr"+imgStr);
        String[] a=imgStr.split(", ");


        Log.e("길이","" + a.length);
        //READ 에서 받은 값
        if(a.length>=1){
            edit_iv_board_img.setImageURI(Uri.parse(a[0]));
            imgStr=a[0];
        }
        if(a.length>=2){
            edit_iv_board_img2.setImageURI(Uri.parse(a[1]));
            imgStr2=a[1];
        }
        if(a.length>=3){
            edit_iv_board_img3.setImageURI(Uri.parse(a[2]));
            imgStr3=a[2];
        }




        edit_et_title.setText(title);
        edit_et_content.setText(content);

        for(int i=0;i<a.length;i++){

        }
/*
            for(int i=0;i<a.length;i++){
                edit_iv_board_img.setImageURI(Uri.parse(a[0]));
                edit_iv_board_img2.setImageURI(Uri.parse(a[1]));
                edit_iv_board_img3.setImageURI(Uri.parse(a[2]));

            }*/
      /*  try {
            edit_iv_board_img.setImageURI(Uri.parse(a[0]));
            edit_iv_board_img2.setImageURI(Uri.parse(a[1]));
            edit_iv_board_img3.setImageURI(Uri.parse(a[2]));
        }catch (NullPointerException e){
            System.out.println("BoardEditActivity.onStart Img NullPointerException");
        }*/

        edit_iv_board_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                select(1);
            }
        });
        edit_iv_board_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(edit_iv_board_img,1);
                return true;
            }
        });

        edit_iv_board_img2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                select(2);
            }
        });
        edit_iv_board_img2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(edit_iv_board_img2,2);
                return true;
            }
        });
        edit_iv_board_img3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                select(3);
            }
        });
        edit_iv_board_img3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(edit_iv_board_img3,3);
                return true;
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_edit:

                Intent intent=new Intent(BoardEditActivity.this,BoardReadActivity.class);
                intent.putExtra("title",edit_et_title.getText().toString());
                intent.putExtra("content",edit_et_content.getText().toString());

                //imgStr 은 수정되거나, 진입하면서 setImage 한 URI
                intent.putExtra("img",imgStr);
                intent.putExtra("img2",imgStr2);
                intent.putExtra("img3",imgStr3);

                System.out.println("Edit imgStr"+imgStr);
                System.out.println("Edit imgStr2"+imgStr2);
                System.out.println("Edit imgStr3"+imgStr3);


                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.btn_cancle:
                Intent intent2=getIntent();
                finish();
                setResult(RESULT_OK,intent2);
                break;
            case R.id.edit_btn_image:

                //   iv_board_img.setImageURI(Uri.parse(result));
                AlertDialog.Builder ad = new AlertDialog.Builder(BoardEditActivity.this);
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
                        Intent intent=new Intent(BoardEditActivity.this,PhotoActivity.class);
                        startActivityForResult(intent,REQUEST_IMAGE_CODE);//3
                    }
                });
                ad.show();

                break;
        }
    }
    private void dispatchTakePictureIntent(int k) {
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
            System.out.println("if");
        }

        if(k==1){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else if (k==2) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE+1);
        } else if (k == 3) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE+2);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            edit_iv_board_img.setImageBitmap(imageBitmap);
            imgStr=getImageUri(this.getApplicationContext(),imageBitmap).toString();
        } if (requestCode == REQUEST_IMAGE_CAPTURE+1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            edit_iv_board_img2.setImageBitmap(imageBitmap);
            imgStr=getImageUri(this.getApplicationContext(),imageBitmap).toString();
        } if (requestCode == REQUEST_IMAGE_CAPTURE+2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            edit_iv_board_img3.setImageBitmap(imageBitmap);
            imgStr=getImageUri(this.getApplicationContext(),imageBitmap).toString();
        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            edit_iv_board_img.setImageURI(selectedImageUri);
            imgStr=selectedImageUri.toString();

        }   if (requestCode == GALLERY+1 && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            edit_iv_board_img2.setImageURI(selectedImageUri);
            imgStr2=selectedImageUri.toString();

        }   if (requestCode == GALLERY+2 && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            edit_iv_board_img3.setImageURI(selectedImageUri);
            imgStr3=selectedImageUri.toString();

        }

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){
            //String 으로 받은 Path 값 Uri.parse해서 셋팅.
            String result=data.getStringExtra("0");
            String result2=data.getStringExtra("1");
            String result3=data.getStringExtra("2");

            try {
                edit_iv_board_img.setImageURI(Uri.parse(result));
            }catch (NullPointerException e){
                System.out.println("CreateBoard .setImageURI 1");
            }
            try {
                edit_iv_board_img2.setImageURI(Uri.parse(result2));
            }catch (NullPointerException e){
                System.out.println("CreateBoard .setImageURI 2");
            }
            try {
                edit_iv_board_img3.setImageURI(Uri.parse(result3));
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
        if(requestCode==REQUEST_EDIT_CODE){

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

        Intent intent=getIntent();
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("img1",imgStr);
        intent.putExtra("img2",imgStr2);
        intent.putExtra("img3",imgStr3);

        finish();
        setResult(RESULT_CANCELED,intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void select(int k){
        AlertDialog.Builder ad = new AlertDialog.Builder(BoardEditActivity.this);
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
        AlertDialog.Builder ad = new AlertDialog.Builder(BoardEditActivity.this);
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
                    imgStr=null;
                }else if(i==2){
                    imgStr2=null;
                }else if(i==3){
                    imgStr3=null;
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