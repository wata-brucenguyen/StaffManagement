package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.Model.Repository.StateRequest.StateRequestRepository;

public class DetailRequestViewModel extends ViewModel {
    private StateRequestRepository mRepository;

    public DetailRequestViewModel() {
        mRepository = new StateRequestRepository();
    }

}
