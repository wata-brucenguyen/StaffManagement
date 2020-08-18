package com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;


import com.example.staffmanagement.MVVM.Model.Entity.Request;

import java.util.List;

public class StaffRequestDiffUtilCallBack extends DiffUtil.Callback {
    private List<Request> newList, oldList;

    public StaffRequestDiffUtilCallBack(List<Request> newList, List<Request> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getId() == oldList.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = oldList.get(oldItemPosition).compareTo(newList.get(newItemPosition));
        return result == 1;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
