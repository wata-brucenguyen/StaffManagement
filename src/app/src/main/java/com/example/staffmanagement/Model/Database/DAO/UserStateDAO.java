package com.example.staffmanagement.Model.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.UserState;
import com.example.staffmanagement.Model.Database.Ultils.ConstString;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserStateDAO extends BaseDAO<UserState> {

    @Insert
    void insertRange(ArrayList<UserState> userStatesList);

    @Query("SELECT COUNT(Id) FROM " + ConstString.ROLE_TABLE_NAME)
    int getCount();

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME + " WHERE " + ConstString.ROLE_COL_ID + " = :Id")
    Role getById(int Id);

    @Query("SELECT * FROM " + ConstString.USER_STATE_TABLE_NAME)
    List<UserState> getAll();
}
