package com.example.staffmanagement.ViewModel.Staff;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Repository.RoleRepository;
import com.example.staffmanagement.Model.Repository.UserRepository;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;

public class StaffUserProfileVM extends ViewModel {
    private User mUser;
    private MutableLiveData<User> mUserLD;
    private UserRepository mRepo;

    public StaffUserProfileVM() {
        mRepo = new UserRepository();
        mUser = UserSingleTon.getInstance().getUser();
        mUserLD = new MutableLiveData<>();
    }

    public void updateUserProfile() {

        mRepo.updateUser(mUser, new CallBackFunc<User>() {
            @Override
            public void onSuccess(User data) {
                mUserLD.postValue(mUser);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void changeAvatar(Bitmap bitmap) {

        mRepo.changeAvatarUser(mUser, bitmap, new CallBackFunc<User>() {
            @Override
            public void onSuccess(User data) {
                mUser.setAvatar(data.getAvatar());
                mUserLD.postValue(mUser);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public User getUser() {
        return mUser;
    }

    public MutableLiveData<User> getUserLD() {
        return mUserLD;
    }

}
