package com.example.staffmanagement.View.Admin.DetailRequestUser;

import com.example.staffmanagement.Model.Entity.Request;

public interface DetailRequestUserInterface {
    void getIdStateByName(String name);
    void getStateNameById(int idState);
    void update(Request request);
}
