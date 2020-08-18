package com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity;
import com.example.staffmanagement.MVVM.View.Data.StaffRequestFilter;

public interface StaffRequestInterface {
    void onApplyFilter(StaffRequestFilter filter);
    void onCancelFilter();
}
