package com.example.staffmanagement.Model.Database.Ultils;

import com.example.staffmanagement.Model.Database.DAL.ConstString;

public class StateRequestQuery {

    public static String getIdStateByName(String stateName){
        String query = "SELECT " + ConstString.STATE_REQUEST_COL_ID + " FROM "
                + ConstString.STATE_REQUEST_TABLE_NAME + " WHERE " + ConstString.STATE_REQUEST_COL_NAME + " = '" + stateName +"'";
        return query;
    }

    public static String getStateNameById(int idState){
        String query = "SELECT " + ConstString.STATE_REQUEST_COL_NAME + " FROM "
                + ConstString.STATE_REQUEST_TABLE_NAME + " WHERE " + ConstString.STATE_REQUEST_COL_ID + " = '" + idState +"'";
        return query;
    }
}
