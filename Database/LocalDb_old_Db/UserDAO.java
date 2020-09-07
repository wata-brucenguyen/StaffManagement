package com.example.staffmanagement.Model.LocalDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Ultils.ConstString;

import java.util.List;

@Dao
public interface UserDAO extends BaseDAO<User> {

    @Insert
    void insertRange(List<User> userList);

    @RawQuery(observedEntities = User.class)
    int getCount(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    int getCountStaff(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    List<User> getAllStaff(SupportSQLiteQuery query);

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME)
    List<User> getAll();

    @RawQuery(observedEntities = User.class)
    List<Role> getAllRole(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    User getById(SupportSQLiteQuery query);

    @Query("SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE Id = :id")
    User getUserById(int id);

    @RawQuery(observedEntities = User.class)
    User getUserByUserName(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    List<User> getLimitListUser(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    boolean changeIdUserState(SupportSQLiteQuery query);

//    @Query("UPDATE User SET IdUserState = :idState WHERE Id = :idUser")
//    void changeIdUserState(int idUser,int idState);

    @RawQuery(observedEntities = User.class)
    boolean resetPassword(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    boolean changeAvatar(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    User checkUserNameIsExisted(SupportSQLiteQuery query);

    @RawQuery(observedEntities = User.class)
    String getFullNameById(SupportSQLiteQuery query);

    @Query("SELECT FullName FROM " + ConstString.USER_TABLE_NAME + " WHERE Id = :id")
    String getUserNameById(int id);

    @Query("DELETE FROM " + ConstString.USER_TABLE_NAME)
    void deleteAll();

    @Query("SELECT COUNT(Id) FROM " + ConstString.USER_TABLE_NAME)
    int count();
}
