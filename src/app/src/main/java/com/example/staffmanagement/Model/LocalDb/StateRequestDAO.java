package com.example.staffmanagement.Model.LocalDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Ultils.ConstString;

import java.util.List;

@Dao
public interface StateRequestDAO extends BaseDAO<StateRequest> {

    @Insert
    void insertRange(List<StateRequest> requestList);

    @Query("SELECT COUNT(*) FROM " + ConstString.STATE_REQUEST_TABLE_NAME)
    int getCount();

    @Query("SELECT * FROM " + ConstString.STATE_REQUEST_TABLE_NAME + " WHERE " + ConstString.STATE_REQUEST_COL_ID + " = :Id")
    StateRequest getById(int Id);

    @Query("SELECT * FROM " + ConstString.STATE_REQUEST_TABLE_NAME)
    List<StateRequest> getAll();

    @RawQuery(observedEntities = StateRequest.class)
    int getIdStateByName(SupportSQLiteQuery query);

    @RawQuery(observedEntities = StateRequest.class)
    String getStateNameById(SupportSQLiteQuery query);

    @Query("DELETE FROM " + ConstString.STATE_REQUEST_TABLE_NAME)
    void deleteAll();

    @Query("SELECT COUNT(Id) FROM " + ConstString.STATE_REQUEST_TABLE_NAME)
    int count();

}
