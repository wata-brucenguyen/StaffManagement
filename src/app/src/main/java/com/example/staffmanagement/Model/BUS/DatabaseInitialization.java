package com.example.staffmanagement.Model.BUS;

import android.util.Log;

import com.example.staffmanagement.Model.Database.Data.SeedData;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Entity.UserState;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.ArrayList;

public class DatabaseInitialization {

    public static void initialize() {
        AppDatabase app = AppDatabase.getDb();

        ArrayList<Role> roleList = (ArrayList<Role>) app.roleDAO().getAll();
        if (roleList == null || (roleList != null && roleList.size() == 0)) {
            app.roleDAO().insertRange(SeedData.getRoleList());
        }

        ArrayList<StateRequest> stateRequestList = (ArrayList<StateRequest>) app.stateRequestDAO().getAll();
        if (stateRequestList == null || (stateRequestList != null && stateRequestList.size() == 0)) {
            app.stateRequestDAO().insertRange(SeedData.getStateList());
        }

        ArrayList<UserState> userStateList = (ArrayList<UserState>) app.userStateDAO().getAll();
        if (userStateList == null || (userStateList != null && userStateList.size() == 0)) {
            app.userStateDAO().insertRange(SeedData.getUserStateList());
        }

        ArrayList<User> userList = (ArrayList<User>) app.userDAO().getAll();
        if (userList == null || (userList != null && userList.size() == 0)) {
            app.userDAO().insertRange(SeedData.getUserList());
            for (int j = 7; j <= 193; j++) {
                User u = new User(0, 2, "Hoàng " + j, "hoang" + j, "123456", "0123444789", "hoang" + j + "@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", new byte[]{}, 1);
                app.userDAO().insert(u);
            }
        }

        ArrayList<Request> requestList = (ArrayList<Request>) app.requestDAO().getAll();
        if (requestList == null || (requestList != null && requestList.size() == 0)) {
            app.requestDAO().insertRange(SeedData.getRequestList());
            for (int j = 1; j <= 200; j++)
                for (int i = 1; i <= 30; i++) {
                    Request r = new Request(0, j, 1, "Nghỉ phép " + i, "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32"));
                    app.requestDAO().insert(r);
                    Log.i("insert_1k", "user " + j + " request " + i);
                }
        }


    }
}
