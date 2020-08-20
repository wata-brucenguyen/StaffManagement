package com.example.staffmanagement.Model.Repository.Role;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.FirebaseDb.RoleService;
import com.example.staffmanagement.Model.Repository.AppDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RoleRepository {
    private RoleService roleService;

    public RoleRepository() {
        roleService = new RoleService();
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

    public List<Role> getAll(){
        CompletableFuture<List<Role>> future = CompletableFuture.supplyAsync(() -> AppDatabase.getDb().roleDAO().getAll());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
