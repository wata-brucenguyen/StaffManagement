package com.example.staffmanagement.Model.LocalDb.BUS;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.LocalDb.Database.Ultils.RequestQuery;
import com.example.staffmanagement.MVVM.View.Data.AdminRequestFilter;
import com.example.staffmanagement.MVVM.View.Data.StaffRequestFilter;

import java.util.List;

public class RequestBUS {

    public Request insert(Request request) {
        long id = AppDatabase.getDb().requestDAO().insert(request);
        String q = RequestQuery.getById((int) id);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        Request req = AppDatabase.getDb().requestDAO().getById(sql);
        return req;
    }

    public void insertRange(List<Request> list){
        AppDatabase.getDb().requestDAO().insertRange(list);
    }

    public void delete(Request request){
        AppDatabase.getDb().requestDAO().delete(request);
    }

//    public void updateStateRequest( Request request) {
//        AppDatabase.getDb().requestDAO().update(request);
//    }

//    public List<Request> getLimitListRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
//        String q = RequestQuery.getQueryForRequestUser(idUser, offset, numRow, criteria);
//        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
//        return AppDatabase.getDb().requestDAO().getLimitListRequestForUser(sql);
//    }

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
