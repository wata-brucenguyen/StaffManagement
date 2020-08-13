package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.Model.LocalDb.BUS.RequestBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.RoleBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.UserBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.UserStateBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;
import com.example.staffmanagement.Presenter.Admin.Background.UserActUiHandler;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserListPresenter {
    private Context mContext;
    private MainAdminInterface mInterface;
    private UserActUiHandler mHandler;

    public UserListPresenter(Context context, MainAdminInterface mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
        mHandler = new UserActUiHandler(mInterface);
    }

    public void getLimitListUser(final int idUser, final int offset, final int numRow, final Map<String, Object> mCriteria) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBUS bus = new UserBUS();
                final ArrayList<User> listUser = (ArrayList<User>) bus.getLimitListUser(idUser, offset, numRow, mCriteria);
                final ArrayList<Integer> quantities = new ArrayList<>();
                for (int i = 0; i < listUser.size(); i++) {
                    int count = new RequestBUS().getQuantityWaitingRequestForUser(listUser.get(i).getId());
                    quantities.add(count);
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onLoadMoreListSuccess(listUser, quantities);
                    }
                });
            }
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

    public void getAllRoleAndUserState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Role> roles = new RoleBUS().getAll();
                List<UserState> userStates = new UserStateBUS().getAll();
                mInterface.onSuccessGetAllRoleAndUserState(roles, userStates);
            }
        }).start();
    }

    public void changeIdUserState(final int idUser, final int idUserState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                UserBUS bus = new UserBUS();
                bus.changeIdUserState(idUser, idUserState);
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
            }
        }).start();

    }
}
