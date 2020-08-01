package com.example.staffmanagement.View.Staff.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;

public class RequestFilterViewModel extends ViewModel {
    private MutableLiveData<StaffRequestFilter> mFilter;

    public MutableLiveData<StaffRequestFilter> getFilterLiveData() {
        if(mFilter == null )
            mFilter = new MutableLiveData<>();
        return mFilter;
    }

    public void setFilterData(StaffRequestFilter filter){
        mFilter.setValue(filter);
    }

    public StaffRequestFilter getFilterData(){
        if(this.mFilter.getValue() == null)
            this.mFilter.setValue(new StaffRequestFilter());
        return mFilter.getValue();
    }

    public void setSearchStringFilter(String search){
        StaffRequestFilter temp = mFilter.getValue();
        temp.setSearchString(search);
        mFilter.setValue(temp);
    }

}
