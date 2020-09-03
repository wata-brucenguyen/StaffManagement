package com.example.staffmanagement.Model.Repository;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Notification.NotificationService;

import java.util.List;

public class DatabaseInitialization {

    public static void initialize() {

//        AppDatabase app = AppDatabase.getDb();
//        List<Role> roleList = app.roleDAO().getAll();
//        if (roleList == null || (roleList != null && roleList.size() == 0)) {
//            app.roleDAO().insertRange(SeedData.getRoleList());
//        }
//
//        List<StateRequest> stateRequestList = app.stateRequestDAO().getAll();
//        if (stateRequestList == null || (stateRequestList != null && stateRequestList.size() == 0)) {
//            app.stateRequestDAO().insertRange(SeedData.getStateList());
//        }
//
//        List<UserState> userStateList =  app.userStateDAO().getAll();
//        if (userStateList == null || (userStateList != null && userStateList.size() == 0)) {
//            app.userStateDAO().insertRange(SeedData.getUserStateList());
//        }
//
//        List<User> userList = app.userDAO().getAll();
//        if (userList == null || (userList != null && userList.size() == 0)) {
//            app.userDAO().insertRange(SeedData.getUserList());
//            for (int j = 7; j <= 43; j++) {
//                User u = new User(0, 2, "Hoàng " + j, "hoang" + j, "123456", "0123444789", "hoang" + j + "@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", new byte[]{}, 1);
//                app.userDAO().insert(u);
//            }
//        }
//
//        List<Request> requestList = app.requestDAO().getAll();
//        if (requestList == null || (requestList != null && requestList.size() == 0)) {
//            app.requestDAO().insertRange(SeedData.getRequestList());
//            for (int j = 1; j <= 50; j++)
//                for (int i = 1; i <= 30; i++) {
//                    Request r = new Request(0, j, 1, "Nghỉ phép " + i, "Tôi muốn nghỉ 1 ngày thứ 6", new Date().getTime());
//                    app.requestDAO().insert(r);
//                }
//        }
    }
}
