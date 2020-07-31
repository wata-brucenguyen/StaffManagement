package com.example.staffmanagement.Model.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;

@Dao
public interface RoleDAO extends BaseDAO<Role> {

    @Insert
    public void insertRange(Role... roles);

    @Query("SELECT COUNT(*) as numRows FROM " + ConstString.ROLE_TABLE_NAME)
    public int getCount();

    @Query("SELECT COUNT(*) as numRows FROM " + ConstString.ROLE_TABLE_NAME + " WHERE " + ConstString.ROLE_COL_ID + " = :Id")
    public Role getById(int Id);
}
