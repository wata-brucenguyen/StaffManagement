package com.example.staffmanagement.Model.Database.Ultils;

import com.example.staffmanagement.Model.Database.DAL.ConstString;

public class UserQuery {
    public static String getCount(){
        String query = "SELECT COUNT(" + ConstString.USER_COL_ID +") FROM " + ConstString.USER_TABLE_NAME;
        return query;
    }

    public static String getAll(){
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        return query;
    }

    public static String getAllRole(){
        String query = "SELECT * FROM " + ConstString.ROLE_TABLE_NAME;
        return query;
    }

    public static String getById(int id){
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME + " WHERE " + ConstString.USER_COL_ID + " = " + id;
        return  query;
    }


}
