package com.example.staffmanagement.Model.BUS;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Ultils.GeneralFunction;
import com.example.staffmanagement.Model.Database.Ultils.RequestQuery;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

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

        String q = RequestQuery.getById((int) id);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        Request req = appDatabase.requestDAO().getById(sql);

        AppDatabase.onDestroy();
        return req;
    }

    public void updateStateRequest(Context context, Request request) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        appDatabase.requestDAO().update(request);
        AppDatabase.onDestroy();
    }

    public List<Request> getRequestForUser(Context context, int idUser, String searchString) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getRequestForUser(idUser,searchString);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        List<Request> list = appDatabase.requestDAO().getRequestForUser(sql);
        AppDatabase.onDestroy();
        return list;
    }

    public void getLimitListRequestForStaff(Context context, int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getQueryForRequestStaff(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        listLiveData = appDatabase.requestDAO().getLimitListRequestForUser(sql);
        AppDatabase.onDestroy();
    }

    public void getLimitListRequestForUser(Context context, int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getQueryForRequestUser(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        listLiveData = appDatabase.requestDAO().getLimitListRequestForUser(sql);
        AppDatabase.onDestroy();
    }

    public int getCountRequest(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getCountRequest();
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int count = appDatabase.requestDAO().getCountRequest(sql);
        AppDatabase.onDestroy();
        return count;
    }

    public int getCountWaitingForUser(Context context, int idUser) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getCountWaitingForUser(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int count = appDatabase.requestDAO().getCountWaitingForUser(sql);
        AppDatabase.onDestroy();
        return count;
    }


    public String getTitleById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getTitleById(idRequest);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        String title = appDatabase.requestDAO().getTitleById(sql);
        AppDatabase.onDestroy();
        return title;
    }

    public long getDateTimeById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getDateTimeById(idRequest);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        long dateTime = appDatabase.requestDAO().getDateTimeById(sql);
        AppDatabase.onDestroy();
        return dateTime;

    }

    public int getIdStateById(Context context, int idRequest) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = RequestQuery.getIdStateById(idRequest);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int idState = appDatabase.requestDAO().getIdStateById(sql);
        AppDatabase.onDestroy();
        return idState;
    }

    public String getStateNameById(Context context, int idState) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String stateName = appDatabase.requestDAO().getStateNameById(idState);
        appDatabase.onDestroy();
        return stateName;
    }

    public List<Request> getAll(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        List<Request> list = appDatabase.requestDAO().getAll();
        AppDatabase.onDestroy();
        return list;
    }
}
