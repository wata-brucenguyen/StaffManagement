package com.example.staffmanagement.Model.Repository.StateRequest;

import com.example.staffmanagement.Model.LocalDb.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.Model.Repository.Base.NetworkBoundResource;

import java.util.List;

public class StateRequestRepository {
    private StateRequestService service;
    private StateRequestBUS bus;

    public StateRequestRepository() {
        service = new StateRequestService();
        bus = new StateRequestBUS();
    }

    public List<StateRequest> getAll(){
        return new NetworkBoundResource<List<StateRequest>, List<StateRequest>>() {
            @Override
            protected List<StateRequest> loadFromDb() {
                return bus.getAllStateRequest();
            }

            @Override
            protected boolean shouldFetchData(List<StateRequest> data) {
                return data.isEmpty();
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                service.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<StateRequest> data) {
                bus.insertRange(data);
            }

            @Override
            protected void onFetchFail() {

            }

            @Override
            protected void onFetchSuccess(List<StateRequest> data) {

            }
        }.run();
    }
}
