package com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity;

import androidx.recyclerview.widget.RecyclerView;

public interface CallBackItemTouch {
    void itemTouchOnMove(int oldPosition,int newPosition);
    void onSwipe(RecyclerView.ViewHolder viewHolder, int position);
    boolean checkStateRequest(RecyclerView.ViewHolder viewHolder);
}
