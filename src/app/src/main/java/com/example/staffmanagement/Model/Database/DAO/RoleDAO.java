package com.example.staffmanagement.Model.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RoleDAO extends BaseDAO<Role> {

    @Insert
    public void insertRange(ArrayList<Role> roleList);

    @Query("SELECT COUNT(Id) FROM " + ConstString.ROLE_TABLE_NAME)
    public int getCount();

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME + " WHERE " + ConstString.ROLE_COL_ID + " = :Id")
    public Role getById(int Id);

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME)
    List<Role> getAll();

}
