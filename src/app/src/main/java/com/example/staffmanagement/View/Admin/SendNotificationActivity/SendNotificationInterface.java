package com.example.staffmanagement.MVVM.View.Admin.SendNotificationActivity;

import com.example.staffmanagement.MVVM.Model.Entity.User;

public interface SendNotificationInterface {
    void setCheckAll(boolean b);
    void loadBottomSheetDialog(User user);
    void showMessage(String message);
    void changeQuantity();
}
