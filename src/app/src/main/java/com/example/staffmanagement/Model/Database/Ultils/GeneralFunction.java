package com.example.staffmanagement.Model.Database.Ultils;

import android.util.Log;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

public class GeneralFunction {

    public static String getQueryForRequest(int idUser, int offset, int numRow, StaffRequestFilter criteria) {
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
}
