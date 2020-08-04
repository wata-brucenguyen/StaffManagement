package com.example.staffmanagement.Model.BUS;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Ultils.GeneralFunction;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RequestBUS {

    private LiveData<List<Request>> listLiveData;

    public RequestBUS() {
        this.listLiveData = new MutableLiveData<>();
        //WeakReference<LiveData<List<Request>>> weak = new WeakReference<>(listLiveData);
    }

    public LiveData<List<Request>> getListLiveData() {
        return listLiveData;
    }

    public Request insert(Context context, Request request) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        long id = appDatabase.requestDAO().insert(request);
        Log.i("INSERT","NEW ID : " + id);
        Request req = appDatabase.requestDAO().getById((int) id);
        AppDatabase.onDestroy();
        return req;
    }

    public void update(Context context, Request request) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        appDatabase.requestDAO().update(request);
        AppDatabase.onDestroy();
    }

    public List<Request> getRequestForUser(Context context, int idUser, String searchString) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = getQuery(idUser, searchString);
        List<Request> list = (List<Request>) appDatabase.requestDAO().getRequestForUser(q);
        return list;
    }

    public void getLimitListRequestForUser(Context context, int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = GeneralFunction.getQueryForRequest(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        listLiveData = appDatabase.requestDAO().getLimitListRequestForUser(sql);
        if ( getListLiveData() != null && getListLiveData().getValue() != null) {
            for(int i= 0 ; i< getListLiveData().getValue().size(); i++){
                Log.i("GETDATA","load bus: "+getListLiveData().getValue().get(i).getTitle());
            }
        }
        AppDatabase.onDestroy();
    }

    public void getLimitListRequestForUser1(Context context, int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = GeneralFunction.getQueryForRequest1(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        listLiveData = appDatabase.requestDAO().getLimitListRequestForUser(sql);
        AppDatabase.onDestroy();
    }

    public int getCountRequest(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        int count = appDatabase.requestDAO().getCountRequest();
        return count;
    }

    public int getCountWaitingForUser(Context context, int idUser) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        int count = appDatabase.requestDAO().getCountWaitingForUser(idUser);
        return count;
    }

    public String getRoleNameById(Context context, int idRole) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String name = appDatabase.requestDAO().getRoleNameById(idRole);
        return name;
    }

    public List<Request> getAllRequestForUser(Context context, int idUser) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        List<Request> list = appDatabase.requestDAO().getAllRequestForUser(idUser);
        return list;
    }

    public List<Request> findRequestByTitle(Context context, int idUser, String title) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        List<Request> list = appDatabase.requestDAO().findRequestByTitle(idUser, title);
        return list;
    }

    public String getTitleById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String title = appDatabase.requestDAO().getTitleById(idRequest);
        appDatabase.close();
        return title;
    }

    public long getDateTimeById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        long dateTime = appDatabase.requestDAO().getDateTimeById(idRequest);
        appDatabase.close();
        return dateTime;

    }

    public String getFullNameById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String fullName = appDatabase.requestDAO().getFullNameById(idRequest);
        appDatabase.close();
        return fullName;
    }

    public int getIdStateById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        int idState = appDatabase.requestDAO().getIdStateById(idRequest);
        appDatabase.close();
        return idState;
    }

    public int getIdStateByName(Context context, String stateName) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        int idState = appDatabase.requestDAO().getIdStateByName(stateName);
        appDatabase.close();
        return idState;
    }

    public String getStateNameById(Context context, int idState) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String stateName = appDatabase.requestDAO().getStateNameById(idState);
        appDatabase.close();
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
            query += ConstString.USER_COL_ID + " = " + idUser + " AND";
        query += ConstString.USER_COL_FULL_NAME + "  LIKE '%" + searchString + "%' ";
        return query;
    }

}
