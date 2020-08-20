package com.example.staffmanagement.View.Admin.UserRequestActivity;
import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.View.Data.AdminRequestFilter;

import java.util.List;

public interface UserRequestInterface {
    void onApplyFilter(AdminRequestFilter filter);
    void onCancelDialog();
}
