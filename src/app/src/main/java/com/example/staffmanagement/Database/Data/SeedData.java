package com.example.staffmanagement.Database.Data;

import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.StateRequest;
import com.example.staffmanagement.Database.Entity.User;

import java.util.ArrayList;

public class SeedData {

    public static ArrayList<Role> getRoleList(){
        ArrayList<Role> list = new ArrayList<>();
        list.add(new Role(1,"Admin"));
        list.add(new Role(2,"Staff"));
        return list;
    }

    public static ArrayList<StateRequest> getStateList(){
        ArrayList<StateRequest> list = new ArrayList<>();
        list.add(new StateRequest(1,"Waiting for responding"));
        list.add(new StateRequest(2,"Approved"));
        list.add(new StateRequest(3,"Declined"));
        return list;
    }

    public static ArrayList<User> getUserList(){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(1,1,"Hoang","hoang","123456","0123456789","hoang@gmail.com","12/12, Quang Trung, Q.12, TP.HCM","12/2/1995"));
        list.add(new User(2,1,"Triet","triet","123456","0123444789","triet@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM","9/5/1993"));
        list.add(new User(3,1,"Vuong","vuong","123456","0123488993","vuong@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM","3/6/1996"));
        list.add(new User(4,2,"Tèo","teo","123456","0123444789","teo@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM","9/6/1992"));
        list.add(new User(5,2,"Tì","ti","123456","0123444789","ti@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM","9/6/1992"));
        list.add(new User(6,2,"Sửu","suu","123456","0123444789","suu@gmail.com","45/3D, Quang Trung, Q.Gò Vấp, TP.HCM","9/6/1992"));

        return list;
    }

    public static ArrayList<Request> getRequestList(){
        ArrayList<Request> list = new ArrayList<>();
        list.add(new Request(1,4,1,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6","23/5/2020 8:00"));
        list.add(new Request(2,5,2,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6","13/6/2020 9:00"));
        list.add(new Request(3,6,3,"Nghỉ phép","Tôi muốn nghỉ 1 ngày thứ 6","03/9/2020 10:00"));

        return list;
    }
}
