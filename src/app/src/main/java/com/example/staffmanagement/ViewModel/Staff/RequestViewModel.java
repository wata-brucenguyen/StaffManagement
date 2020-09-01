package com.example.staffmanagement.ViewModel.Staff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.Model.Repository.StateRequest.StateRequestRepository;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;
import java.util.List;

public class RequestViewModel extends ViewModel {
    private RequestRepository mRepo;
    private StateRequestRepository mStRequestRepo;
    private List<StateRequest> mStateRequestList = new ArrayList<>();
    private List<Request> mListRequest = new ArrayList<>();
    private MutableLiveData<List<Request>> mRequestListLD;
    private MutableLiveData<List<StateRequest>> mStateRequestListLD;

    public RequestViewModel() {
        this.mRepo = new RequestRepository();
        this.mStRequestRepo = new StateRequestRepository();
        this.mRequestListLD = mRepo.getLiveData();
        this.mStateRequestListLD = mStRequestRepo.getLiveData();
    }

    public List<StateRequest> getStateRequestList() {
        return mStateRequestList;
    }

    public void insertNewStateRequestList(List<StateRequest> list) {
        mStateRequestList.clear();
        mStateRequestList.addAll(list);
    }

    public MutableLiveData<List<Request>> getRequestListLD() {
        return mRequestListLD;
    }

    public MutableLiveData<List<StateRequest>> getStateRequestListLD() {
        return mStateRequestListLD;
    }

    public List<Request> getListRequest() {
        return mListRequest;
    }

    public void addRange(List<Request> list) {
        mListRequest.addAll(list);
    }

    public void clearList() {
        mListRequest.clear();
    }

    public void insert(Request request) {
        mListRequest.add(request);
    }

    public void restoreRequest(Request request) {
        mRepo.restoreRequest(request);
    }

    public int update(Request item) {
        mRepo.updateRequest(item);
        for (int i = 0; i < mListRequest.size(); i++) {
            if (item.getId() == mListRequest.get(i).getId()) {
                mListRequest.set(i, item);

                return i;
            }
        }
        return -1;
    }

    public void delete(int position) {
        mListRequest.remove(position);
    }

    public void getLimitListRequestForUser(final int idUser, final int offset, final int numRow, final StaffRequestFilter criteria) {
        mRepo.getLimitListRequestForStaffLD(idUser, offset, numRow, criteria);
    }

    public void addNewRequest(Request request, int idUser, StaffRequestFilter filter) {
        mRepo.insert(request, idUser, mListRequest.size(), filter);
    }

    public void deleteRequest(Request request) {
        mRepo.deleteRequest(request);
    }

    public void getAllStateRequest() {
        if (mStateRequestList.isEmpty()) {
            mStRequestRepo.getAll();
        }
    }
}
