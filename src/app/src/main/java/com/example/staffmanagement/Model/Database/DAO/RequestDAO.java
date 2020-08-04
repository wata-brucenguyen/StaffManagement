package com.example.staffmanagement.Model.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Request;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RequestDAO extends BaseDAO<Request> {

    @RawQuery(observedEntities = Request.class)
    Request updateStateRequest(SupportSQLiteQuery sql);

    @RawQuery(observedEntities = Request.class)
    int getCountRequest(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    int getCountWaitingForUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    List<Request> getRequestForUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    LiveData<List<Request>> getLimitListRequestForUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    String getTitleById(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    long getDateTimeById(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    int getIdStateById(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Request.class)
    Request getById(SupportSQLiteQuery query);

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME)
    List<Request> getAll();

    @Insert
    void insertRange(ArrayList<Request> items);

}
