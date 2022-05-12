package com.example.androidproject;

import static com.example.androidproject.BoardActivity.REQUEST_EDIT_CODE;
import static com.example.androidproject.BoardActivity.RESULT_DELETE_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardReadActivity extends AppCompatActivity {
//내게시물
    TextView tv_board_content,tv_board_title;
    //리팩터
    //ImageView iv_board_img;
    Button btn_back;
    private static final int REQUEST_CODE=777;
    Intent cancle;
    String title,content,img1,img2,img3;

    String imgStr1,imgStr2,imgStr3;

    String editImgStr,editImgStr2,editImgStr3;
    String[] getBoardImg;

    String photo;

    boolean editCheck=false;

    static int RAEDandEDIT=0;

    //리사이클러뷰 사용 준비
    ArrayList<BoardReadItemData> mArrayList;
    private BoardReadAdapter mAdapter;
    private String splitPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_read);

        tv_board_content=findViewById(R.id.tv_board_content);
        tv_board_title=findViewById(R.id.tv_board_title);

        //리팩터
        //iv_board_img=findViewById(R.id.iv_board_img);
        btn_back=findViewById(R.id.btn_back);

        //리팩터
        //iv_board_img.setImageURI(Uri.parse(img));

        //여기에 리사이클러뷰를 생성해야함.
        RecyclerView mRecyclerView=(RecyclerView) findViewById(R.id.list_read_img);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(
                this,RecyclerView.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //가로 사진 리사이클러뷰
        mArrayList=new ArrayList<BoardReadItemData>();

        mAdapter=new BoardReadAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //스트링으로 보냈으니, 스트링으로 받아야 된다.
        Intent intent=getIntent();
        /* cancle =intent;*/
        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");

        photo=intent.getStringExtra("photo");
        splitPhoto=photo.substring(1,photo.length()-1);
        String[] a=splitPhoto.split(", ");


        BoardReadItemData itemData = null;
        //null 이 아닐때에 만 리스트추가 //리스트는 photo 리사이클러뷰
        for(int i=0;i<a.length;i++){
            if(a[i]!=null){
                itemData=new BoardReadItemData(a[i]);
                mArrayList.add(itemData);
            }
        }

        tv_board_title.setText(title);
        tv_board_content.setText(content);


        mAdapter.notifyDataSetChanged();
        //뒤로가기
        BoardReadItemData finalItemData = itemData;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BoardReadActivity.this,BoardActivity.class);
                if(tv_board_title!=null && tv_board_content!=null  ){
                    intent.putExtra("title",tv_board_title.getText().toString());
                    intent.putExtra("content",tv_board_content.getText().toString());


                    if(editCheck==true){
                        System.out.println("BoardRead In editCheck return");

                        intent.putExtra("editImgStr", editImgStr);
                        intent.putExtra("editImgStr2", editImgStr2);
                        intent.putExtra("editImgStr3", editImgStr3);
                        intent.putExtra("edit", "edit");
                        System.out.println(editImgStr);
                        System.out.println(editImgStr2);
                        System.out.println(editImgStr3);
                        setResult(REQUEST_EDIT_CODE,intent);
                        finish();
                        editCheck=false;
                    }else {
                        System.out.println("read return");
                        intent.putExtra("photo", splitPhoto);
                        String str="edit";
                        intent.putExtra("flag",str);
                        setResult(RESULT_OK,intent);
                        finish();
                    }


                }else {
                }
            }
        });
    } //onCreate

    @Override //menu 버튼 객체화
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.board_rud, menu);
        return true;
    }

    @Override //menu 버튼 클릭 choose
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                Intent intent=new Intent(BoardReadActivity.this,BoardEditActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                intent.putExtra("photo",splitPhoto);
                startActivityForResult(intent,REQUEST_EDIT_CODE);
                break;
            case R.id.delete:
                Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show();
                Intent intent2=getIntent();
                setResult(RESULT_DELETE_CODE,intent2);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("리퀘스트코드"+requestCode);
        //뒤로가기 result==0; 
        System.out.println("리절트코드"+resultCode);

        if(resultCode== RESULT_OK){
            Toast.makeText(getApplicationContext(), "수신 성공", Toast.LENGTH_SHORT).show();
            
            if(requestCode==REQUEST_EDIT_CODE){
                editCheck=true;
                System.out.println("REQUEST_EDIT_CODE"+REQUEST_EDIT_CODE);
                RAEDandEDIT=1;
                tv_board_title.setText("");
                tv_board_content.setText("");
                //iv_board_img.setImageURI(Uri.parse(""));
                title=data.getStringExtra("title");
                content=data.getStringExtra("content");
                //수정을 위한 삭제 ( 기존 데이터 삭제 -> 생성 데이터 삽입 )
                mArrayList.clear();
                System.out.println("수정 전 삭제 확인 mArrayList : "+ mArrayList);
                //변경된 값 받음.
                editImgStr=data.getStringExtra("img");
                editImgStr2=data.getStringExtra("img2");
                editImgStr3=data.getStringExtra("img3");

                //수정하면서 받은것.
                System.out.println("editImgStr : "+editImgStr);
                System.out.println("editImgStr2 : "+editImgStr2);
                System.out.println("editImgStr3 : "+editImgStr3);

                //생성될떄 받았던것
                System.out.println("splitPhoto : "+splitPhoto);
                if(editImgStr!=null){
                    BoardReadItemData itemData=new BoardReadItemData(editImgStr);
                    mArrayList.add(itemData);
                }else {
                    System.out.println("이미지널");
                }
                if(editImgStr2!=null){
                    BoardReadItemData itemData=new BoardReadItemData(editImgStr2);
                    mArrayList.add(itemData);
                }else {
                    System.out.println("이미지널2");
                }
                if(editImgStr3!=null){
                    BoardReadItemData itemData=new BoardReadItemData(editImgStr3);
                    mArrayList.add(itemData);
                }else {
                    System.out.println("이미지널3");
                }

                //하나 들어 간거 확인
                System.out.println("mArrayList : "+mArrayList);



                mAdapter.notifyDataSetChanged();

                try {
                tv_board_title.setText(title);
                }catch (NullPointerException e){
                    System.out.println("BoardReadActivity onActivityResult title nullPointException");
                }
                try {
                    tv_board_content.setText(content);
                }catch (NullPointerException e){
                    System.out.println("BoardReadActivity onActivityResult content nullPointException");
                }

                try {
                    //iv_board_img.setImageURI(Uri.parse(img));

                } catch (NullPointerException e){
                    System.out.println("BoardReadActivity onActivityResult img nullPointException");
                }

            }

        }else {

        }

    }
    public void onBackPressed() {
        Intent intent=new Intent(BoardReadActivity.this,BoardEnjoyActivity.class);
        //if(tv_board_title!=null && tv_board_content!=null && img1!=null ) {}
            System.out.println("onBackPressed");
            intent.putExtra("title", tv_board_title.getText().toString());
            intent.putExtra("content", tv_board_content.getText().toString());
            intent.putExtra("img1",img1);
            intent.putExtra("img2",img2);
            intent.putExtra("img3",img3);
            String str = "str";
            intent.putExtra("flag", str);
            setResult(RESULT_CANCELED, intent);
            finish();
    }
}