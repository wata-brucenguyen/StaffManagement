package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import com.example.staffmanagement.Model.Database.Entity.Role;

public class RoleBUS {

    public String getRoleNameById(Context context, int id){
        AppDatabase database = AppDatabase.getInstance(context);
        Role role = database.roleDAO().getById(id);
        AppDatabase.onDestroy();
        return role.getName();
    }
}
