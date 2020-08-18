package com.example.staffmanagement.MVVM.Model.Repository.Role;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.Repository.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.LocalDb.BUS.RoleBUS;

import java.util.List;

public class RoleRepository {
    private RoleService roleService;
    private RoleBUS roleBUS;

    public RoleRepository() {
        roleBUS = new RoleBUS();
        roleService = new RoleService();
    }

    public List<Role> getAll() {
        return new NetworkBoundResource<List<Role>, List<Role>>() {
            @Override
            protected List<Role> loadFromDb() {
                return roleBUS.getAll();
            }

            @Override
            protected boolean shouldFetchData(List<Role> data) {
                return data.isEmpty();
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                roleService.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<Role> data) {
                roleBUS.insertRange(data);
            }

            @Override
            protected void onFetchFail() {

            }

            @Override
            protected void onFetchSuccess(List<Role> data) {

            }
        }.run();
    }
}
