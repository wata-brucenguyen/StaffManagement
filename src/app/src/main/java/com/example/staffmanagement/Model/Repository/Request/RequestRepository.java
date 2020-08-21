package com.example.staffmanagement.Model.Repository.Request;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.FirebaseDb.Request.RequestService;
import com.example.staffmanagement.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.Ultils.RequestQuery;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RequestRepository {
    private RequestService service;
    private MutableLiveData<List<Request>> mLiveData;
    private MutableLiveData<List<String>> fullNameListLD;

    public RequestRepository() {
        service = new RequestService();
        this.mLiveData = new MutableLiveData<>();
        fullNameListLD = new MutableLiveData<>();
    }

    public void getLimitListRequestForStaffLD(int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        new Thread(() -> {
            String q = RequestQuery.getQueryForRequestStaff(idUser, offset, numRow, criteria);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            mLiveData.postValue(AppDatabase.getDb().requestDAO().getLimitListRequestForUserInStaff(sql));
        }).start();
    }

    public void getLimitListRequestForStaffService(int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        new NetworkBoundResource<List<Request>, List<Request>>() {
            @Override
            protected List<Request> loadFromDb() {
                String q = RequestQuery.getQueryForRequestStaff(idUser, offset, numRow, criteria);
                SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
                return AppDatabase.getDb().requestDAO().getLimitListRequestForUserInStaff(sql);
            }

            @Override
            protected boolean shouldFetchData(List<Request> data) {
                return data.size() < numRow ;//data == null || data.size() == 0;
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                service.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<Request> data) {
                int count = AppDatabase.getDb().requestDAO().count();
                if(count != data.size()){
                    AppDatabase.getDb().requestDAO().deleteAll();
                    AppDatabase.getDb().requestDAO().insertRange(data);
                }
            }

            @Override
            protected void onFetchFail(String message) {

            }

            @Override
            protected void onFetchSuccess(List<Request> data) {
                mLiveData.postValue(data);
            }
        }.run();
    }


    public MutableLiveData<List<String>> getFullNameListLD() {
        return fullNameListLD;
    }

    public MutableLiveData<List<Request>> getLiveData() {
        return mLiveData;
    }

    public void getLimitListRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        new Thread(() -> {
            String q = RequestQuery.getQueryForRequestUser(idUser, offset, numRow, criteria);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            List<Request> requestList = AppDatabase.getDb().requestDAO().getLimitListRequestForUser(sql);
            List<String> fullNameList = new ArrayList<>();
            for (int i = 0; i < requestList.size(); i++) {
                String s = AppDatabase.getDb().userDAO().getUserNameById(requestList.get(i).getIdUser());
                fullNameList.add(s);
            }
            fullNameListLD.postValue(fullNameList);
            mLiveData.postValue(requestList);
        }).start();

    }

    public void restoreRequest(Request request) {
        new Thread(() -> AppDatabase.getDb().requestDAO().insert(request)).start();
    }

    public Request insert(Request request, final int idUser, final int offset, final StaffRequestFilter criteria) {
        CompletableFuture<Request> future = CompletableFuture.supplyAsync(() -> {
            long id = AppDatabase.getDb().requestDAO().insert(request);
            String q = RequestQuery.getById((int) id);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            Request req = AppDatabase.getDb().requestDAO().getById(sql);
            return req;
        }).thenApply(request1 -> {
            getLimitListRequestForStaffLD(idUser, offset, 1, criteria);
            return request1;
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRequest(Request request){
        new Thread(() -> AppDatabase.getDb().requestDAO().update(request)).start();
    }

    public void deleteRequest(Request request){
        new Thread(() -> AppDatabase.getDb().requestDAO().delete(request)).start();
    }

    public int getQuantityWaitingRequestForUser(int idUser) {
        String q = RequestQuery.getCountWaitingForUser(idUser);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int count = AppDatabase.getDb().requestDAO().getCountWaitingForUser(sql);
        return count;
    }

}


