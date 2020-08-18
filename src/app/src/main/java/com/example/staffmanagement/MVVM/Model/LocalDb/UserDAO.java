package com.example.staffmanagement.MVVM.Model.LocalDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.Model.LocalDb.Database.Ultils.ConstString;

import java.util.List;

@Dao
public interface UserDAO extends BaseDAO<User> {

    @Insert
    void insertRange(List<User> userList);

    @RawQuery(observedEntities = User.class)
    int getCount(SupportSQLiteQuery query);

   @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME)
    List<User> getAll();

    @RawQuery(observedEntities = User.class)
    List<Role> getAllRole(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    User getById(SupportSQLiteQuery query);

    @Query("SELECT * FROM "+ ConstString.USER_TABLE_NAME + " WHERE Id = :id")
    User getUserById(int id);

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
    User checkUserNameIsExisted(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    String getFullNameById(SupportSQLiteQuery query);
}
