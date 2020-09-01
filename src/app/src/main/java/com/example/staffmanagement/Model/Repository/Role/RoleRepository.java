package com.example.staffmanagement.Model.Repository.Role;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Role.RoleService;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;

import java.util.List;

public class RoleRepository {
    private RoleService roleService;
    private MutableLiveData<List<Role>> mLiveData;

    public RoleRepository() {
        roleService = new RoleService();
        mLiveData = new MutableLiveData<>();
    }

    public void getRoleNameById(int idRole, CallBackFunc<String> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                roleService.getById(idRole, new ApiResponse<Role>() {
                    @Override
                    public void onSuccess(Resource<Role> success) {
                        callBackFunc.onSuccess(success.getData().getName());
                    }

                    @Override
                    public void onLoading(Resource<Role> loading) {

                    }

                    @Override
                    public void onError(Resource<Role> error) {
                        callBackFunc.onError(error.getMessage());
                    }
                });

            }
        }).start();
    }

    public void getAll(CallBackFunc<List<Role>> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                roleService.getAll(new ApiResponse<List<Role>>() {
                    @Override
                    public void onSuccess(Resource<List<Role>> success) {
                        callBackFunc.onSuccess(success.getData());
                    }

                    @Override
                    public void onLoading(Resource<List<Role>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<Role>> error) {

                    }
                });
            }
        }).start();
    }

}
