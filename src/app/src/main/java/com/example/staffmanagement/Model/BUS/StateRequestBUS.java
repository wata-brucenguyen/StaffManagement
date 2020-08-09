package com.example.staffmanagement.Model.BUS;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Ultils.StateRequestQuery;

import java.util.List;

public class StateRequestBUS {

    public String getStateNameById(int idState) {
        StateRequest s = AppDatabase.getDb().stateRequestDAO().getById(idState);
        return s.getName();
    }

    public List<StateRequest> getAllStateRequest() {
        List<StateRequest> list = AppDatabase.getDb().stateRequestDAO().getAll();
        return list;
    }

    public int getIdStateByName(String stateName) {
        String q = StateRequestQuery.getIdStateByName(stateName);
        SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
        int idState = AppDatabase.getDb().stateRequestDAO().getIdStateByName(sql);
        return idState;
    }
}
