package com.example.staffmanagement.Model.LocalDb.BUS;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;

import java.util.List;

public class UserStateBUS {
    public List<UserState> getAll(){
        return AppDatabase.getDb().userStateDAO().getAll();
    }

    public void insertRange(List<UserState> list){
        AppDatabase.getDb().userStateDAO().insertRange(list);
    }
}
