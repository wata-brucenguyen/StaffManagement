package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import androidx.recyclerview.widget.RecyclerView;

public interface CallBackItemTouch {
    void itemTouchOnMove(int oldPosition,int newPosition);
    void onSwipe(int position);
}
