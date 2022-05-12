package com.example.androidproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private ArrayList<BoardItemData> mList;
    private Context context;



    public class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;
        TextView time;
        TextView name;


        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title=(TextView)itemView.findViewById(R.id.board_list_title);
            this.time=(TextView) itemView.findViewById(R.id.board_list_time);
            this.name=(TextView) itemView.findViewById(R.id.board_list_name);
            this.img=(ImageView) itemView.findViewById(R.id.board_list_img);

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
        }
    }

    public BoardAdapter(ArrayList<BoardItemData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board,parent,false);

        BoardViewHolder viewHolder=new BoardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.time.setText(mList.get(position).getTime());

       /* for(int i=0;i<mList.size();i++){
            holder.img.setImageURI(Uri.parse(mList.get(position).getImgList().get(0)));
        }*/



        for (int i=0; i<mList.get(position).getImgList().size();i++){
            try{ //setImage가 되어있는지 확인
                // 이미지리스트의 사이즈를 활용해서할것인지.
                if(mList.get(position).getImgList().get(i)!=null){
                    holder.img.setImageURI(Uri.parse(mList.get(position).getImgList().get(i)));
                }


            }
            catch (NullPointerException e){
            }
        }

   /*     try{
            holder.img.setImageURI(Uri.parse(mList.get(position).getImgList().get(0)));
            holder.img.setImageURI(Uri.parse(mList.get(position).getImgList().get(1)));
            holder.img.setImageURI(Uri.parse(mList.get(position).getImgList().get(2)));
        }catch (NullPointerException e){
            System.out.println("BoardAdapter Null");
        }*/


        holder.name.setText(mList.get(position).getName());
        //System.out.println("뷰홀더 이미지 스트링 : "+mList.get(position).getImg());




    }

    @Override
    public int getItemCount() {
        return (null!=mList?mList.size():0);
    }


    public interface OnItemClickListener{
        void OnItemClick(View v,int pos);
    }
    private RecordAdapter.OnItemClickListener mListener=null;
    public void setOnItemClickListener(RecordAdapter.OnItemClickListener listener){
        this.mListener=listener;
    }



}
