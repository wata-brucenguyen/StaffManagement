package com.example.staffmanagement.Model.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.UserState;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Dao
public interface UserStateDAO extends BaseDAO<UserState> {

    @Insert
    public void insertRange(ArrayList<UserState> userStatesList);

    @Query("SELECT COUNT(Id) FROM " + ConstString.ROLE_TABLE_NAME)
    public int getCount();

    @Query("SELECT * " + ConstString.ROLE_TABLE_NAME + " WHERE " + ConstString.ROLE_COL_ID + " = :Id")
    public Role getById(int Id);

    @Query("SELECT * FROM " + ConstString.USER_STATE_COL_NAME)
    ArrayList<UserState> getAll();
}
