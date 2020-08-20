package com.example.staffmanagement.Model.Repository.StateRequest;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.FirebaseDb.StateRequestService;
import com.example.staffmanagement.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.Ultils.StateRequestQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class StateRequestRepository {
    private StateRequestService service;
    private MutableLiveData<List<StateRequest>> mLiveData;

    public StateRequestRepository() {
        service = new StateRequestService();
        mLiveData = new MutableLiveData<>();
    }

    public void getAll(){
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
            List<StateRequest> list = AppDatabase.getDb().stateRequestDAO().getAll();
            return list;
        }).thenAcceptAsync(stateRequests -> {
            mLiveData.postValue(stateRequests);
        });
    }

    public MutableLiveData<List<StateRequest>> getLiveData() {
        return mLiveData;
    }

    public List<String> getStateName(List<String> stateName){
        for (int i=0;i<mLiveData.getValue().size();i++)
            stateName.add(mLiveData.getValue().get(i).getName());
        return stateName;
    }

    public String getStateNameById(int idState){
        CompletableFuture<String> future =CompletableFuture.supplyAsync(() -> {
            String q=StateRequestQuery.getStateNameById(idState);
            SimpleSQLiteQuery sql=new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().stateRequestDAO().getStateNameById(sql);
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getIdByStateName(String name){
        CompletableFuture<Integer> future =CompletableFuture.supplyAsync(() -> {
            String q=StateRequestQuery.getIdStateByName(name);
            SimpleSQLiteQuery sql=new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().stateRequestDAO().getIdStateByName(sql);
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
