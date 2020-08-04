package com.example.staffmanagement.Model.Database.Ultils;

import android.util.Log;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;

public class GeneralFunction {

    public static String getQueryForRequest1(int idUser, int offset, int numRow, AdminRequestFilter criteria) {

        String query = "SELECT RE.Id, RE.Title, RE.IdUser, RE.IdState, RE.Content, RE.DateTime FROM " + ConstString.REQUEST_TABLE_NAME + " RE, " + ConstString.USER_TABLE_NAME + " U " + " WHERE ";

        if (idUser != 0)
            query +=  "U." + ConstString.USER_COL_ID + " = " + idUser + " AND ";

        query += "RE." + ConstString.REQUEST_COL_ID_USER + " = " + "U." + ConstString.USER_COL_ID + " AND ";

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
