package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.UserBUS;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.Background.UserActUiHandler;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.View.Data.UserSingleTon;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class UserListPresenter {
    private Context mContext;
    private MainAdminInterface mInterface;
    private UserActUiHandler mHandler;

    public UserListPresenter(Context context, MainAdminInterface mInterface) {
        this.mContext = context;
        this.mInterface = mInterface;
        mHandler = new UserActUiHandler(mInterface);
    }

    public void getLimitListUser(final int idUser, final int offset, final int numRow, final Map<String, Object> mCriteria) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBUS bus = new UserBUS();
                ArrayList<User> arrayList = (ArrayList<User>) bus.getLimitListUser(mContext, idUser, offset, numRow, mCriteria);
                mHandler.sendMessage((MyMessage.getMessage(UserActUiHandler.MSG_ADD_LOAD_MORE_LIST, arrayList)));

            }
        }).start();

    }


    public void insertUser(final User user) {
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
        UserBUS bus = new UserBUS();
        User req = bus.insert(mContext, user);
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_ADD_NEW_USER_SUCCESSFULLY, req));
    }

    public String getRoleNameById(final int idRole) {
        RequestBUS bus = new RequestBUS();
        return bus.getRoleNameById(mContext, idRole);
    }

    public int getCountWaitingForRequest(int idUser) {
        RequestBUS bus = new RequestBUS();
        return bus.getCountWaitingForUser(mContext, idUser);
    }

    public void deleteUser(int idUser) {
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
        UserDbHandler db = new UserDbHandler(mContext);
        db.delete(idUser);
//        UserBUS bus = new UserBUS();
//        bus.delete(mContext,user);
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DELETE_USER_SUCCESSFULLY));
    }

    public void changeIdUserState(final int idUser, final int idUserState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                UserBUS bus = new UserBUS();
                bus.changeIdUserState(mContext, idUser, idUserState);
                mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
            }
        }).start();

    }
}
