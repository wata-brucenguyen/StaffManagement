package com.example.staffmanagement.Model.BUS;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Ultils.RequestQuery;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Main.App;

import java.util.ArrayList;
import java.util.List;

public class RequestBUS {

    public Request insert(Request request) {
        long id = AppDatabase.getDb().requestDAO().insert(request);
        String q = RequestQuery.getById((int) id);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        Request req = AppDatabase.getDb().requestDAO().getById(sql);
        return req;
    }

    public void delete(Request request){
        AppDatabase.getDb().requestDAO().delete(request);
    }

    public void updateStateRequest( Request request) {
        AppDatabase.getDb().requestDAO().update(request);
    }

    public List<Request> getLimitListRequestForStaff(int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        String q = RequestQuery.getQueryForRequestStaff(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        return AppDatabase.getDb().requestDAO().getLimitListRequestForUserInStaff(sql);
    }

    public List<Request> getLimitListRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        String q = RequestQuery.getQueryForRequestUser(idUser, offset, numRow, criteria);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        return AppDatabase.getDb().requestDAO().getLimitListRequestForUser(sql);
    }

    public int getQuantityWaitingRequestForUser(int idUser) {
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
