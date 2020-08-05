package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Ultils.StateRequestQuery;

import java.util.List;

public class StateRequestBUS {

    public String getStateNameById(Context context, int idState) {
        StateRequest s = AppDatabase.getDb().stateRequestDAO().getById(idState);
        return s.getName();
    }

    public List<StateRequest> getAllStateRequest(Context context) {
        List<StateRequest> list = AppDatabase.getDb().stateRequestDAO().getAll();
        return list;
    }

    public int getIdStateByName(Context context, String stateName) {
        String q = StateRequestQuery.getIdStateByName(stateName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int idState = AppDatabase.getDb().stateRequestDAO().getIdStateByName(sql);
        return idState;
    }
}
