package com.example.staffmanagement.Model.LocalDb.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.Request;
import com.example.staffmanagement.Model.LocalDb.Database.Ultils.ConstString;

import java.util.ArrayList;
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

    @Insert
    void insertRange(List<Request> items);

}
