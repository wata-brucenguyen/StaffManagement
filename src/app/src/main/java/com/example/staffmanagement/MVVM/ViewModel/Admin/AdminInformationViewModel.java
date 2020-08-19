package com.example.staffmanagement.MVVM.ViewModel.Admin;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Repository.User.UserRepository;
import com.example.staffmanagement.MVVM.View.Data.UserSingleTon;
import com.example.staffmanagement.MVVM.View.Ultils.ImageHandler;

import java.util.List;

public class AdminInformationViewModel extends ViewModel {
    private UserRepository mRepo;
    private MutableLiveData<List<Role>> mListRoleLD;
    private MutableLiveData<User> mUserLD;
    private String mRole;
    private User mUser;

    public AdminInformationViewModel() {
        this.mRepo = new UserRepository();
        mUserLD = new MutableLiveData<>();
    }

    public void setUpUser(User user) {
        mUser = user;
        mUserLD.postValue(mUser);
    }

    public MutableLiveData<List<Role>> getListRoleLD() {
        return mListRoleLD;
    }

    public MutableLiveData<User> getUserLD() {
        return mUserLD;
    }

   public void getRoleNameById(int idUser){

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
