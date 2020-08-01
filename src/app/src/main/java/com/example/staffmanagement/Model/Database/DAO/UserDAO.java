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
import java.util.List;

@Dao
public interface UserDAO extends BaseDAO<User>{

    @Insert
    public void insertRange(ArrayList<User> userList);

    @Query("SELECT COUNT(" + ConstString.USER_COL_ID +") FROM " + ConstString.USER_TABLE_NAME)
    public int getCount();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME)
    public List<User> getAll();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID +
            " = :idUser AND " + ConstString.USER_COL_FULL_NAME + " LIKE :name ")
    public List<User> findRequestByFullName(int idUser, String name);

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME)
    public List<Role> getAllRole();


    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = :id ")
    User getById(int id);

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE "
            + ConstString.USER_COL_USERNAME + " = :userName ")
    User getUserByUserName(String userName);

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + ":query");
    List<User> getLimitListForUser(String query);
}
