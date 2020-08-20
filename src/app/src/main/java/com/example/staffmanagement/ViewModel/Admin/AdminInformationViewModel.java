package com.example.staffmanagement.ViewModel.Admin;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import java.util.Collection;
import java.util.List;

public class AdminInformationViewModel extends ViewModel {
    private UserRepository mRepo;
    private RoleRepository mRepoRole;
    private MutableLiveData<User> mUserLD;
    private MutableLiveData<String> mRoleLD;
    private User mUser;

    public AdminInformationViewModel() {
        this.mRepo = new UserRepository();
        this.mRepoRole = new RoleRepository();
        mUserLD = new MutableLiveData<>();
        mRoleLD = new MutableLiveData<>();
    }

    public void setUpUser(User user) {
        mUser = user;
        mUserLD.postValue(mUser);
    }

    public MutableLiveData<User> getUserLD() {
        return mUserLD;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public MutableLiveData<String> getRoleLD() {
        return mRoleLD;
    }

    public void getRoleNameById(){
        mRoleLD.postValue(mRepoRole.getRoleNameById(mUser.getIdRole()));
   }

    public void resetPassword(int idUser) {
        mRepo.resetPassword(idUser);
        mUserLD.postValue(mUser);
    }


    public void update() {
        mRepo.updateUser(mUser);
        mUserLD.postValue(mUser);
    }

    public void changeAvatar(Bitmap bitmap) {
        byte[] bytes = ImageHandler.getByteArrayFromBitmap(bitmap);
        UserSingleTon.getInstance().getUser().setAvatar(bytes);
        mRepo.changeAvatar(UserSingleTon.getInstance().getUser());
        mUserLD.postValue(mUser);
    }

}
