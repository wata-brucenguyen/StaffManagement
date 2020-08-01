package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import com.example.staffmanagement.Model.Database.Entity.StateRequest;

public class StateRequestBUS {

    public String getStateNameById(Context context,int idState){
        AppDatabase database = AppDatabase.getInstance(context);
        StateRequest s = database.stateRequestDAO().getById(idState);
        database.onDestroy();
        return s.getName();
    }
}
