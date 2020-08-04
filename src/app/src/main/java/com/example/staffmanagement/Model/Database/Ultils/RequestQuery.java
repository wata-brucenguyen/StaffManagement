package com.example.staffmanagement.Model.Database.Ultils;

import android.util.Log;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

public class RequestQuery {

    public static String getCountRequest(){
        String query = "SELECT " + ConstString.REQUEST_COL_ID + " AS NumRow FROM "
                + ConstString.REQUEST_TABLE_NAME;
        return query;
    }

    public static String getCountWaitingForUser(int idUser){
        String query = "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
                + ConstString.REQUEST_COL_ID_USER + " = " + idUser + " AND " + ConstString.REQUEST_COL_ID_STATE + " =1";
        return query;
    }

    public static String getRequestForUser(int idUser, String searchString){
        String query = "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " , " + ConstString.USER_TABLE_NAME + " WHERE " +
                ConstString.REQUEST_TABLE_NAME + "." + ConstString.REQUEST_COL_ID_USER + "=" + ConstString.USER_TABLE_NAME + "." + ConstString.USER_COL_ID;
        if (idUser != 0)
            query += ConstString.USER_COL_ID + " = " + idUser + " AND";
        query += ConstString.USER_COL_FULL_NAME + "  LIKE '%" + searchString + "%' ";;
        return query;
    }

    public static String getTitleById(int idRequest){
        String query = "SELECT " + ConstString.REQUEST_COL_TITLE + " FROM "
                + ConstString.REQUEST_TABLE_NAME + " WHERE " + ConstString.REQUEST_COL_ID + " = " + idRequest;
        return query;
    }

    public static String getDateTimeById(int idRequest){
        String query = "SELECT " + ConstString.REQUEST_COL_DATETIME + " FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
                + ConstString.REQUEST_COL_ID + " = "+idRequest;
        return query;
    }

    public static String getIdStateById(int idRequest){
        String query = "SELECT " + ConstString.REQUEST_COL_ID_STATE + " FROM "
                + ConstString.REQUEST_TABLE_NAME + " WHERE " + ConstString.REQUEST_COL_ID + " = "+idRequest;
        return query;
    }

    public static String getById(int idRequest){
        String query = "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
                + ConstString.REQUEST_COL_ID + " = "+idRequest;
        return query;
    }

    public static String getQueryForRequestStaff(int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        String query = "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE ";
        query += ConstString.REQUEST_COL_ID_USER + " = " + idUser + " AND " + ConstString.REQUEST_COL_TITLE + " LIKE '%" + criteria.getSearchString() + "%' ";
        if (criteria.getStateList().size() > 0) {
            query += "AND (";
            for (StaffRequestFilter.STATE s : criteria.getStateList()) {
                if (s.equals(StaffRequestFilter.STATE.Waiting))
                    query += ConstString.REQUEST_COL_ID_STATE + " = 1 OR ";
                else if (s.equals(StaffRequestFilter.STATE.Accept))
                    query += ConstString.REQUEST_COL_ID_STATE + " = 2 OR ";
                else if (s.equals(StaffRequestFilter.STATE.Decline))
                    query += ConstString.REQUEST_COL_ID_STATE + " = 3 OR ";
            }
            query = query.substring(0, query.length() - 3);
            query += ") ";
        }

        if (criteria.getFromDateTime() != 0 && criteria.getToDateTime() != 0) {
            query += " AND ( " + ConstString.REQUEST_COL_DATETIME + " BETWEEN " + criteria.getFromDateTime() + " AND " + criteria.getToDateTime() + " ) ";
        }

        if (!criteria.getSortName().equals(StaffRequestFilter.SORT.None)) {
            query += " ORDER BY " + criteria.getSortName() + " " + criteria.getSortType();
        }
        query += " LIMIT " + offset + "," + numRow;
        Log.i("GETDATA", "sql : " + query);
        return query;
    }

    public static String getQueryForRequestUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        String query = "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + ", " + ConstString.USER_TABLE_NAME + " WHERE ";

        if (idUser != 0)
            query +=ConstString.USER_TABLE_NAME+"."+ ConstString.USER_COL_ID + " = " + idUser + " AND ";

        query += ConstString.REQUEST_TABLE_NAME+"."+ConstString.REQUEST_COL_ID_USER + " = " +ConstString.USER_TABLE_NAME+"."+ ConstString.USER_COL_ID+" AND ";

        query += ConstString.USER_COL_FULL_NAME + " LIKE '%" + criteria.getSearchString() + "%' ";
        if (criteria.getStateList().size() > 0) {
            query += "AND (";
            for (AdminRequestFilter.STATE s : criteria.getStateList()) {
                if (s.equals(AdminRequestFilter.STATE.Waiting))
                    query += ConstString.REQUEST_COL_ID_STATE + " = 1 OR ";
                else if (s.equals(AdminRequestFilter.STATE.Accept))
                    query += ConstString.REQUEST_COL_ID_STATE + " = 2 OR ";
                else if (s.equals(AdminRequestFilter.STATE.Decline))
                    query += ConstString.REQUEST_COL_ID_STATE + " = 3 OR ";
            }
            query = query.substring(0, query.length() - 3);
            query += ") ";
        }

        if (criteria.getFromDateTime() != 0 && criteria.getToDateTime() != 0) {
            query += " AND ( " + ConstString.REQUEST_COL_DATETIME + " BETWEEN " + criteria.getFromDateTime() + " AND " + criteria.getToDateTime() + " ) ";
        }

        if (!criteria.getSortName().equals(AdminRequestFilter.SORT.None)) {
            query += " ORDER BY " + criteria.getSortName() + " " + criteria.getSortType();
        }
        query += " LIMIT " + offset + "," + numRow;
        Log.i("GETDATA", "sql : " + query);
        return query;
    }
}
