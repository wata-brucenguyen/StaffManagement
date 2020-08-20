package com.example.staffmanagement.View.Admin.UserRequestActivity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.staffmanagement.Model.Entity.Request;

import java.util.List;

public class UserRequestDiffUtilCallback extends DiffUtil.Callback {
    List<Request> oldRequestList, newRequestList;

    public UserRequestDiffUtilCallback(List<Request> oldRequest, List<Request> newRequest) {
        this.oldRequestList = oldRequest;
        this.newRequestList = newRequest;
    }

    @Override
    public int getOldListSize() {
        return oldRequestList.size();
    }

    @Override
    public int getNewListSize() {
        return newRequestList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRequestList.get(oldItemPosition).getId() == newRequestList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = oldRequestList.get(oldItemPosition).compareTo(newRequestList.get(newItemPosition));
        return result == 1;
    }

}
