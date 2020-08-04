package com.example.staffmanagement.View.Admin.DetailRequestUser;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;

import java.util.List;

public interface DetailRequestUserInterface {
    void getIdStateByName(String name);
    void getStateNameById(int idState);
    void update(Request request);
    void onSuccessGetAllStateRequest(List<StateRequest> list);
}
