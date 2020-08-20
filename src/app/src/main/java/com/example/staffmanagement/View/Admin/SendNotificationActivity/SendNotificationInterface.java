package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import com.example.staffmanagement.Model.Entity.User;

public interface SendNotificationInterface {
    void setCheckAll(boolean b);
    void loadBottomSheetDialog(User user);
    void showMessage(String message);
    void changeQuantity();
}
