package com.example.staffmanagement.MVVM.Model.Repository.User;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.UserService;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.LocalDb.Database.Ultils.UserQuery;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private UserService service;

    public UserRepository(){
        service = new UserService();
    }

    public void populateData(){
        service.populateData();
    }

    public void updateUser(User user){
        new Thread(() -> AppDatabase.getDb().userDAO().update(user)).start();
    }

    public User getUserForLogin(final int idUser) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            return AppDatabase.getDb().userDAO().getUserById(idUser);
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

    public User getByLoginInformation(String userName, String password) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getUserByUserName(userName);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            User user = AppDatabase.getDb().userDAO().getUserByUserName(sql);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
                return null;
            }
            return null;
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
}
