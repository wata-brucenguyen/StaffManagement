package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.Model.Repository.StateRequest.StateRequestRepository;

public class DetailRequestViewModel extends ViewModel {
    private StateRequestRepository mRepository;
    private RequestRepository requestRepository;

    public DetailRequestViewModel() {
        mRepository = new StateRequestRepository();
    }

    public void getIdStateByName(String stateName) {
        mRepository.getIdByStateName(stateName);
    }

    public void getStateNameById(int idState) {
        mRepository.getStateNameById(idState);
    }
    public int updateRequest(Request request) {
        requestRepository.updateRequest(request);
        return 0;
    }
}
