package com.example.staffmanagement.Model.Database.DAO;

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
public interface UserDAO {

    @Insert
    public void initialize(User... user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(User user);

    @Update
    public void update(User user);

    @Delete
    public void delete(int id);

    @Query("SELECT COUNT(" + ConstString.USER_COL_ID +") AS numRows FROM " + ConstString.USER_TABLE_NAME)
    public int getCount();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME)
    public ArrayList<User> getAll();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID +
            " = :idUser AND " + ConstString.USER_COL_FULL_NAME + " LIKE :name ")
    public ArrayList<User> findRequestByFullName(int idUser, String name);

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME)
    public ArrayList<Role> getAllRole();

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = :id ")
    public User getById(int id);

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE "
            + ConstString.USER_COL_USERNAME + " = :userName AND "+ ConstString.USER_COL_PASSWORD + " =:password ")
    public User getByLoginInformation(String userName, String password);

}
