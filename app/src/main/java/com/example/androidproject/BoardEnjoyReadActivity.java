package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardEnjoyReadActivity extends AppCompatActivity {

    TextView tv_board_content,tv_board_title;
    //리팩터
    //ImageView iv_board_img;
    Button btn_back;
    private static final int REQUEST_CODE=777;
    String title,content,time,name;
    String imgStr1,imgStr2,imgStr3;

    String editImgStr,editImgStr2,editImgStr3;
    String[] getBoardImg;

    String photo;

    boolean editCheck=false;

    static int RAEDandEDIT=0;

    Context context ;







    //리사이클러뷰 사용 준비
    ArrayList<BoardReadItemData> mArrayList;
    private BoardReadAdapter mAdapter;
    private String splitPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_enjoy_read);

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
        time=intent.getStringExtra("time");
        name=intent.getStringExtra("name");

        System.out.println(title);
        System.out.println(content);
        System.out.println(photo);
        System.out.println(time);
        System.out.println(name);

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
        System.out.println(mArrayList);
        tv_board_title.setText(title);
        tv_board_content.setText(content);


        mAdapter.notifyDataSetChanged();
        //뒤로가기
        BoardReadItemData finalItemData = itemData;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BoardEnjoyReadActivity.this,BoardEnjoyActivity.class);
                        String str="read";
                        intent.putExtra("flag",str);
                        setResult(RESULT_CANCELED,intent);
                        finish();
            }
        });
    } //onCreate
    @Override //menu 버튼 객체화
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.board_enjoy, menu);
        return true;
    }
    @Override //menu 버튼 클릭 choose
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.enjoy:

                Intent intent=new Intent(BoardEnjoyReadActivity.this,BoardEnjoyActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                intent.putExtra("photo",photo);
                intent.putExtra("time",time);
                intent.putExtra("name",name);
                setResult(RESULT_OK,intent);
                finish();
//                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }




}
