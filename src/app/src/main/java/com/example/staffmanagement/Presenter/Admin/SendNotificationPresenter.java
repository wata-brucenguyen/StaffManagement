package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.Model.LocalDb.BUS.RequestBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.RoleBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.UserBUS;
import com.example.staffmanagement.Presenter.Admin.Background.SendNotificationUIHandler;
import com.example.staffmanagement.Presenter.Admin.Background.UserActUiHandler;
import com.example.staffmanagement.MVVM.View.Ultils.MyMessage;
import com.example.staffmanagement.MVVM.View.Admin.SendNotificationActivity.SendNotificationInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendNotificationPresenter {
    private Context mContext;
    private SendNotificationInterface mInterface;
    private SendNotificationUIHandler mHandler;

    public SendNotificationPresenter(Context context, SendNotificationInterface mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
        mHandler = new SendNotificationUIHandler(mInterface);
    }

    public void getLimitListUser(final int idUser, final int offset, final int numRow, final Map<String, Object> mCriteria) {
        new Thread(() -> {
            UserBUS bus = new UserBUS();
            final List<User> listUser = bus.getLimitListUser(idUser, offset, numRow, mCriteria);
            final List<Integer> quantities = new ArrayList<>();
            for (int i = 0; i < listUser.size(); i++) {
                int count = new RequestBUS().getQuantityWaitingRequestForUser(listUser.get(i).getId());
                quantities.add(count);
            }
            ((Activity) mContext).runOnUiThread(() ->
                    mInterface.onLoadMoreListSuccess(listUser));
        }).start();

    }


    public void insertUser(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                UserBUS bus = new UserBUS();
                User req = bus.insert(user);
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_ADD_NEW_USER_SUCCESSFULLY, req));
            }
        }).start();

    }

    public void getAllRole() {
        new Thread(() -> {
            List<Role> roles = new RoleBUS().getAll();
            mInterface.onSuccessGetAllRole(roles);
        }).start();
    }
}
