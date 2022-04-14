package com.example.androidproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {

    Toolbar toolbar;

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_CODE_PERMISSION = 0;

    RecyclerView recyclerView;
    PhotoAdapter photoAdapter;
    //이미지 Path 저장 리스트
    ArrayList<String> mArrayList= new ArrayList<>();
    SelectionTracker<Long> selectionTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE_PERMISSION );
      /*  toolbar=findViewById(R.id.toolbar);

        System.out.println("툻바 : "+toolbar);
        *//*toolbar.inflateMenu(R.menu.normal_menu);
        toolbar.inflateMenu(R.menu.add_photo_menu);*//*
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Photo");*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_photo_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.add_photo){
           Intent intent=new Intent(PhotoActivity.this,CreateBoardActivity.class);
           //반복문 활용
            //이미지 Path 저장된 mArrayList의 인덱스값을 모두 인텐트에 담는다.
           for(int i=0;i<mArrayList.size();i++){
               intent.putExtra(String.valueOf(i),mArrayList.get(i));
           }

           setResult(RESULT_OK,intent);
           finish();
        }


        return super.onOptionsItemSelected(item);
    }
    /* selection methods --------------------------------------------------------------------------------------------------*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE_PERMISSION){
            for(int grantResult :grantResults){
                if(grantResult == PackageManager.PERMISSION_DENIED){
                    finish();
                    return;
                }
            }
            setupUI();
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_photo);
        //리사이클러뷰 연결
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //어댑터 생성
        photoAdapter = new PhotoAdapter(this,mArrayList);
        //리사이클러뷰에 어댑터 set
        recyclerView.setAdapter(photoAdapter);
        //리사이클러뷰 레이아웃 set
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        setupSelectionTracker();
        photoAdapter.setSelectionTracker(selectionTracker);
    }


    //5 SelectionTracker 만들기
    //Warning : SelectionTracker 는 반드시 RecyclerView 에
    // Adapter 가 참조된 이후에 만들어야 합니다.
    // 그렇지 않으면 IllegalArgumentException 이 발생됩니다.
/*selectionId : 선택내용 대한 Id를 지정합니다.
recyclerView : 선택내용을 추적할 RecyclerView 를 지정합니다.
keyProvider : 캐시를 위한 선택되는 아이템의 Key 제공자
itemDetailsLookup : RecyclerView 아이템의 대한 정보
storage : saved state 에서 키를 저장하기 위한 전략*/
    private void setupSelectionTracker(){
        selectionTracker = new SelectionTracker.Builder<>(
                "selection_id",
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new PhotoDetailsLookUp(recyclerView),
                StorageStrategy.createLongStorage())
                //어떠한 선택을 할지 결정할 수 있게 합니다. Selection 라이브러리에서
                // 제공하는 SelectionPredicates.createSelectionAnything()을 쓸 수도 있고,
                // 자신만의 SelectionPredicate 를 만들 수도 있습니다.
                .withSelectionPredicate(SelectionPredicates.<Long>createSelectAnything())
                .build();
    }



}