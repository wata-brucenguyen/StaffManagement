package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import com.example.staffmanagement.Model.Database.Entity.Role;

import java.util.List;

public class RoleBUS {

    public String getRoleNameById(Context context, int id){
        Role role = AppDatabase.getDb().roleDAO().getById(id);
        return role.getName();
    }

    public List<Role> getAll(){
        return AppDatabase.getDb().roleDAO().getAll();
    }
}
