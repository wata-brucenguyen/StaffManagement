package com.example.staffmanagement.Model.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;

@Dao
public interface StateRequestDAO extends BaseDAO<StateRequest> {

    @Insert
    public void insertRange(StateRequest... stateRequests);

    @Query("SELECT COUNT(*) as numRows FROM " + ConstString.STATE_REQUEST_TABLE_NAME)
    public int getCount();

    @Query("SELECT COUNT(*) as numRows FROM " + ConstString.STATE_REQUEST_TABLE_NAME + " WHERE " + ConstString.STATE_REQUEST_COL_ID + " = :Id")
    public Role getById(int Id);
}
