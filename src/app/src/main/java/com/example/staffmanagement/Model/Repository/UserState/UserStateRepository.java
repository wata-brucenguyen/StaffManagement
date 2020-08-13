package com.example.staffmanagement.Model.Repository.UserState;

import com.example.staffmanagement.Model.LocalDb.BUS.UserStateBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.Model.Repository.Base.NetworkBoundResource;

import java.util.List;

public class UserStateRepository {
    private UserStateService service;
    private UserStateBUS bus;

    public UserStateRepository() {
        service = new UserStateService();
        bus = new UserStateBUS();
    }

    public List<UserState> getAll() {
        return new NetworkBoundResource<List<UserState>, List<UserState>>() {

            @Override
            protected List<UserState> loadFromDb() {
                return bus.getAll();
            }

            @Override
            protected boolean shouldFetchData(List<UserState> data) {
                return data.isEmpty();
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                service.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<UserState> data) {
                bus.insertRange(data);
            }

            @Override
            protected void onFetchFail() {

            }

            @Override
            protected void onFetchSuccess(List<UserState> data) {

            }
        }.run();
    }
}
