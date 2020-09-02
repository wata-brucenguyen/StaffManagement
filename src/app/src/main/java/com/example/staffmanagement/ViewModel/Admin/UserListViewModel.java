package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserListViewModel extends ViewModel {
    private UserRepository mRepo;
    private RoleRepository mRepoRole;
    //LiveData
    private MutableLiveData<List<User>> mUserListLD;
    private MutableLiveData<List<User>> mUserCheckListLD;
    private MutableLiveData<List<Integer>> mListQuantitiesLD;
    private MutableLiveData<Integer> mCountStaffLD;

    private List<User> mUserList = new ArrayList<>();
    private List<Integer> mQuantityWaitingRequest = new ArrayList<>();
    private List<User> mUserCheckList = new ArrayList<>();


    public UserListViewModel() {
        this.mRepo = new UserRepository();
        this.mRepoRole = new RoleRepository();
        this.mCountStaffLD = new MutableLiveData<>();
        this.mCountStaffLD.postValue(0);
        this.mUserListLD = mRepo.getLiveData();
        this.mListQuantitiesLD = mRepo.getLiveDataQuantities();
        this.mUserCheckListLD = mRepo.getLiveDataUserCheck();

        countStaff();
    }

    public void getLimitListUser(final int offset, final int numRow, final Map<String, Object> mCriteria) {
        mRepo.getLimitListUser(offset, numRow, mCriteria);
    }

    public void insertUser(User user, int idUser, Map<String, Object> mCriteria) {
        mRepo.insert(user, idUser, mUserList.size(), mCriteria);
    }

    public void changeIdUserState(int idUser, int idUserState) {

        mRepo.changeIdUserState(idUser, idUserState, new CallBackFunc<User>() {
            @Override
            public void onSuccess(User data) {
                for (int i = 0; i < mUserList.size(); i++) {
                    if (mUserList.get(i).getId() == idUser) {
                        mUserList.set(i, data);
                        break;
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countStaff() {
        mRepo.getCountStaff(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mCountStaffLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public MutableLiveData<Integer> getCountStaffLD() {
        return mCountStaffLD;
    }

    public void getAllStaff() {
        mRepo.getAllStaff(new CallBackFunc<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                mUserCheckListLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public List<Integer> getQuantityWaitingRequest() {
        return mQuantityWaitingRequest;
    }

    public List<User> getUserCheckList() {
        return mUserCheckList;
    }

    public void clearList() {
        mUserList.clear();
    }

    public void insert(User user) {
        mUserList.add(user);
    }

    public void delete(int position) {
        mUserList.remove(position);
    }

    public MutableLiveData<List<User>> getUserListLD() {
        return mUserListLD;
    }

    public MutableLiveData<List<User>> getUserCheckListLD() {
        return mUserCheckListLD;
    }

    public MutableLiveData<List<Integer>> getListQuantitiesLD() {
        return mListQuantitiesLD;
    }

}
