package com.example.staffmanagement.ViewModel.Admin;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;

import java.util.List;

public class AddUserViewModel extends ViewModel {
    private UserRepository mRepo;
    private RoleRepository mRepoRole;
    private MutableLiveData<FLAG> flag;
    private MutableLiveData<ERROR> error;
    private MutableLiveData<List<Role>> role ;
    private MutableLiveData<List<String>> string ;

    public AddUserViewModel() {
        this.mRepo = new UserRepository();
        this.mRepoRole = new RoleRepository();
        this.role = new MutableLiveData<>();
        this.string = new MutableLiveData<>();
        this.flag = new MutableLiveData<>();
        this.error = new MutableLiveData<>();
        flag.setValue(FLAG.NONE);
        error.setValue(ERROR.NONE);
    }

    public enum FLAG {
        NONE, ADD, CHECK_USER
    }

    public enum ERROR {
        NONE, DUPLICATE, NOT_DUPLICATE;
    }

    public MutableLiveData<List<Role>> getRole() {
        return role;
    }

    public MutableLiveData<List<String>> getString() {
        return string;
    }

    public MutableLiveData<FLAG> getFlag() {
        return flag;
    }

    public MutableLiveData<ERROR> getError() {
        return error;
    }



    public void checkUserNameIsExisted(User user) {
        mRepo.checkUserNameIsExisted(user.getUserName(), new CallBackFunc<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                Log.i("ADDUSER","success " +data.toString());
                if (!data) {
                    flag.setValue(FLAG.ADD);
                    error.setValue(ERROR.NOT_DUPLICATE);
                } else {
                    flag.setValue(FLAG.CHECK_USER);
                    error.setValue(ERROR.DUPLICATE);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void setFlag(){
        flag.setValue(FLAG.NONE);
        error.setValue(ERROR.NONE);
    }

    public void getAllRole() {
        mRepoRole.getAll(new CallBackFunc<List<Role>>() {
            @Override
            public void onSuccess(List<Role> data) {
                role.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
