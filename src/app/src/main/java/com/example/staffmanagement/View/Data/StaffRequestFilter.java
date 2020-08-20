package com.example.staffmanagement.View.Data;

import android.util.Log;

import java.util.ArrayList;

public class StaffRequestFilter {
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
    private ArrayList<STATE> stateList;
    private long fromDateTime, toDateTime;
    private SORT sortName;
    private SORT_TYPE sortType;

    public StaffRequestFilter() {
        searchString = "";
        stateList = new ArrayList<>();
        fromDateTime = 0;
        toDateTime = 0;
        sortName = SORT.None;
        sortType = SORT_TYPE.None;
    }

    public StaffRequestFilter(String searchString, ArrayList<STATE> stateList, long fromDateTime, long toDateTime, SORT sortName, SORT_TYPE sortType) {
        this.searchString = searchString;
        this.stateList = stateList;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.sortName = sortName;
        this.sortType = sortType;
    }

    public void dumpToLog(){
        Log.i("FILTER","FILTER : search :" + searchString);
        for(STATE s : stateList)
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

    public ArrayList<STATE> getStateList() {
        return stateList;
    }

    public void setStateList(ArrayList<STATE> stateList) {
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

    public SORT getSortName() {
        return sortName;
    }

    public void setSortName(SORT sortName) {
        this.sortName = sortName;
    }

    public SORT_TYPE getSortType() {
        return sortType;
    }

    public void setSortType(SORT_TYPE sortType) {
        this.sortType = sortType;
    }
}
