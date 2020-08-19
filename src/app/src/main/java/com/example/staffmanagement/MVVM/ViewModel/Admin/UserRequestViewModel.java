package com.example.staffmanagement.MVVM.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.MVVM.Model.Repository.StateRequest.StateRequestRepository;
import com.example.staffmanagement.MVVM.Model.Repository.User.UserRepository;
import com.example.staffmanagement.MVVM.View.Data.AdminRequestFilter;

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
        listFullNameLD = userRepository.getListFullName();
    }

    public MutableLiveData<List<String>> getListFullNameLD() {
        return listFullNameLD;
    }

    public void getStateName() {
        stateRequestRepository.getStateName(listRequestState);
    }

    public void getLimitRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {
        requestRepository.getLimitListRequestForUser(idUser, offset, numRow, criteria);
    }

    public List<String> getListFullName() {
        return listFullName;
    }

    public void getStateNameById(int idState) {
        stateRequestRepository.getStateNameById(idState);
    }

    //    public void getAllStateNameList(){
//        stateRequestRepository.getStateName(listFullName);
//    }
    public MutableLiveData<List<Request>> getLimitRequestForUserLD() {
        return requestListLD;
    }

    public int updateRequest(Request request) {
        requestRepository.updateRequest(request);
        return 0;
    }

    public List<Request> getRequestList() {
        return requestList;
    }


    public List<String> getStateRequestNameList() {
        return listRequestState;
    }

    public MutableLiveData<List<StateRequest>> getStateRequestListLD() {
        return stateRequestListLD;
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

    public void addRange(List<Request> list) {
        requestList.addAll(list);
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
