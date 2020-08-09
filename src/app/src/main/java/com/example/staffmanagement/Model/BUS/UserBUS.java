package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Ultils.UserQuery;

import java.util.List;
import java.util.Map;

public class UserBUS {

    public User getByLoginInformation(String userName, String password) {

        String q = UserQuery.getUserByUserName(userName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        User user = AppDatabase.getDb().userDAO().getUserByUserName(sql);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
            return null;
        }
        return null;
    }


    public List<User> getLimitListUser(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        String q = UserQuery.getLimitListForUser(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        List<User> list = AppDatabase.getDb().userDAO().getLimitListUser(sql);
        return list;
    }

    public void update(User user) {
        AppDatabase.getDb().userDAO().update(user);
    }

    public User insert(User user) {
        long id = AppDatabase.getDb().userDAO().insert(user);
        String q = UserQuery.getById((int) id);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        User u = AppDatabase.getDb().userDAO().getById(sql);
        return u;
    }
    //Livedata : map . flat map, stream, observe

    public User getById(int idUser) {
        String q = UserQuery.getById(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        User user =AppDatabase.getDb().userDAO().getById(sql);
        return user;
    }

    public int getCount() {
        String q = UserQuery.getCount();
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int count = AppDatabase.getDb().userDAO().getCount(sql);
        return count;
    }

    public void changeIdUserState(int idUser, int idUserState) {
        String q = UserQuery.changeIdUserState(idUser, idUserState);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        AppDatabase.getDb().userDAO().changeIdUserState(sql);
    }

    public void resetPassword(int idUser) {
        String q = UserQuery.resetPassword(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        AppDatabase.getDb().userDAO().resetPassword(sql);
    }

    public void changeAvatar(User user) {
        String q = UserQuery.changeAvatar(user);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        AppDatabase.getDb().userDAO().changeAvatar(sql);
    }

    public boolean checkUserNameIsExisted(String userName) {
        String q = UserQuery.getUserByUserName(userName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        if (AppDatabase.getDb().userDAO().checkUserNameIsExisted(sql) != null) {
            return true;
        }
        return false;

    }

    public String getFullNameById( int idRequest) {
        String q = UserQuery.getFullNameById(idRequest);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        String fullName = AppDatabase.getDb().userDAO().getFullNameById(sql);
        return fullName;
    }
}
