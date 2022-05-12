package com.example.androidproject;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class LookUpViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    ImageView checkBox;
    Boolean checked;
    public LookUpViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        checkBox = itemView.findViewById(R.id.checkbox);
        //생성될때 체크값 false
        this.checked=false;
    }


    //4
    //. 추상클래스인 ItemDetails 에는 getPosition 과 getSelectionKey 라는 메소드를 구현하게끔 되어있는데,
    // 위와 같이 adapter 의 position 과 item id를 리턴시켜줍니다. (리턴 한 다음->SelectionTracker 만들기)
    public ItemDetailsLookup.ItemDetails<Long> getItemDetails(){
        return new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                return getItemId();
            }

            @Override
            public boolean inSelectionHotspot(@NonNull MotionEvent e) {
                return true;
            }
        };
    }
    // glide 기본이미지 로딩
    public void setPhoto(Photo photo) {
            //with(context) //load(String url)
        Glide.with(image).load(photo.getPath()).transition(DrawableTransitionOptions.withCrossFade()).
                //into(ImageView targetImageView) : 다운로드 받은 이미지를 보여줄 이미지 뷰
                into(image);
        //갤러리에 있는 모든 이미지 uri
        //System.out.println("photo.getPath() : "+photo.getPath());
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        if(selectionTracker!=null && selectionTracker.isSelected((long) getAbsoluteAdapterPosition())){
            checkBox.setImageResource(android.R.drawable.checkbox_on_background);
            this.checked=true;
        }else{
            checkBox.setImageResource(android.R.drawable.checkbox_off_background);
            this.checked=false;
        }
    }

}
