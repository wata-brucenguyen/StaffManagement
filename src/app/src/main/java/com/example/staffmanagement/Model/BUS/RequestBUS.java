package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Ultils.RequestQuery;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.List;

public class RequestBUS {

    private LiveData<List<Request>> listLiveData;

    public RequestBUS() {
        this.listLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Request>> getListLiveData() {
        return listLiveData;
    }

    public Request insert(Context context, Request request) {
        long id = AppDatabase.getDb().requestDAO().insert(request);
        String q = RequestQuery.getById((int) id);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        Request req =AppDatabase.getDb().requestDAO().getById(sql);
        return req;
    }

    public void updateStateRequest(Context context, Request request) {
        AppDatabase.getDb().requestDAO().update(request);
    }

    public void getLimitListRequestForStaff(Context context, int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        String q = RequestQuery.getQueryForRequestStaff(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        listLiveData = AppDatabase.getDb().requestDAO().getLimitListRequestForUser(sql);
    }

    public void getLimitListRequestForUser(Context context, int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        String q = RequestQuery.getQueryForRequestUser(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        listLiveData = AppDatabase.getDb().requestDAO().getLimitListRequestForUser(sql);
    }

    public int getCountWaitingForUser(Context context, int idUser) {
        String q = RequestQuery.getCountWaitingForUser(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int count = AppDatabase.getDb().requestDAO().getCountWaitingForUser(sql);
        return count;
    }

    public List<Request> getAll() {
        List<Request> list = AppDatabase.getDb().requestDAO().getAll();
        return list;
    }
}
