package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Ultils.StateRequestQuery;

import java.util.List;

public class StateRequestBUS {

    public String getStateNameById(Context context,int idState){
        AppDatabase database = AppDatabase.getInstance(context);
        StateRequest s = database.stateRequestDAO().getById(idState);
        AppDatabase.onDestroy();
        return s.getName();
    }
    public List<StateRequest> getAllStateRequest(Context context){
        AppDatabase database = AppDatabase.getInstance(context);
        List<StateRequest> list=database.stateRequestDAO().getAll();
        AppDatabase.onDestroy();
        return list;
    }

    public int getIdStateByName(Context context, String stateName) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String q = StateRequestQuery.getIdStateByName(stateName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int idState = appDatabase.stateRequestDAO().getIdStateByName(sql);
        AppDatabase.onDestroy();
        return idState;
    }
}
