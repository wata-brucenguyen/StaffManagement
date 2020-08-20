package com.example.staffmanagement.MVVM.Model.LocalDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Ultils.ConstString;

import java.util.List;

@Dao
public interface RequestDAO extends BaseDAO<Request> {


    @RawQuery(observedEntities = Request.class)
    int getCountRequest(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    int getCountWaitingForUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    List<Request> getLimitListRequestForUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    List<Request> getLimitListRequestForUserInStaff(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    Request getById(SupportSQLiteQuery query);

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME)
    List<Request> getAll();

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " LIMIT 0,12")
    LiveData<List<Request>> init0();

    @RawQuery(observedEntities = Request.class)
    LiveData<List<Request>> getLimitListRequestForUserInStaffLD(SupportSQLiteQuery query);

    @Insert
    void insertRange(List<Request> items);

    @Query("DELETE FROM " + ConstString.REQUEST_TABLE_NAME)
    void deleteAll();

    @Query("SELECT COUNT(Id) FROM " + ConstString.REQUEST_TABLE_NAME)
    int count();

}
