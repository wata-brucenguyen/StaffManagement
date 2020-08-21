package com.example.staffmanagement.Model.Repository.Role;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.FirebaseDb.Role.RoleService;
import com.example.staffmanagement.Model.Repository.AppDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RoleRepository {
    private RoleService roleService;
    private MutableLiveData<List<Role>> mLiveData;

    public RoleRepository() {
        roleService = new RoleService();
        mLiveData = new MutableLiveData<>();
    }

    public String getRoleNameById(int idRole) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Role role = AppDatabase.getDb().roleDAO().getById(idRole);
            return role.getName();
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

    public List<Role> getAll() {
        CompletableFuture<List<Role>> future = CompletableFuture.supplyAsync(() -> AppDatabase.getDb().roleDAO().getAll());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getAllService() {
        new NetworkBoundResource<List<Role>, List<Role>>() {
            @Override
            protected List<Role> loadFromDb() {
                return AppDatabase.getDb().roleDAO().getAll();
            }

            @Override
            protected boolean shouldFetchData(List<Role> data) {
                return data == null || data.size() == 0;
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                roleService.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<Role> data) {
                int count = AppDatabase.getDb().requestDAO().count();
                if (count != data.size()) {
                    AppDatabase.getDb().roleDAO().deleteAll();
                    AppDatabase.getDb().roleDAO().insertRange(data);
                }
            }

            @Override
            protected void onFetchFail(String message) {
            }

            @Override
            protected void onFetchSuccess(List<Role> data) {
                mLiveData.postValue(data);
            }
        }.run();
    }
}
