package com.example.staffmanagement.Model.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.Model.Database.Entity.Role;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RequestDAO extends BaseDAO<Request> {

    @Query("SELECT " + ConstString.REQUEST_COL_ID + " AS NumRow FROM "
            + ConstString.REQUEST_TABLE_NAME)
    int getCountRequest();

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
            + ConstString.REQUEST_COL_ID_USER + " = :idUser AND " + ConstString.REQUEST_COL_ID_STATE + " =1")
    int getCountWaitingForUser(int idUser);

    @Query("SELECT " + ConstString.ROLE_COL_NAME + " FROM "
            + ConstString.ROLE_TABLE_NAME + " WHERE " + ConstString.ROLE_COL_ID + "= :idRole")
    String getRoleNameById(int idRole);

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME
            + " WHERE " + ConstString.REQUEST_COL_ID + "= :idUser")
    List<Request> getAllRequestForUser(int idUser);

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " , " + ConstString.USER_TABLE_NAME + " WHERE :query")
    List<Request> getRequestForUser(String query);

    @RawQuery(observedEntities = Request.class)
    LiveData<List<Request>> getLimitListRequestForUser(SupportSQLiteQuery query);

//    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " , " + ConstString.USER_TABLE_NAME + " ")
//    List<Request> getRequestForUser();

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
            + ConstString.REQUEST_COL_ID_USER + "= :idUser AND " + ConstString.REQUEST_COL_TITLE + " LIKE :title")
    List<Request> findRequestByTitle(int idUser, String title);

    @Query("SELECT " + ConstString.REQUEST_COL_TITLE + " FROM "
            + ConstString.REQUEST_TABLE_NAME + " WHERE " + ConstString.REQUEST_COL_ID + "= :idRequest")
    String getTitleById(int idRequest);

    @Query("SELECT " + ConstString.REQUEST_COL_DATETIME + " FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE " + ConstString.REQUEST_COL_ID + " = :idRequest")
    long getDateTimeById(int idRequest);

    @Query("SELECT " + ConstString.USER_COL_FULL_NAME + " FROM "
            + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = :idUser")
    String getFullNameById(int idUser);

    @Query("SELECT " + ConstString.REQUEST_COL_ID_STATE + " FROM "
            + ConstString.REQUEST_TABLE_NAME + " WHERE " + ConstString.REQUEST_COL_ID + " = :idRequest")
    int getIdStateById(int idRequest);

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
            + ConstString.REQUEST_COL_ID + " = :idRequest")
    Request getById(int idRequest);

    @Query("SELECT " + ConstString.STATE_REQUEST_COL_ID + " FROM "
            + ConstString.STATE_REQUEST_TABLE_NAME + " WHERE " + ConstString.STATE_REQUEST_COL_NAME + "= :stateName")
    int getIdStateByName(String stateName);

    @Query("SELECT " + ConstString.STATE_REQUEST_COL_NAME + " FROM "
            + ConstString.STATE_REQUEST_TABLE_NAME + " WHERE " + ConstString.STATE_REQUEST_COL_ID + "= :idState")
    String getStateNameById(int idState);

    @Query("SELECT * FROM " + ConstString.REQUEST_TABLE_NAME)
    List<Request> getAll();

    @Insert
    void insertRange(ArrayList<Request> items);

}
