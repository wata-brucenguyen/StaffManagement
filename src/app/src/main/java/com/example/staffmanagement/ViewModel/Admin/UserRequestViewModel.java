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
    private StateRequestRepository stateRequestRepository;
    private UserRepository userRepository;

    private List<Request> requestList = new ArrayList<>();
    private List<StateRequest> stateRequestList = new ArrayList<>();
    private List<String> listRequestState = new ArrayList<>();
    private List<String> listFullName = new ArrayList<>();

    private MutableLiveData<List<StateRequest>> stateRequestListLD;
    private MutableLiveData<List<Request>> requestListLD;
    private MutableLiveData<List<String>> listFullNameLD;

    public UserRequestViewModel() {
        this.requestRepository = new RequestRepository();
        this.stateRequestRepository = new StateRequestRepository();
        this.userRepository = new UserRepository();
        this.stateRequestListLD = stateRequestRepository.getLiveData();
        this.requestListLD = requestRepository.getLiveData();
        this.listFullNameLD = requestRepository.getFullNameListLD();
    }

    public String getStateNameById(int idState) {
        return stateRequestRepository.getStateNameById(idState);
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


    public List<String> getStateRequestNameList() {
        return listRequestState;
    }


    public List<String> getListFullName() {
        return listFullName;
    }

    public List<String> getListRequestState() {
        return listRequestState;
    }

    public MutableLiveData<List<StateRequest>> getStateRequestListLD() {
        return stateRequestListLD;
    }

    public MutableLiveData<List<Request>> getRequestListLD() {
        return requestListLD;
    }

    public MutableLiveData<List<String>> getListFullNameLD() {
        return listFullNameLD;
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

    public void addRangeStateRequestList(List<StateRequest> mStateRequestList) {
        this.listRequestState.addAll(listRequestState);
        this.listRequestState.clear();
        for (StateRequest s : mStateRequestList)
            listRequestState.add(s.getName());
    }

    public List<StateRequest> getStateRequestList() {
        return stateRequestList;
    }

    public void getAllStateRequest() {
        if (stateRequestList.isEmpty()) {
            stateRequestRepository.getAll();
        }
    }


}
