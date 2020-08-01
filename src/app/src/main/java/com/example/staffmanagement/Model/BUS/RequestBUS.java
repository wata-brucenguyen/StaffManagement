package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;
import java.util.List;

public class RequestBUS {
    public List<Request> getRequestForUser(Context context, int idUser, String searchString) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = getQuery(idUser, searchString);
        List<Request> list= (List<Request>) appDatabase.requestDAO().getRequestForUser(q);
        return list;
    }

    public List<Request> getLimitListRequestForUser(Context context, int idUser, int offset, int numRow, StaffRequestFilter criteria){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q =getQuery(idUser,offset,numRow,criteria);
        List<Request> list = (List<Request>) appDatabase.requestDAO().getLimitListRequestForUser(q);
        return list;
    }

    public List<Request> getLimitListRequestForUser1(Context context, int idUser, int offset, int numRow, StaffRequestFilter criteria){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q =getQuery(idUser,offset,numRow,criteria);
        List<Request> list = (List<Request>) appDatabase.requestDAO().getLimitListRequestForUser(q);
        return list;
    }

    public int getCountRequest(Context context){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        int count = appDatabase.requestDAO().getCountRequest();
        return count;
    }

    public int getCountWaitingForUser(Context context, int idUser){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        int count = appDatabase.requestDAO().getCountWaitingForUser(idUser);
        return count;
    }

    public String getRoleNameById(Context context, int idRole){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        String name = appDatabase.requestDAO().getRoleNameById(idRole);
        return name;
    }

    public List<Request> getAllRequestForUser(Context context, int idUser){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        List<Request> list = appDatabase.requestDAO().getAllRequestForUser(idUser);
        return list;
    }

    public List<Request> findRequestByTitle(Context context, int idUser, String title){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        List<Request> list = appDatabase.requestDAO().findRequestByTitle(idUser, title);
        return list;
    }

    public String getTitleById(Context context, int idRequest){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        String title = appDatabase.requestDAO().getTitleById(idRequest);
        return title;
    }

    public long getDateTimeById(Context context, int idRequest){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        long dateTime = appDatabase.requestDAO().getDateTimeById(idRequest);
        return dateTime;
    }

    public String getFullNameById(Context context, int idRequest){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        String fullName = appDatabase.requestDAO().getFullNameById(idRequest);
        return fullName;
    }

    public int getIdStateById(Context context, int idRequest){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        int idState = appDatabase.requestDAO().getIdStateById(idRequest);
        return idState;
    }

    public int getIdStateByName(Context context, String stateName){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        int idState = appDatabase.requestDAO().getIdStateByName(stateName);
        return idState;
    }

    public String getStateNameById(Context context, int idState){
        AppDatabase appDatabase =AppDatabase.getInstance(context);
        String stateName = appDatabase.requestDAO().getStateNameById(idState);
        return stateName;
    }

    public List<Request> getAll(Context context){
        AppDatabase appDatabase=AppDatabase.getInstance(context);
        List<Request> list = appDatabase.requestDAO().getAll();
        AppDatabase.onDestroy();
        return list;
    }
    public String getQuery(int idUser, String searchString) {
        String query = ConstString.REQUEST_TABLE_NAME + "." + ConstString.REQUEST_COL_ID_USER + "=" + ConstString.USER_TABLE_NAME + "." + ConstString.USER_COL_ID;
        if (idUser != 0)
            query += ConstString.USER_COL_ID+" = " + idUser + " AND";
        query += ConstString.USER_COL_FULL_NAME+"  LIKE '%" + searchString + "%' ";
        return query;
    }

    public String getQuery1(int idUser, int offset, int numRow, StaffRequestFilter criteria){
        String query =ConstString.USER_COL_FULL_NAME +" LIKE '%" + criteria.getSearchString() + "%' ";
        if (criteria.getStateList().size() > 0) {
            query += "AND (";
            for (StaffRequestFilter.STATE s : criteria.getStateList()) {
                if (s.equals(StaffRequestFilter.STATE.Waiting))
                    query += ConstString.REQUEST_COL_ID_STATE+" = 1 OR ";
                else if (s.equals(StaffRequestFilter.STATE.Accept))
                    query += ConstString.REQUEST_COL_ID_STATE+" = 2 OR ";
                else if (s.equals(StaffRequestFilter.STATE.Decline))
                    query += ConstString.REQUEST_COL_ID_STATE+ " = 3 OR ";
            }
            query = query.substring(0, query.length() - 3);
            query += ") ";
        }

        if (criteria.getFromDateTime() != 0 && criteria.getToDateTime() != 0) {
            query += " AND ( "+ConstString.REQUEST_COL_DATETIME+" BETWEEN " + criteria.getFromDateTime() + " AND " + criteria.getToDateTime() + " ) ";
        }

        if (!criteria.getSortName().equals(StaffRequestFilter.SORT.None)) {
            query += " ORDER BY " + criteria.getSortName() + " " + criteria.getSortType();
        }
        query += " LIMIT " + offset + "," + numRow;
        return query;
    }

    public String getQuery(int idUser, int offset, int numRow, StaffRequestFilter criteria){
        String query = ConstString.REQUEST_COL_ID_USER+"  = " + idUser + " AND "+ConstString.REQUEST_COL_TITLE+" LIKE '%" + criteria.getSearchString() + "%' ";
        if (criteria.getStateList().size() > 0) {
            query += "AND (";
            for (StaffRequestFilter.STATE s : criteria.getStateList()) {
                if (s.equals(StaffRequestFilter.STATE.Waiting))
                    query += ConstString.REQUEST_COL_ID_STATE+" = 1 OR ";
                else if (s.equals(StaffRequestFilter.STATE.Accept))
                    query += ConstString.REQUEST_COL_ID_STATE+" = 2 OR ";
                else if (s.equals(StaffRequestFilter.STATE.Decline))
                    query += ConstString.REQUEST_COL_ID_STATE+ " = 3 OR ";
            }
            query = query.substring(0, query.length() - 3);
            query += ") ";
        }

        if (criteria.getFromDateTime() != 0 && criteria.getToDateTime() != 0) {
            query += " AND ( "+ConstString.REQUEST_COL_DATETIME+" BETWEEN " + criteria.getFromDateTime() + " AND " + criteria.getToDateTime() + " ) ";
        }

        if (!criteria.getSortName().equals(StaffRequestFilter.SORT.None)) {
            query += " ORDER BY " + criteria.getSortName() + " " + criteria.getSortType();
        }
        query += " LIMIT " + offset + "," + numRow;
        return query;
    }
}
