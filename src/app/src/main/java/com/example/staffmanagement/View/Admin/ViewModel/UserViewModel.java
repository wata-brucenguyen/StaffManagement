package com.example.staffmanagement.View.Admin.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private List<User> mUserList = new ArrayList<>();
    private List<UserState> mUserStateList = new ArrayList<>();
    private List<Role> mRoleList =  new ArrayList<>();
    private List<Integer> mQuantityWaitingRequest = new ArrayList<>();
    private List<User> mUserCheckList = new ArrayList<>();

    public List<User> getUserList() {
        return mUserList;
    }

    public List<Integer> getQuantityWaitingRequest() {
        return mQuantityWaitingRequest;
    }

    public void addRangeQuantityWaitingRequest(List<Integer> mQuantityWaitingRequest) {
        this.mQuantityWaitingRequest.addAll(mQuantityWaitingRequest);
    }

    public void addRangeUserList(List<User> mUserList) {
        this.mUserList.addAll(mUserList);
    }

    public List<UserState> getUserStateList() {
        return mUserStateList;
    }

    public List<User> getUserCheckList() {
        return mUserCheckList;
    }

    public void setUserCheckList(List<User> mUserCheckList) {
        this.mUserCheckList = mUserCheckList;
    }

    public void addNewUserStateList(List<UserState> mUserStateList) {
        this.mUserStateList.clear();
        this.mUserStateList.addAll(mUserStateList);
    }

    public List<Role> getRoleList() {
        return mRoleList;
    }

    public void addNewRoleList(List<Role> mRoleList) {
        this.mRoleList.clear();
        this.mRoleList.addAll(mRoleList);
    }

    public void clearList() {
        mUserList.clear();
        mUserCheckList.clear();
    }

    public void insert(User user) {
        mUserList.add(user);
    }

    public void delete(int position) {
        mUserList.remove(position);
    }

    public int updateState(int idUser, int idState) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (idUser == mUserList.get(i).getId()) {
                mUserList.get(i).setIdUserState(idState);
                return i;
            }
        }
        return -1;
    }
}
