package com.example.staffmanagement.Presenter.Staff;

import android.content.Context;

import com.example.staffmanagement.View.Staff.Home.StaffHomeInterface;

public class StaffHomePresenter {
    private StaffHomeInterface mInterface;
    private Context mContext;

    public StaffHomePresenter(StaffHomeInterface mInterface, Context mContext) {
        this.mInterface = mInterface;
        this.mContext = mContext;
    }
}
