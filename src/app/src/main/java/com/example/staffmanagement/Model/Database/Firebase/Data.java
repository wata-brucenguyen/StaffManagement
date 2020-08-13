package com.example.staffmanagement.Model.Database.Firebase;

import com.example.staffmanagement.Model.LocalDb.Database.Ultils.ConstString;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Data {
    private static DatabaseReference reference;
    private static FirebaseStorage storage;
    private static DatabaseReference user, role, stateRequest, userState, request;

    private void init() {
        reference = FirebaseDatabase.getInstance().getReference();

        user = reference.child("database").child(ConstString.USER_TABLE_NAME);
        role = reference.child("database").child(ConstString.ROLE_TABLE_NAME);
        stateRequest = reference.child("database").child(ConstString.STATE_REQUEST_TABLE_NAME);
        userState = reference.child("database").child(ConstString.USER_STATE_TABLE_NAME);
        request = reference.child("database").child(ConstString.REQUEST_TABLE_NAME);

    }

    public static ArrayList<Role> getRoleList(String id) {
        ArrayList<Role> roleArrayList = new ArrayList<>();
        roleArrayList.add(new Role(id, "Admin"));
        roleArrayList.add(new Role(id, "Staff"));
        return roleArrayList;
    }

    public static ArrayList<UserState> getUserStateList(String id) {
        ArrayList<UserState> userStateArrayList = new ArrayList<>();
        userStateArrayList.add(new UserState(id, "Active"));
        userStateArrayList.add(new UserState(id, "Lock"));
        return userStateArrayList;
    }

    public static ArrayList<StateRequest> getStateList(String id) {
        ArrayList<StateRequest> stateRequestArrayList = new ArrayList<>();
        stateRequestArrayList.add(new StateRequest(id, "Waiting"));
        stateRequestArrayList.add(new StateRequest(id, "Accept"));
        stateRequestArrayList.add(new StateRequest(id, "Decline"));
        return stateRequestArrayList;
    }

    public static ArrayList<User> getUserList(String id, String idRole, String idUserState) {
        ArrayList<User> userArrayList = new ArrayList<>();

        String linkAvatarDefault = "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fuser.png?alt=media&token=6acc4879-cf04-430b-bd99-d3125de53ea5";
        userArrayList.add(new User(id, idRole, idUserState, "Hoang", "hoang", "123456", "0123456789", "hoang@gmail.com", "12/12, Quang Trung, Q.12, TP.HCM", linkAvatarDefault));
        userArrayList.add(new User(id, idRole, idUserState, "Triet", "triet", "123456", "0123444789", "triet@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", linkAvatarDefault));
        userArrayList.add(new User(id, idRole, idUserState, "Vuong", "vuong", "123456", "0123488993", "vuong@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", linkAvatarDefault));
        userArrayList.add(new User(id, idRole, idUserState, "Tèo", "teo", "123456", "0123444789", "teo@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", linkAvatarDefault));
        userArrayList.add(new User(id, idRole, idUserState, "Tí", "ti", "123456", "0123444789", "ti@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", linkAvatarDefault));
        userArrayList.add(new User(id, idRole, idUserState, "Sửu", "suu", "123456", "0123444789", "suu@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", linkAvatarDefault));

        return userArrayList;
    }

    public static ArrayList<Request> getRequestList(String id, String idUser, String idState) {
        ArrayList<Request> requestArrayList = new ArrayList<>();
        requestArrayList.add(new Request(id, idUser, idState, "Nghỉ phép", "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32")));
        requestArrayList.add(new Request(id, idUser, idState, "Nghỉ phép", "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("13/6/2020 9:00:00")));
        requestArrayList.add(new Request(id, idUser, idState, "Nghỉ phép", "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("03/9/2020 10:00:00")));

        return requestArrayList;
    }
}
