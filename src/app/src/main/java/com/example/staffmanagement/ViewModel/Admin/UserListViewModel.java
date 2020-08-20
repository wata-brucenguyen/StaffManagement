package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;

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

    private List<User> mUserList = new ArrayList<>();
    private List<UserState> mUserStateList = new ArrayList<>();
    private List<Role> mRoleList = new ArrayList<>();
    private List<Integer> mQuantityWaitingRequest = new ArrayList<>();
    private List<User> mUserCheckList = new ArrayList<>();


    public UserListViewModel() {
        this.mRepo = new UserRepository();
        this.mRepoRole = new RoleRepository();
        this.mUserListLD = mRepo.getLiveData();
        this.mListQuantitiesLD = mRepo.getLiveDataQuantities();
        this.mListRoleLD = mRepo.getLiveDataRole();
        this.mListUserStateLD = mRepo.getLiveDataUserState();
        this.mUserCheckListLD = mRepo.getLiveDataUserCheck();
    }

    public void getLimitListUser(final int idUser, final int offset, final int numRow, final Map<String, Object> mCriteria) {
        mRepo.getLimitListUser(idUser, offset, numRow, mCriteria);
    }

    public void insertUser(User user, int idUser, Map<String, Object> mCriteria) {
        mRepo.insert(user, idUser, mUserList.size(), mCriteria);
    }

    public void getAllRoleAndUserState() {
        mRepo.getAllRoleAndUserState();
    }

    public void changeIdUserState(int idUser, int idUserState) {
        mRepo.changeIdUserState(idUser, idUserState);
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

    public void addNewUserStateList(List<UserState> mUserStateList) {
        this.mUserStateList.clear();
        this.mUserStateList.addAll(mUserStateList);
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

    public void getAllRole() {
        mListRoleLD.postValue(mRepoRole.getAll());
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
