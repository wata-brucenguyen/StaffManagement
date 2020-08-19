package com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemRequestBinding {
    private Request request;
    private String StateRequest;

    public String convertMilliSecToDateString(long milliSecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(milliSecond);
        return format.format(date);
    }

    public ItemRequestBinding(Request request, String stateRequest) {
        this.request = request;
        this.StateRequest = stateRequest;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getStateRequest() {
        return StateRequest;
    }

    public void setStateRequest(String stateRequest) {
        StateRequest = stateRequest;
    }
}
