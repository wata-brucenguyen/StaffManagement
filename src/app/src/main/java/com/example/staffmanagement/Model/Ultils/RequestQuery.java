package com.example.staffmanagement.Model.Ultils;

import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

public class RequestQuery {

    public static String getCountWaitingForUser(int idUser) {
        String query = "SELECT COUNT(Id) FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
                + ConstString.REQUEST_COL_ID_USER + " = " + idUser + " AND " + ConstString.REQUEST_COL_ID_STATE + " = 1";
        return query;
    }

    public static String getById(int idRequest) {
        String query = "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE "
                + ConstString.REQUEST_COL_ID + " = " + idRequest;
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
        return query;
    }


    public static String getQueryForRequestUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        String query = "SELECT RE.Id, RE.Title, RE.IdUser, RE.IdState, RE.Content, RE.DateTime FROM " + ConstString.REQUEST_TABLE_NAME + " RE, " + ConstString.USER_TABLE_NAME + " U " + " WHERE ";
        query += "RE." + ConstString.REQUEST_COL_ID_USER + " = " + "U." + ConstString.USER_COL_ID + " AND ";
        if (idUser != 0)
            query += "RE." + ConstString.REQUEST_COL_ID_USER + " = " + idUser + " AND ";

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
        return query;
    }

}
