package com.example.staffmanagement.Model.LocalDb.BUS;

import android.util.Log;

import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Request;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.StateRequest.StateRequestRepository;
import com.example.staffmanagement.Model.Repository.User.UserRepository;
import com.example.staffmanagement.Model.Repository.UserState.UserStateRepository;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInitialization {

    public static void initialize() {

        new RoleRepository().getAll();
        new UserStateRepository().getAll();
        new StateRequestRepository().getAll();
        new RequestRepository().getAll();
        new UserRepository().getAll();

//        List<Request> list = requestRepository.getAll();
//        for(int i =0 ;i<list.size();i++){
//            Log.i("Fetch",list.get(i).getTitle());
//        }

//        AppDatabase app = AppDatabase.getDb();
//        List<Role> roleList = (ArrayList<Role>) app.roleDAO().getAll();
//        if (roleList == null || (roleList != null && roleList.size() == 0)) {
//            app.roleDAO().insertRange(SeedData.getRoleList());
//        }
//
//        List<StateRequest> stateRequestList = (ArrayList<StateRequest>) app.stateRequestDAO().getAll();
//        if (stateRequestList == null || (stateRequestList != null && stateRequestList.size() == 0)) {
//            app.stateRequestDAO().insertRange(SeedData.getStateList());
//        }
//
//        ArrayList<UserState> userStateList = (ArrayList<UserState>) app.userStateDAO().getAll();
//        if (userStateList == null || (userStateList != null && userStateList.size() == 0)) {
//            app.userStateDAO().insertRange(SeedData.getUserStateList());
//        }
//
//        List<User> userList = (ArrayList<User>) app.userDAO().getAll();
//        if (userList == null || (userList != null && userList.size() == 0)) {
//            app.userDAO().insertRange(SeedData.getUserList());
//            for (int j = 7; j <= 43; j++) {
//                User u = new User(0, 2, "Hoàng " + j, "hoang" + j, "123456", "0123444789", "hoang" + j + "@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", new byte[]{}, 1);
//                app.userDAO().insert(u);
//            }
//        }
//
//        List<Request> requestList = (ArrayList<Request>) app.requestDAO().getAll();
//        if (requestList == null || (requestList != null && requestList.size() == 0)) {
//            app.requestDAO().insertRange(SeedData.getRequestList());
//            for (int j = 1; j <= 50; j++)
//                for (int i = 1; i <= 30; i++) {
//                    Request r = new Request(0, j, 1, "Nghỉ phép " + i, "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32"));
//                    app.requestDAO().insert(r);
//                    Log.i("insert_1k", "user " + j + " request " + i);
//                }
//        }


    }
}
