package com.example.androidproject;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private OnItemTouchListener listener;
    private RecordAdapter mAdapter;
    public CustomItemTouchHelperCallback(OnItemTouchListener listener) {
        this.listener = listener;
    }




    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START|ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.moveItem(viewHolder.getAbsoluteAdapterPosition(),target.getAbsoluteAdapterPosition());
    }

    @Override//스와이프리무브
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            listener.removeItem(viewHolder.getAbsoluteAdapterPosition());
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState==ItemTouchHelper.ACTION_STATE_DRAG){
            viewHolder.itemView.setBackgroundColor(Color.GRAY);
        }
    }




    public interface OnItemTouchListener{
        boolean check();
        boolean moveItem(int fromPosition,int toPosition);
        void removeItem(int position);
    }



}
