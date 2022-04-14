package com.example.androidproject;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

//3 아이템 디테일 룩업 구현
//ItemDetailsLookup<T> 클래스는 사용자의 선택과 관련된 항목에 대한 정보를 제공합니다.
// MotionEvent를 기반하여 선택된 내용을 ViewHolder에 매핑합니다.
public class PhotoDetailsLookUp extends ItemDetailsLookup<Long> {

    private RecyclerView recyclerView;
    public PhotoDetailsLookUp(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }
    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent motionEvent) {
        View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (view != null) {
            LookUpViewHolder viewHolder = (LookUpViewHolder) recyclerView.getChildViewHolder(view);
            return viewHolder.getItemDetails();
        }
        return null;
    }
}
