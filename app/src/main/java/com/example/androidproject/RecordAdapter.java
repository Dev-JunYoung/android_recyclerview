package com.example.androidproject;

import static com.example.androidproject.RecordActivity.식별;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> implements CustomItemTouchHelperCallback.OnItemTouchListener{
    private ArrayList<RecordItemData> mList;
    private RecordActivity recordActivity;


    @Override
    public boolean check() {
        return false;
    }

    @Override
    public boolean moveItem(int fromPosition, int toPosition) {
        RecordItemData data=mList.get(fromPosition);
        mList.remove(fromPosition);
        mList.add(toPosition,data);
        notifyItemMoved(fromPosition,toPosition);

        return false;
    }

    @Override
    public void removeItem(int position) {
        식별=position;
        mList.get(position).setPosi(position);
        System.out.println(식별);
        recordActivity.removeItem(position);

      
        notifyDataSetChanged();
    }

    public void RemoveItem(ArrayList<RecordItemData> selectionList) {
        for(int i=0;i<selectionList.size();i++){
            // Remove 선택된 인덱스값
            mList.remove(selectionList.get(i));
            notifyDataSetChanged();
        }
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        protected TextView distance;
        protected TextView time;
        protected TextView height;
        protected TextView step;
        protected TextView maxHeight;
        protected TextView minHeight;
        protected TextView avg;
        protected TextView recordTime;
        protected CheckBox checkbox;
        protected View view;
   

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            this.distance=(TextView) itemView.findViewById(R.id.list_distance);
            this.time=(TextView) itemView.findViewById(R.id.list_time);
            this.height=(TextView) itemView.findViewById(R.id.list_height);
            this.step=(TextView) itemView.findViewById(R.id.list_step);
            this.recordTime=(TextView) itemView.findViewById(R.id.list_record_time);
            checkbox=(CheckBox) itemView.findViewById(R.id.checkbox);

            view=itemView;
            view.setOnLongClickListener(recordActivity);
            checkbox.setOnClickListener(this);
          

            //READ record
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAbsoluteAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.OnItemClick(view,pos);
                        }
                    }
                }
            });
            this.maxHeight=(TextView) itemView.findViewById(R.id.et_avg);
            this.minHeight=(TextView) itemView.findViewById(R.id.et_avg);
            this.avg=(TextView) itemView.findViewById(R.id.et_avg);

        }
        @Override //체크박스 클릭시, 아이템 위치값 저장
        public void onClick(View view) {
            recordActivity.MakeSelection(view,getAbsoluteAdapterPosition());

        }

    }

    public RecordAdapter(ArrayList<RecordItemData> mList, RecordActivity recordActivity) {
        this.mList = mList;
        this.recordActivity = recordActivity;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record,parent,false);

        RecordViewHolder viewHolder=new RecordViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.distance.setText(mList.get(position).getDistance());
        holder.time.setText(mList.get(position).getTime());
        holder.height.setText(mList.get(position).getHeight());
        holder.step.setText(mList.get(position).getStep());
        holder.recordTime.setText(mList.get(position).getTimestamp());


        //holder.checkbox.setVisibility(View.VISIBLE);
        if(!recordActivity.isContexualModeEnable){
            holder.checkbox.setVisibility(View.GONE);
        }else {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        return (null!=mList?mList.size():0);
    }


    public interface OnItemClickListener{
        void OnItemClick(View v,int pos);
        void OnDeleteClick(View v,int pos);

    }


    private OnItemClickListener mListener=null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

}

