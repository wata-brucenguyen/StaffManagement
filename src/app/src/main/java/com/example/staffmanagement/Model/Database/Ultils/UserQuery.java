package com.example.staffmanagement.Model.Database.Ultils;

import com.example.staffmanagement.Model.Database.DAL.ConstString;

public class UserQuery {
    public static String getCount(){
        String query = "SELECT COUNT(" + ConstString.USER_COL_ID +") FROM " + ConstString.USER_TABLE_NAME;
        return query;
    }
}
