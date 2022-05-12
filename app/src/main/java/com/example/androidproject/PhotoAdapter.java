package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<LookUpViewHolder> {

    // 글라이드로 가져온 이미지 저장 리스트
    private List<Photo> photoList;
    private Context context;

    // pick한 이미지 저장리스트
    private ArrayList<String> mArrayList;


    private SelectionTracker<Long> selectionTracker;


    // 1.
    public PhotoAdapter(Context context, ArrayList<String> mArrayList) {

        this.context = context;
        this.mArrayList = mArrayList;
        //1
        setHasStableIds(true);
        photoList = getPhotoList(context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LookUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_photo, parent, false);

        return new LookUpViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LookUpViewHolder holder, int position) {
        /*for(int i=0;i<photoList.size();i++){
            System.out.println("photoList : "+photoList.get(i));
        }*/
        Photo photo = photoList.get(position);
        holder.setPhoto(photo);
        holder.setSelectionTracker(selectionTracker);

        // 어댑터 포지션 == 아이템아이디
        if (holder.checked) {
            //Add the selected photo.getPath()
            mArrayList.add(photo.getPath());
        }

    }

    @SuppressLint("Range")
    private List<Photo> getPhotoList(Context context) {

       // String[] projection = new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        String[] projection = new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID};

        Cursor cursor = context.getContentResolver()
                .query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
        System.out.println("MediaStore.Images.Media.DATA : " + MediaStore.Images.Media.DATA);
        System.out.println("MediaStore.Images.Media.DATA : " + MediaStore.Images.Media._ID);
        System.out.println("MediaStore.Images.Media.DISPLAY_NAME : " + MediaStore.Images.Media.DISPLAY_NAME);
        System.out.println(" URI : " + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ArrayList<Photo> photoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(projection[0]));
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(projection[1]));
            ;
            Photo photo = new Photo(name, "content://media/external/images/media/"+path);
            photoList.add(photo);
        }//여기


        for (int i = 0; i < photoList.size(); i++) {
            System.out.println(photoList.get(i).getPath());
        }

        cursor.close();


        return photoList;
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    //2. 아이템 지정
    public long getItemId(int position) {
        return position;
    }

    //6. SelectionTracker 를 만들었다면,
    // Adapter 에 setter 를 만들어SelectionTracker 를 넘겨줍니다.
    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }


}
