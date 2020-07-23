package com.example.staffmanagement.Database.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Entity.Role;

import java.util.ArrayList;

public class RoleDbHandler extends DatabaseHandler {

    public RoleDbHandler(Context context) {
        super(context);
        initialize();
    }

    public void initialize(){
        Log.i("DATABASE","INITIALIZE ROLE");
        if(getCount() == 0)
        {
            Log.i("DATABASE","INITIALIZE ROLE GET DATA");
            ArrayList<Role> list = SeedData.getRoleList();
            SQLiteDatabase db = this.getWritableDatabase();

            for(Role item : list){
                ContentValues contentValues = new ContentValues();
                contentValues.put(ConstString.ROLE_COL_ID,item.getId());
                contentValues.put(ConstString.ROLE_COL_NAME,item.getName());
                db.insert(ConstString.ROLE_TABLE_NAME,null,contentValues);
            }

            db.close();
        }

    }

    private int getCount(){
        String query =  "SELECT * FROM " + ConstString.ROLE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public ArrayList<Role> getAll(){
        ArrayList<Role> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.ROLE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Role role = new Role(cursor.getInt(0),cursor.getString(1));
                list.add(role);
                cursor.moveToNext();
            }
            while(cursor.isAfterLast() == false);
        }

        cursor.close();
        db.close();

        return list;
    }

    public Role getById(int id){
        Role role = null;
        String selection =  ConstString.ROLE_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.ROLE_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(id)},
                null,null,null);
        if( cursor.moveToFirst() )
            role = new Role(cursor.getInt(0),cursor.getString(1));
        cursor.close();
        db.close();
        return role;
    }

    public void insert(Role role){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.ROLE_COL_NAME,role.getName());
        long i = db.insert(ConstString.ROLE_TABLE_NAME,null,contentValues);
        Log.i("DATABASE","INSERT ROLE : " + i);
        db.close();
    }

    public void update(Role role){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.ROLE_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(role.getId()) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.ROLE_COL_NAME,role.getName());
        db.update(ConstString.ROLE_TABLE_NAME,contentValues,selection,selectionArgs);
        Log.i("DATABASE","UPDATE ROLE ");
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.ROLE_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(ConstString.ROLE_TABLE_NAME,selection,selectionArgs);
        Log.i("DATABASE","DELETE ROLE ");
        db.close();
    }

}
