package com.example.staffmanagement.Model.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;

import java.util.ArrayList;

@Dao
public interface UserDAO extends BaseDAO<User>{

    @Insert
    void initialize(User... user);

    @Query("SELECT COUNT(" + ConstString.USER_COL_ID +") AS numRows FROM " + ConstString.USER_TABLE_NAME)
    int getCount();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME)
    LiveData<ArrayList<User>> getAll();

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME)
    LiveData<ArrayList<Role>> getAllRole();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = :id ")
    User getById(int id);

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE "
            + ConstString.USER_COL_USERNAME + " = :userName ")
    User getUserByUserName(String userName);

}
