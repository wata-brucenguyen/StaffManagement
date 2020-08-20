package com.example.staffmanagement.Model.LocalDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Ultils.ConstString;

import java.util.List;

@Dao
public interface RoleDAO extends BaseDAO<Role> {

    @Insert
    void insertRange(List<Role> roleList);

    @Query("SELECT COUNT(Id) FROM " + ConstString.ROLE_TABLE_NAME)
    int getCount();

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME + " WHERE " + ConstString.ROLE_COL_ID + " = :Id")
    Role getById(int Id);

    @Query("SELECT * FROM " + ConstString.ROLE_TABLE_NAME)
    List<Role> getAll();

    @Query("DELETE FROM " + ConstString.ROLE_TABLE_NAME)
    void deleteAll();

    @Query("SELECT COUNT(Id) FROM " + ConstString.ROLE_TABLE_NAME)
    int count();
}
