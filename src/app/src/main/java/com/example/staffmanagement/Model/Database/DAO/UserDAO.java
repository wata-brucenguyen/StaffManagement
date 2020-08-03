package com.example.staffmanagement.Model.Database.DAO;

import android.view.textservice.SentenceSuggestionsInfo;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDAO extends BaseDAO<User>{

    @Insert
    void insertRange(ArrayList<User> userList);

    @RawQuery(observedEntities = User.class)
    int getCount(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    List<User> getAll(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    List<Role> getAllRole(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    User getById(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    User getUserByUserName(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    List<User> getLimitListUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    boolean changeIdUserState(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    boolean resetPassword(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    boolean changeAvatar(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    boolean checkUserNameIsExisted(SupportSQLiteQuery query);
}
