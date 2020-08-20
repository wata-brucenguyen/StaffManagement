package com.example.staffmanagement.View.Admin.UserRequestActivity;

import com.example.staffmanagement.View.Data.AdminRequestFilter;

import java.util.List;

public interface UserRequestInterface {
    void onApplyFilter(AdminRequestFilter filter);
    void onCancelDialog();
}
