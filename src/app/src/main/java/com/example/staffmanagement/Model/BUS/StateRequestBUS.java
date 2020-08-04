package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;

import java.util.List;

public class StateRequestBUS {

    public String getStateNameById(Context context,int idState){
        AppDatabase database = AppDatabase.getInstance(context);
        StateRequest s = database.stateRequestDAO().getById(idState);
        database.onDestroy();
        return s.getName();
    }
    public List<StateRequest> getAllStateRequest(Context context){
        AppDatabase database = AppDatabase.getInstance(context);
        List<StateRequest> list=database.stateRequestDAO().getAll();
        return list;
    }
}
