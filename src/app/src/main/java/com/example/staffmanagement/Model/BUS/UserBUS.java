package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Ultils.UserQuery;
import com.example.staffmanagement.View.Ultils.Constant;

import java.util.List;
import java.util.Map;
import java.util.prefs.AbstractPreferences;

public class UserBUS {

    public User getByLoginInformation(Context context, String userName, String password) {
        AppDatabase app = AppDatabase.getInstance(context);
        User user = app.userDAO().getUserByUserName(userName);
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
        String q = getQuery(idUser, offset, numRow, criteria);
        List<User> list = appDatabase.userDAO().getLimitListForUser(q);
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
        User req = appDatabase.userDAO().getById((int) id);
        AppDatabase.onDestroy();
        return req;
    }

    public void delete(Context context,User user) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        appDatabase.userDAO().delete(user);
        AppDatabase.onDestroy();
    }

    public String getQuery(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        String searchString = (String) criteria.get(Constant.SEARCH_NAME_IN_ADMIN);
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U1 WHERE " + ConstString.USER_COL_FULL_NAME + " LIKE '%" + searchString
                + "%' AND NOT EXISTS ( SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U2 WHERE U1." + ConstString.USER_COL_ID + "= U2."
                + ConstString.USER_COL_ID + " AND U2." + ConstString.USER_COL_ID + " = " + idUser + " ) ";
        query += " LIMIT " + offset + "," + numRow;
        return query;
    }
    //Livedata : map . flat map, stream, observe

    public User getById(Context context, int idUser) {
        User user = AppDatabase.getInstance(context).userDAO().getById(idUser);
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
        List<Role> list = appDatabase.userDAO().getAllRole();
        AppDatabase.onDestroy();
        return list;
    }

    public User changeIdUserState(Context context,int id,int idUserState){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        User user = appDatabase.userDAO().changeIdUserState(id,idUserState);
        AppDatabase.onDestroy();
        return user;
    }
}
