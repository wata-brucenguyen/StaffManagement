package com.example.staffmanagement.MVVM.ViewModel.Staff;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.MVVM.Model.Repository.User.UserRepository;
import com.example.staffmanagement.MVVM.View.Data.UserSingleTon;
import com.example.staffmanagement.MVVM.View.Ultils.ImageHandler;

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
        mUserLD.postValue(mUser);
        mRoleNameLD.setValue(new RoleRepository().getRoleNameById(mUser.getIdRole()));
    }

    public void updateUserProfile() {
        mUserLD.postValue(mUser);
        mRepo.updateUser(mUser);
    }

    public void changeAvatar(Bitmap bitmap) {
        byte[] bytes = ImageHandler.getByteArrayFromBitmap(bitmap);
        mUser.setAvatar(bytes);
        mUserLD.postValue(mUser);
        mRepo.updateUser(mUser);
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
