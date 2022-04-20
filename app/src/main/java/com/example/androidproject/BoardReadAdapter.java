package com.example.androidproject;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardReadAdapter extends RecyclerView.Adapter<BoardReadAdapter.BoardReadViewHolder>{

    private ArrayList<BoardReadItemData> mList;

    public BoardReadAdapter(ArrayList<BoardReadItemData> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public BoardReadAdapter.BoardReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo,parent,false);
        BoardReadViewHolder viewHolder=new BoardReadViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardReadAdapter.BoardReadViewHolder holder, int position) {
        for(int i=0;i<mList.size();i++){
        try {

                holder.img.setImageURI(Uri.parse(mList.get(position).img));

            }catch (NullPointerException e){
            System.out.println("In BoardReadAdapter.onBindViewHolder");
        }


        }

    }
    @Override
    public int getItemCount() {
        return (null!=mList?mList.size():0);
    }

    public class BoardReadViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public BoardReadViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.photo_list);
        }
    }
}
