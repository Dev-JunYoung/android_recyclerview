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

    TextView tv_board_content,tv_board_title;
    //리팩터
    //ImageView iv_board_img;
    Button btn_back;
    private static final int REQUEST_CODE=777;
    Intent cancle;
    String title,content,img1,img2,img3;

    String imgStr1,imgStr2,imgStr3;
    //리사이클러뷰 사용 준비
    ArrayList<BoardReadItemData> mArrayList;
    private BoardReadAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_read);

        tv_board_content=findViewById(R.id.tv_board_content);
        tv_board_title=findViewById(R.id.tv_board_title);

        //리팩터
        //iv_board_img=findViewById(R.id.iv_board_img);
        btn_back=findViewById(R.id.btn_back);






        Intent intent=getIntent();
        cancle =intent;
        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");



//리팩터        img=intent.getStringExtra("img");



        //


        tv_board_title.setText(title);
        tv_board_content.setText(content);

        //리팩터
        //iv_board_img.setImageURI(Uri.parse(img));

        //여기에 리사이클러뷰를 생성해야함.
        RecyclerView mRecyclerView=(RecyclerView) findViewById(R.id.list_read_img);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(
                this,RecyclerView.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList=new ArrayList<BoardReadItemData>();
        mAdapter=new BoardReadAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //스트링으로 보냈으니, 스트링으로 받아야 된다.

        Intent intent1=getIntent();
        imgStr1=intent1.getStringExtra("img1");
        imgStr2=intent1.getStringExtra("img2");
        imgStr3=intent1.getStringExtra("img3");
/**/

        System.out.println("READ인텐트 받은 ImgPath"+imgStr1);
        System.out.println("READ인텐트 받은 ImgPath"+imgStr2);
        System.out.println("READ인텐트 받은 ImgPath"+imgStr3);

        BoardReadItemData itemdata=new BoardReadItemData(imgStr1);
        BoardReadItemData itemdata2=new BoardReadItemData(imgStr2);
        BoardReadItemData itemdata3=new BoardReadItemData(imgStr3);

        mArrayList.add(itemdata);
        mArrayList.add(itemdata2);
        mArrayList.add(itemdata3);

        mAdapter.notifyDataSetChanged();
        //뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BoardReadActivity.this,BoardActivity.class);
                if(tv_board_title!=null && tv_board_content!=null  ){
                    intent.putExtra("title",tv_board_title.getText().toString());
                    intent.putExtra("content",tv_board_content.getText().toString());

                    intent.putExtra("img1",imgStr1);
                    intent.putExtra("img2",imgStr2);
                    intent.putExtra("img3",imgStr3);

                    String str="str";
                    intent.putExtra("flag",str);
                    setResult(RESULT_OK,intent);
                    finish();
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



                intent.putExtra("img1",imgStr1);
                intent.putExtra("img2",imgStr2);
                intent.putExtra("img3",imgStr3);



                startActivityForResult(intent,REQUEST_EDIT_CODE);


                break;
            case R.id.delete:
                Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show();
                Intent intent2=getIntent();
                setResult(RESULT_DELETE_CODE,cancle);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            Toast.makeText(getApplicationContext(), "수신 성공", Toast.LENGTH_SHORT).show();
            if(requestCode==REQUEST_EDIT_CODE){
                tv_board_title.setText("");
                tv_board_content.setText("");
                //iv_board_img.setImageURI(Uri.parse(""));
                title=data.getStringExtra("title");
                content=data.getStringExtra("content");


                img1=data.getStringExtra("img1");

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
        Intent intent=new Intent(BoardReadActivity.this,BoardActivity.class);
        //if(tv_board_title!=null && tv_board_content!=null && img1!=null ) {}
            System.out.println("onBackPressed");
            intent.putExtra("title", tv_board_title.getText().toString());
            intent.putExtra("content", tv_board_content.getText().toString());

        intent.putExtra("img1",imgStr1);
        intent.putExtra("img2",imgStr2);
        intent.putExtra("img3",imgStr3);
            String str = "str";
            intent.putExtra("flag", str);
            setResult(RESULT_OK, intent);
            finish();


    }
}