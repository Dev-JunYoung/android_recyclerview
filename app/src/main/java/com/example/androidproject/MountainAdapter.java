package com.example.androidproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MountainAdapter  extends RecyclerView.Adapter<MountainAdapter.MountainViewHolder>{
    private ArrayList<MountainVO> mList;
    private Context context;
    public final String TAG="MountainAdapter";
    public MountainAdapter(ArrayList<MountainVO> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MountainAdapter.MountainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mountain,parent,false);

        MountainViewHolder viewHolder=new MountainViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MountainAdapter.MountainViewHolder holder, int position) {
        holder.mnt_name.setText(mList.get(position).getMntName());
        holder.mnt_height.setText(mList.get(position).getMntHeight());
        holder.mnt_location.setText(" -"+mList.get(position).getMntLocation().replace(",","\n-"));
    }

    @Override
    public int getItemCount() {
        return (null!=mList?mList.size():0);
    }

    public class MountainViewHolder extends RecyclerView.ViewHolder {
        TextView mnt_name;
        TextView mnt_height;
        TextView mnt_location;
        public MountainViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mnt_name=itemView.findViewById(R.id.mnt_name);
            this.mnt_height=itemView.findViewById(R.id.mnt_height);
            this.mnt_location=itemView.findViewById(R.id.mnt_location);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG,"클릭 ");
                    int pos=getAbsoluteAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.OnItemClick(view,pos);
                        }
                    }
                }
            });
        }

    }

    private RecordAdapter.OnItemClickListener mListener=null;
    public void setOnItemClickListener(RecordAdapter.OnItemClickListener listener){
        this.mListener=listener;
    }
}
