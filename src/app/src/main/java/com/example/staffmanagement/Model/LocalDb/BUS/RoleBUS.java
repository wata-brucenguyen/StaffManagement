package com.example.staffmanagement.Model.LocalDb.BUS;

import android.content.Context;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;

import java.util.List;

public class RoleBUS {

    public String getRoleNameById(Context context, int id){
        Role role = AppDatabase.getDb().roleDAO().getById(id);
        return role.getName();
    }

    public List<Role> getAll(){
        return AppDatabase.getDb().roleDAO().getAll();
    }

    public void insertRange(List<Role> list){
        AppDatabase.getDb().roleDAO().insertRange(list);
    }


}
