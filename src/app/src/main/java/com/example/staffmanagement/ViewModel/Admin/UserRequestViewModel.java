package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.Model.Repository.StateRequest.StateRequestRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.View.Data.AdminRequestFilter;

import java.util.ArrayList;
import java.util.List;

public class UserRequestViewModel extends ViewModel {
    private RequestRepository requestRepository;
    private UserRepository userRepository;

    private List<Request> requestList = new ArrayList<>();

    private MutableLiveData<List<Request>> requestListLD;

    public UserRequestViewModel() {
        this.requestRepository = new RequestRepository();
        this.userRepository = new UserRepository();
        this.requestListLD = requestRepository.getLiveData();
    }

    public void getLimitRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        requestRepository.getLimitListRequestForUser(idUser, offset, numRow, criteria);
    }

    public int updateRequest(Request request) {
        requestRepository.updateRequest(request);
        for (int i = 0; i < requestList.size(); i++) {
            if (request.getId() == requestList.get(i).getId()) {
                requestList.set(i,request);
                return i;
            }
        }
        return -1;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public MutableLiveData<List<Request>> getRequestListLD() {
        return requestListLD;
    }

    public void insert(Request item) {
        requestList.add(item);
    }

    public void clearList() {
        requestList.clear();
    }

    public void delete(int position) {
        requestList.remove(position);
    }

}
