package com.example.staffmanagement.MVVM.View.Admin.DetailRequestUser;

import com.example.staffmanagement.MVVM.Model.Entity.Request;

public interface DetailRequestUserInterface {
    void getIdStateByName(String name);
    void getStateNameById(int idState);
    void update(Request request);
}
