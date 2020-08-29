package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.ViewModel.CallBackFunc;

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
    private MutableLiveData<List<Role>> mListRoleLD;
    private MutableLiveData<List<UserState>> mListUserStateLD;
    private MutableLiveData<Integer> mCountStaffLD;

    private List<User> mUserList = new ArrayList<>();
    private List<UserState> mUserStateList = new ArrayList<>();
    private List<Role> mRoleList = new ArrayList<>();
    private List<Integer> mQuantityWaitingRequest = new ArrayList<>();
    private List<User> mUserCheckList = new ArrayList<>();


    public UserListViewModel() {
        this.mRepo = new UserRepository();
        this.mRepoRole = new RoleRepository();
        this.mCountStaffLD = new MutableLiveData<>();
        this.mCountStaffLD.postValue(0);
        this.mUserListLD = mRepo.getLiveData();
        this.mListQuantitiesLD = mRepo.getLiveDataQuantities();
        this.mListRoleLD = mRepo.getLiveDataRole();
        this.mListUserStateLD = mRepo.getLiveDataUserState();
        this.mUserCheckListLD = mRepo.getLiveDataUserCheck();

        countStaff();
    }

    public void getLimitListUser(final int offset, final int numRow, final Map<String, Object> mCriteria) {
        mRepo.getLimitListUser(offset, numRow, mCriteria);
    }

    public void insertUser(User user, int idUser, Map<String, Object> mCriteria) {
        mRepo.insert(user, idUser, mUserList.size(), mCriteria);
    }

    public void getAllRoleAndUserState() {
        mRepo.getAllRoleAndUserState();
    }

    public void changeIdUserState(int idUser, int idUserState) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUserList.get(i).getId() == idUser) {
                mUserList.get(i).setIdUserState(idUserState);
                mRepo.changeIdUserState(idUser, idUserState);
                break;
            }
        }
    }

    public void countStaff(){
        mRepo.getCountStaff(new CallBackFunc<Integer>() {
            @Override
            public void success(Integer data) {
                mCountStaffLD.postValue(data);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public MutableLiveData<Integer> getCountStaffLD() {
        return mCountStaffLD;
    }

    public void getAllStaff(){
         mRepo.getAllStaff(new CallBackFunc<List<User>>() {
             @Override
             public void success(List<User> data) {
                 mUserCheckListLD.postValue(data);
             }

             @Override
             public void error(String message) {

             }
         });
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public List<Integer> getQuantityWaitingRequest() {
        return mQuantityWaitingRequest;
    }

    public List<UserState> getUserStateList() {
        return mUserStateList;
    }

    public List<User> getUserCheckList() {
        return mUserCheckList;
    }

    public List<Role> getRoleList() {
        return mRoleList;
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


    public void getAllRole() {
        mRepoRole.getAll(new CallBackFunc<List<Role>>() {
            @Override
            public void success(List<Role> data) {
                mListRoleLD.postValue(data);
            }

            @Override
            public void error(String message) {

            }
        });
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

    public MutableLiveData<List<Role>> getListRoleLD() {
        return mListRoleLD;
    }

    public MutableLiveData<List<UserState>> getListUserStateLD() {
        return mListUserStateLD;
    }

}
