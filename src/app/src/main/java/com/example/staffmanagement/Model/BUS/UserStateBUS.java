package com.example.staffmanagement.Model.BUS;

import com.example.staffmanagement.Model.Database.Entity.UserState;

import java.util.List;

public class UserStateBUS {
    public List<UserState> getAll(){
        return AppDatabase.getDb().userStateDAO().getAll();
    }
}
