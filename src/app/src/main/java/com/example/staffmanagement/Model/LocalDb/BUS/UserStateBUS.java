package com.example.staffmanagement.Model.LocalDb.BUS;

import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;

import java.util.List;

public class UserStateBUS {
    public List<UserState> getAll(){
        return AppDatabase.getDb().userStateDAO().getAll();
    }

    public void insertRange(List<UserState> list){
        AppDatabase.getDb().userStateDAO().insertRange(list);
    }
}
