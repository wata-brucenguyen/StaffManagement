package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.Background.UserActUiHandler;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.View.Data.UserSingleTon;

import java.util.ArrayList;
import java.util.Map;

public class UserListPresenter {
    private Context mContext;
    private MainAdminInterface mInterface;
    private UserActUiHandler mHandler;

    public UserListPresenter(Context mContext, MainAdminInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        mHandler = new UserActUiHandler(mInterface);
    }

    public void getLimitListUser(final int idUser, final int offset, final int numRow, final Map<String, Object> mCriteria) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDbHandler db = new UserDbHandler(mContext);
                final ArrayList<User> arrayList = db.getLimitListUser(idUser, offset, numRow, mCriteria);

                mHandler.sendMessage((MyMessage.getMessage(UserActUiHandler.MSG_ADD_LOAD_MORE_LIST,arrayList)));

            }
        }).start();

    }

    public ArrayList<User> findFullName(int idUser, String name) {
        UserDbHandler db = new UserDbHandler(mContext);
        return db.findRequestByFulName(idUser, name);
    }

    public void insertUser(User user) {
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
        UserDbHandler db = new UserDbHandler(mContext);
        User req = db.insert(user);
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_ADD_NEW_USER_SUCCESSFULLY,req));
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getRoleNameById(idRole);
    }

    public int getCountWaitingForRequest(int idUser) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getCountWaitingForUser(idUser);
    }

    public void deleteUser(int idUser) {
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
        UserDbHandler db = new UserDbHandler(mContext);
        db.delete(idUser);
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
        mHandler.sendMessage(MyMessage.getMessage(UserActUiHandler.MSG_DELETE_USER_SUCCESSFULLY));
    }




}
