package com.example.staffmanagement.MVVM.Model.Data;
import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.View.Ultils.GeneralFunc;

import java.util.ArrayList;
import java.util.List;

public class SeedData {

    public static List<Role> getRoleList(){
        List<Role> list = new ArrayList<>();
        list.add(new Role(1,"Admin"));
        list.add(new Role(2,"Staff"));
        return list;
    }

    public static List<UserState> getUserStateList(){
        List<UserState> list = new ArrayList<>();
        list.add(new UserState(1,"Active"));
        list.add(new UserState(2,"Lock"));
        return list;
    }

    public static List<StateRequest> getStateList(){
        List<StateRequest> list = new ArrayList<>();
        list.add(new StateRequest(1,"Waiting"));
        list.add(new StateRequest(2,"Accept"));
        list.add(new StateRequest(3,"Decline"));
        return list;
    }

    public static List<User> getUserList(){
        List<User> list = new ArrayList<>();
        list.add(new User(1,1,"Hoang","hoang","123456","0123456789","hoang@gmail.com","12/12, Quang Trung, Q.12, TP.HCM",new byte[]{},1));
        list.add(new User(2,1,"Triet","triet","123456","0123444789","triet@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM",new byte[]{},1));
        list.add(new User(3,1,"Vuong","vuong","123456","0123488993","vuong@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM",new byte[]{},1));
        list.add(new User(4,2,"Tèo","teo","123456","0123444789","teo@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM",new byte[]{},1));
        list.add(new User(5,2,"Tí","ti","123456","0123444789","ti@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM",new byte[]{},1));
        list.add(new User(6,2,"Sửu","suu","123456","0123444789","suu@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM",new byte[]{},1));

        return list;
    }

    public static List<Request> getRequestList(){
        List<Request> list = new ArrayList<>();
        list.add(new Request(1,4,1,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32")));
        list.add(new Request(2,5,2,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6",GeneralFunc.convertDateStringToLong("13/6/2020 9:00:00")));
        list.add(new Request(3,6,3,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6",GeneralFunc.convertDateStringToLong("03/9/2020 10:00:00")));

        return list;
    }
}
