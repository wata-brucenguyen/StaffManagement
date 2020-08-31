package com.example.staffmanagement.ViewModel.Staff;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import com.example.staffmanagement.ViewModel.CallBackFunc;

public class StaffUserProfileVM extends ViewModel {
    private User mUser;
    private MutableLiveData<User> mUserLD;
    private MutableLiveData<String> mRoleNameLD;
    private UserRepository mRepo;

    public StaffUserProfileVM() {
        mRepo = new UserRepository();
        mUser = UserSingleTon.getInstance().getUser();
        mUserLD = new MutableLiveData<>();
        mRoleNameLD = new MutableLiveData<>();
    }

    public void setUpUser() {
        new RoleRepository().getRoleNameById(mUser.getIdRole(), new CallBackFunc<String>() {
            @Override
            public void success(String data) {
                //mUserLD.postValue(mUser);
                mRoleNameLD.setValue(data);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public void updateUserProfile() {

        mRepo.updateUser(mUser, new CallBackFunc<User>() {
            @Override
            public void success(User data) {
                mUserLD.postValue(mUser);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public void changeAvatar(Bitmap bitmap) {

        mRepo.changeAvatarUser(mUser, bitmap, new CallBackFunc<User>() {
            @Override
            public void success(User data) {
                mUser.setAvatar(data.getAvatar());
                mUserLD.postValue(mUser);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public User getUser() {
        return mUser;
    }

    public MutableLiveData<User> getUserLD() {
        return mUserLD;
    }

    public MutableLiveData<String> getRoleNameLD() {
        return mRoleNameLD;
    }

}
