package com.example.staffmanagement.MVVM.Model.Repository.UserState;

import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.UserStateService;
import com.example.staffmanagement.Model.LocalDb.BUS.UserStateBUS;

import java.util.List;

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