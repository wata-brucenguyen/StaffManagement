package com.example.staffmanagement.Model.LocalDb.Database.Ultils;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.View.Ultils.Constant;

import java.util.Map;

public class UserQuery {
    public static String getCount() {
        String query = "SELECT COUNT(" + ConstString.USER_COL_ID + ") FROM " + ConstString.USER_TABLE_NAME;
        return query;
    }

    public static String getById(int id) {
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = " + id;
        return query;
    }

    public static String getUserByUserName(String userName) {
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE "
                + ConstString.USER_COL_USERNAME + " = '" + userName + "'";
        return query;
    }

    // IdRole = 2 AND
    public static String getLimitListForUser(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        String searchString = (String) criteria.get(Constant.SEARCH_NAME_IN_ADMIN);
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U1 WHERE IdRole = 2 AND " + ConstString.USER_COL_FULL_NAME + " LIKE '%" + searchString
                + "%' AND NOT EXISTS ( SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U2 WHERE U1." + ConstString.USER_COL_ID + "= U2."
                + ConstString.USER_COL_ID + " AND U2." + ConstString.USER_COL_ID + " = " + idUser + " ) ";
        query += " LIMIT " + offset + "," + numRow;
        return query;
    }

    public static String changeIdUserState(int idUser, int idUserState) {
        String query = " UPDATE " + ConstString.USER_TABLE_NAME + " SET " + ConstString.USER_COL_ID_USER_STATE +
                " = " + idUserState + " WHERE " + ConstString.USER_COL_ID + " = " + idUser;
        return query;
    }

    public static String resetPassword(int idUser) {
        String query = " UPDATE " + ConstString.USER_TABLE_NAME + " SET " + ConstString.USER_COL_PASSWORD +
                " = " + ConstString.DEFAULT_PASSWORD + " WHERE " + ConstString.USER_COL_ID + " = " + idUser;
        return query;
    }

    public static String changeAvatar(User user) {
        String query = " UPDATE " + ConstString.USER_TABLE_NAME + " SET " + ConstString.USER_COL_AVATAR +
                " = " + user.getAvatar() + " WHERE " + ConstString.USER_COL_ID + " = " + user.getId();
        return query;
    }

    public static String getFullNameById(int idUser) {
        String query = "SELECT " + ConstString.USER_COL_FULL_NAME + " FROM "
                + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = " + idUser;
        return query;
    }

}
