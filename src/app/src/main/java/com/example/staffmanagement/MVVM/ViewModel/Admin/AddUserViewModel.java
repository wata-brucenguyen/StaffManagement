package com.example.staffmanagement.MVVM.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.MVVM.Model.Repository.User.UserRepository;

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
        boolean b = mRepo.checkUserNameIsExisted(user.getUserName());
        if (!b) {
            flag.setValue(FLAG.ADD);
            error.setValue(ERROR.NOT_DUPLICATE);
        } else {
            flag.setValue(FLAG.CHECK_USER);
            error.setValue(ERROR.DUPLICATE);
        }
    }

    public void setFlag(){
        flag.setValue(FLAG.NONE);
        error.setValue(ERROR.NONE);
    }

    public void getAllRole() {
        role.postValue(mRepoRole.getAll());
    }
}
