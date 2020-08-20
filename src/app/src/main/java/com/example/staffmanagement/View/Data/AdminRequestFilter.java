package com.example.staffmanagement.MVVM.View.Data;

import android.util.Log;

import java.util.ArrayList;

public class AdminRequestFilter {
    public enum STATE {
        Waiting, Decline, Accept;
    }

    public enum SORT {
        None, Title, DateTime
    }

    public enum SORT_TYPE {
        None, ASC, DESC
    }

    private String searchString;
    private ArrayList<AdminRequestFilter.STATE> stateList;
    private long fromDateTime, toDateTime;
    private AdminRequestFilter.SORT sortName;
    private AdminRequestFilter.SORT_TYPE sortType;

    public AdminRequestFilter() {
        searchString = "";
        stateList = new ArrayList<>();
        fromDateTime = 0;
        toDateTime = 0;
        sortName = AdminRequestFilter.SORT.None;
        sortType = AdminRequestFilter.SORT_TYPE.None;
    }

    public AdminRequestFilter(String searchString, ArrayList<AdminRequestFilter.STATE> stateList, long fromDateTime, long toDateTime, AdminRequestFilter.SORT sortName, AdminRequestFilter.SORT_TYPE sortType) {
        this.searchString = searchString;
        this.stateList = stateList;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.sortName = sortName;
        this.sortType = sortType;
    }

    public void dumpToLog(){
        Log.i("FILTER","FILTER : search :" + searchString);
        for(AdminRequestFilter.STATE s : stateList)
            Log.i("FILTER","FILTER : state :" + String.valueOf(s));
        Log.i("FILTER","FILTER : sort - title : " + sortName + ", type : " + sortType);
        Log.i("FILTER","FILTER : DateTime From : " + fromDateTime);
        Log.i("FILTER","FILTER : DateTime To : " + toDateTime);
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public ArrayList<AdminRequestFilter.STATE> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<AdminRequestFilter.STATE> stateList) {
        this.stateList = stateList;
    }

    public long getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(long fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public long getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(long toDateTime) {
        this.toDateTime = toDateTime;
    }

    public AdminRequestFilter.SORT getSortName() {
        return sortName;
    }

    public void setSortName(AdminRequestFilter.SORT sortName) {
        this.sortName = sortName;
    }

    public AdminRequestFilter.SORT_TYPE getSortType() {
        return sortType;
    }

    public void setSortType(AdminRequestFilter.SORT_TYPE sortType) {
        this.sortType = sortType;
    }
}
