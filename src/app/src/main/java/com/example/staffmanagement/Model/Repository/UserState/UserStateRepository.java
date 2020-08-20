package com.example.staffmanagement.Model.Repository.UserState;

import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.FirebaseDb.UserStateService;

import java.util.List;

public class UserStateRepository {
    private UserStateService service;

    public UserStateRepository() {
        service = new UserStateService();
    }

    public List<UserState> getAll() {
        return null;
    }
}

