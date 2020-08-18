package com.example.staffmanagement.MVVM.ViewModel.Staff;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemStaffRequestVM extends ViewModel {
    private Request request;
    private StateRequest stateRequest;

    public String convertMilliSecToDateString(long milliSecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(milliSecond);
        return format.format(date);
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public StateRequest getStateRequest() {
        return stateRequest;
    }

    public void setStateRequest(StateRequest stateRequest) {
        this.stateRequest = stateRequest;
    }
}
