package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Ultils.UserQuery;

import java.util.List;
import java.util.Map;
import java.util.prefs.AbstractPreferences;

public class UserBUS {

    public User getByLoginInformation(Context context, String userName, String password) {
        AppDatabase app = AppDatabase.getInstance(context);
        String q = UserQuery.getUserByUserName(userName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        User user = app.userDAO().getUserByUserName(sql);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                AppDatabase.onDestroy();
                return user;
            }
            AppDatabase.onDestroy();
            return null;
        }
        AppDatabase.onDestroy();
        return null;
    }


    public List<User> getLimitListUser(Context context, int idUser, int offset, int numRow, Map<String, Object> criteria) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.getLimitListForUser(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        List<User> list = appDatabase.userDAO().getLimitListUser(sql);
        AppDatabase.onDestroy();
        return list;
    }

    public void update(Context context, User user) {
        AppDatabase app = AppDatabase.getInstance(context);
        app.userDAO().update(user);
        AppDatabase.onDestroy();
    }

    public User insert(Context context, User user) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        long id = appDatabase.userDAO().insert(user);
        String q = UserQuery.getById((int) id);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        User req = appDatabase.userDAO().getById(sql);
        AppDatabase.onDestroy();
        return req;
    }
    //Livedata : map . flat map, stream, observe

    public User getById(Context context, int idUser) {
        String q = UserQuery.getById(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        User user = AppDatabase.getInstance(context).userDAO().getById(sql);
        AppDatabase.onDestroy();
        return user;
    }

    public int getCount(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.getCount();
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int count = appDatabase.userDAO().getCount(sql);
        AppDatabase.onDestroy();
        return count;
    }

    public List<User> getAll(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        List<User> list = appDatabase.userDAO().getAll();
        AppDatabase.onDestroy();
        return list;
    }

    public List<Role> getAllRole(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.getAllRole();
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        List<Role> list = appDatabase.userDAO().getAllRole(sql);
        AppDatabase.onDestroy();
        return list;
    }

    public void changeIdUserState(Context context, int idUser, int idUserState) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.changeIdUserState(idUser, idUserState);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        appDatabase.userDAO().changeIdUserState(sql);
        AppDatabase.onDestroy();
    }

    public void resetPassword(Context context, int idUser) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.resetPassword(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        appDatabase.userDAO().resetPassword(sql);
        AppDatabase.onDestroy();
    }

    public void changeAvatar(Context context, User user) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.changeAvatar(user);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        appDatabase.userDAO().changeAvatar(sql);
        AppDatabase.onDestroy();
    }

    public boolean checkUserNameIsExisted(Context context, String userName) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = UserQuery.getUserByUserName(userName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        if (appDatabase.userDAO().checkUserNameIsExisted(sql)) {
            AppDatabase.onDestroy();
            return true;
        }
        AppDatabase.onDestroy();
        return false;

    }
}
