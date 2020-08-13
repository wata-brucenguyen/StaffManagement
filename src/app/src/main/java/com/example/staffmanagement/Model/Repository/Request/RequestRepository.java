package com.example.staffmanagement.Model.Repository.Request;

import com.example.staffmanagement.Model.LocalDb.BUS.RequestBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Request;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.Model.Repository.Base.NetworkBoundResource;

import java.util.List;

public class RequestRepository {
    private RequestService service;
    private RequestBUS bus;

    public RequestRepository() {
        service = new RequestService();

    }

    public List<Request> getAll() {
        return new NetworkBoundResource<List<Request>, List<Request>>() {
            @Override
            protected List<Request> loadFromDb() {
                return bus.getAll();
            }

            @Override
            protected boolean shouldFetchData(List<Request> data) {
                return data.isEmpty();
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
               // service.getAll();
            }

            @Override
            protected void saveCallResult(List<Request> data) {

            }

            @Override
            protected void onFetchFail() {

            }

            @Override
            protected void onFetchSuccess(List<Request> data) {

            }
        }.run();
    }
}
