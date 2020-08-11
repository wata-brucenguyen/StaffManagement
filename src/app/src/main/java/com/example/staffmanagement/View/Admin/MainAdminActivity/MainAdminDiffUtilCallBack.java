package com.example.staffmanagement.View.Admin.MainAdminActivity;

import androidx.recyclerview.widget.DiffUtil;

import com.example.staffmanagement.Model.Database.Entity.User;

import java.util.List;

public class MainAdminDiffUtilCallBack extends DiffUtil.Callback {
    private List<User> newList,oldList;

    public MainAdminDiffUtilCallBack(List<User> newList, List<User> oldList) {
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
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result =  oldList.get(oldItemPosition).compareTo(newList.get(newItemPosition));
        return result == 1;
    }
}
