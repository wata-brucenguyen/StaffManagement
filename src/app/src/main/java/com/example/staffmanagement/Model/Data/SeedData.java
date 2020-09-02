package com.example.staffmanagement.Model.Data;
import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.ArrayList;
import java.util.Date;
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
        list.add(new User(1, "Hoang", "hoang", GeneralFunc.getMD5("123456"), "0123456789", "hoang@gmail.com", "12/12, Quang Trung, Q.12, TP.HCM", Constant.DEFAULT_AVATAR, SeedData.getRoleList().get(0), SeedData.getUserStateList().get(0)));
        list.add(new User(2, "Triet", "triet", GeneralFunc.getMD5("123456"), "0123444789", "triet@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", Constant.DEFAULT_AVATAR, SeedData.getRoleList().get(0), SeedData.getUserStateList().get(0)));
        list.add(new User(3, "Vuong", "vuong", GeneralFunc.getMD5("123456"), "0123488993", "vuong@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", Constant.DEFAULT_AVATAR, SeedData.getRoleList().get(0), SeedData.getUserStateList().get(0)));
        list.add(new User(4, "Tèo", "teo", GeneralFunc.getMD5("123456"), "0123444789", "teo@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", Constant.DEFAULT_AVATAR, SeedData.getRoleList().get(1), SeedData.getUserStateList().get(0)));
        list.add(new User(5, "Tí", "ti", GeneralFunc.getMD5("123456"), "0123444789", "ti@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", Constant.DEFAULT_AVATAR, SeedData.getRoleList().get(1), SeedData.getUserStateList().get(0)));
        list.add(new User(6, "Sửu", "suu", GeneralFunc.getMD5("123456"), "0123444789", "suu@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", Constant.DEFAULT_AVATAR, SeedData.getRoleList().get(1), SeedData.getUserStateList().get(0)));

        return list;
    }

    public static List<Request> getRequestList(){
        List<Request> list = new ArrayList<>();
        StateRequest st =  new StateRequest(1,"Waiting");
        list.add(new Request(1,4,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6", new Date().getTime(),st,"Tèo"));
        list.add(new Request(2,5,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6",new Date().getTime(),st,"Tí"));
        list.add(new Request(3,6,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6",new Date().getTime(),st,"Sửu"));

        return list;
    }
}
