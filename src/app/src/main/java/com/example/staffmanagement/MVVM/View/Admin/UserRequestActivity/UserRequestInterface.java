package com.example.staffmanagement.MVVM.View.Admin.UserRequestActivity;
import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.View.Data.AdminRequestFilter;

import java.util.List;

public interface UserRequestInterface {
    void onApplyFilter(AdminRequestFilter filter);
    void onCancelDialog();
}
