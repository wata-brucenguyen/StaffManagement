package com.example.staffmanagement.Model.BUS;

import android.content.Context;

import com.example.staffmanagement.Model.Database.Data.SeedData;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Entity.UserState;

import java.util.ArrayList;

public class DatabaseInitialization {

    public static void initialize(Context context) {
        AppDatabase app = AppDatabase.getInstance(context);
        ArrayList<User> userList = (ArrayList<User>) app.userDAO().getAll();
        if ( userList == null || (userList != null && userList.size() == 0) ) {
            app.userDAO().insertRange(SeedData.getUserList());
        }

        ArrayList<Role> roleList = (ArrayList<Role>) app.roleDAO().getAll();
        if ( roleList == null || (roleList != null && roleList.size() == 0) ) {
            app.roleDAO().insertRange(SeedData.getRoleList());
        }

        ArrayList<StateRequest> stateRequestList = (ArrayList<StateRequest>) app.stateRequestDAO().getAll();
        if ( stateRequestList == null || (stateRequestList != null && stateRequestList.size() == 0) ) {
            app.stateRequestDAO().insertRange(SeedData.getStateList());
        }

        ArrayList<Request> requestList = (ArrayList<Request>) app.requestDAO().getAll();
        if ( requestList == null || (requestList != null && requestList.size() == 0) ) {
            app.requestDAO().insertRange(SeedData.getRequestList());
        }

        ArrayList<UserState> userStateList = (ArrayList<UserState>) app.userStateDAO().getAll();
        if ( userStateList == null || (userStateList != null && userStateList.size() == 0) ) {
            app.userStateDAO().insertRange(SeedData.getUserStateList());
        }
        AppDatabase.onDestroy();
    }
}
