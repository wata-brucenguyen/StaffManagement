package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Ultils.Constant;

import java.util.Map;

public class UserBUS {


    public User getByLoginInformation(Context context, String userName, String password) {
        AppDatabase app = AppDatabase.getInstance(context);
        User user = app.userDAO().getUserByUserName(userName);
        if (user != null) {
            if (user.getPassword().equals(password))
                return user;
            return null;
        }
        return null;
    }

    private String getQuery(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        String searchString = (String) criteria.get(Constant.SEARCH_NAME_IN_ADMIN);
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U1 WHERE " + ConstString.USER_COL_FULL_NAME + " LIKE '%" + searchString
                + "%' AND NOT EXISTS ( SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U2 WHERE U1." + ConstString.USER_COL_ID + "= U2."
                + ConstString.USER_COL_ID + " AND U2." + ConstString.USER_COL_ID + " = " + idUser + " ) ";
        query += " LIMIT " + offset + "," + numRow;
        return query;
    }


}
