package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class StaffRequestItemTouchHelper extends ItemTouchHelper.Callback {

    private CallBackItemTouch callBackItemTouch;

    public StaffRequestItemTouchHelper(CallBackItemTouch callBackItemTouch) {
        this.callBackItemTouch = callBackItemTouch;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.START;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        callBackItemTouch.itemTouchOnMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        callBackItemTouch.onSwipe(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        else {
            View foreground = ((StaffRequestListAdapter.ViewHolder)viewHolder).getViewBackground();
            getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if(actionState != ItemTouchHelper.ACTION_STATE_DRAG){
            View foreground = ((StaffRequestListAdapter.ViewHolder)viewHolder).getViewForeground();
            getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //super.clearView(recyclerView, viewHolder);
        View foreground = ((StaffRequestListAdapter.ViewHolder)viewHolder).getViewForeground();
        foreground.setBackgroundColor(Color.WHITE);
        getDefaultUIUtil().clearView(foreground);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(viewHolder != null){
            View viewForeground =  ((StaffRequestListAdapter.ViewHolder)viewHolder).getViewForeground();
            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                viewForeground.setBackgroundColor(Color.LTGRAY);
            }
            getDefaultUIUtil().onSelected(viewForeground);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
