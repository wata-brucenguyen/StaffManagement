package com.example.staffmanagement.Model.Repository.Request;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Request.RequestService;
import com.example.staffmanagement.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.Ultils.ConstString;
import com.example.staffmanagement.Model.Ultils.RequestQuery;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

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
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    AppDatabase.getDb().requestDAO().deleteAll();
                    AppDatabase.getDb().requestDAO().insertRange(success.getData());
                    String q = RequestQuery.getQueryForRequestStaff(idUser, offset, numRow, criteria);
                    SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
                    mLiveData.postValue(AppDatabase.getDb().requestDAO().getLimitListRequestForUserInStaff(sql));
                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {
                mLiveData.postValue(new ArrayList<>());
            }
        });

    }

    public MutableLiveData<List<String>> getFullNameListLD() {
        return fullNameListLD;
    }

    public MutableLiveData<List<Request>> getLiveData() {
        return mLiveData;
    }

    public void getLimitListRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {

        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    AppDatabase.getDb().requestDAO().deleteAll();
                    AppDatabase.getDb().requestDAO().insertRange(success.getData());
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

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {
                mLiveData.postValue(new ArrayList<>());
                mLiveData.postValue(new ArrayList<>());
            }
        });
    }

    public void restoreRequest(Request request) {
        service.update(request);
    }

    public void insert(Request request, final int idUser, final int offset, final StaffRequestFilter criteria) {
        service.put(request, new ApiResponse<Request>() {
            @Override
            public void onSuccess(Resource<Request> success) {
                getLimitListRequestForStaffLD(idUser, offset, 1, criteria);
            }

            @Override
            public void onLoading(Resource<Request> loading) {

            }

            @Override
            public void onError(Resource<Request> error) {

            }
        });
    }

    public void updateRequest(Request request) {
        service.update(request);
    }

    public void deleteRequest(Request request) {
        service.delete(request.getId());
    }

    public void getAll() {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                List<Request> list = success.getData();
                list.sort((request, t1) -> request.getDateTime() > t1.getDateTime() ? -1 : 1);
                for (int i = 0; i < list.size(); i++) {
                    Log.i("FETCH", " " + list.get(i).getDateTime());
                }
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }
}


