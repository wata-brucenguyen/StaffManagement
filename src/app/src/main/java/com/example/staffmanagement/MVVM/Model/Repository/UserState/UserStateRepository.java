package com.example.staffmanagement.MVVM.Model.Repository.UserState;

import com.example.staffmanagement.MVVM.Model.FirebaseDb.UserStateService;
import com.example.staffmanagement.Model.LocalDb.BUS.UserStateBUS;

public class UserStateRepository {
    private UserStateService service;
    private UserStateBUS bus;

    public UserStateRepository() {
        service = new UserStateService();
        bus = new UserStateBUS();
    }

    public List<UserState> getAll() {
        return null;
    }
}

