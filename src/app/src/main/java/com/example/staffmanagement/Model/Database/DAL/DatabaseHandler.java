package com.example.staffmanagement.Model.Database.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StaffManagement";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.i("DATABASE","START");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createTables(SQLiteDatabase sqLiteDatabase){
        Log.i("DATABASE","CREATE TABLE");
        createTableRole(sqLiteDatabase);
        createTableStateRequest(sqLiteDatabase);
        createTableUser(sqLiteDatabase);
        createTableRequest(sqLiteDatabase);
    }

    private void createTableRole(SQLiteDatabase sqLiteDatabase){
        Log.i("DATABASE","CREATE TABLE ROLE");
        String query = "CREATE TABLE IF NOT EXISTS " + ConstString.ROLE_TABLE_NAME + "("
                + ConstString.ROLE_COL_ID + " INTEGER PRIMARY KEY,"
                + ConstString.ROLE_COL_NAME + " TEXT " + ")";

        sqLiteDatabase.execSQL(query);
    }

    private void createTableStateRequest(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE IF NOT EXISTS " + ConstString.STATE_REQUEST_TABLE_NAME + "("
                + ConstString.STATE_REQUEST_COL_ID + " INTEGER PRIMARY KEY,"
                + ConstString.STATE_REQUEST_COL_NAME + " TEXT " + ")";

        sqLiteDatabase.execSQL(query);
    }

    private void createTableUser(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE IF NOT EXISTS " + ConstString.USER_TABLE_NAME + "("
                + ConstString.USER_COL_ID + " INTEGER PRIMARY KEY,"
                + ConstString.USER_COL_ID_ROLE + " INTEGER,"
                + ConstString.USER_COL_FULL_NAME + " TEXT,"
                + ConstString.USER_COL_USERNAME + " TEXT,"
                + ConstString.USER_COL_PASSWORD + " TEXT,"
                + ConstString.USER_COL_PHONE_NUMBER + " TEXT,"
                + ConstString.USER_COL_EMAIL + " TEXT,"
                + ConstString.USER_COL_ADDRESS + " TEXT,"
                + ConstString.USER_COL_BIRTHDAY + " TEXT " + ")";

        sqLiteDatabase.execSQL(query);
    }

    private void createTableRequest(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE IF NOT EXISTS " + ConstString.REQUEST_TABLE_NAME + "("
                + ConstString.REQUEST_COL_ID + " INTEGER PRIMARY KEY,"
                + ConstString.REQUEST_COL_ID_USER + " INTEGER, "
                + ConstString.REQUEST_COL_ID_STATE + " INTEGER, "
                + ConstString.REQUEST_COL_TITLE + " TEXT, "
                + ConstString.REQUEST_COL_CONTENT + " TEXT, "
                + ConstString.REQUEST_COL_DATETIME +" TEXT "+ ")";

        sqLiteDatabase.execSQL(query);
    }

}
