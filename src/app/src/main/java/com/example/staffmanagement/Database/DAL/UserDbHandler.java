package com.example.staffmanagement.Database.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.User;

import java.util.ArrayList;

public class UserDbHandler extends DatabaseHandler {

    public UserDbHandler(Context context) {
        super(context);
        initialize();
    }

    public void initialize(){
        Log.i("DATABASE","INITIALIZE USER");
        if(getCount() == 0)
        {
            Log.i("DATABASE","INITIALIZE User GET DATA");
            ArrayList<User> list = SeedData.getUserList();
            SQLiteDatabase db = this.getWritableDatabase();

            for(User item : list){
                ContentValues contentValues = new ContentValues();
                contentValues.put(ConstString.USER_COL_ID,item.getId());
                contentValues.put(ConstString.USER_COL_ID_ROLE,item.getIdRole());
                contentValues.put(ConstString.USER_COL_FULL_NAME,item.getFullName());
                contentValues.put(ConstString.USER_COL_USERNAME,item.getUserName());
                contentValues.put(ConstString.USER_COL_PASSWORD,item.getPassword());
                contentValues.put(ConstString.USER_COL_PHONE_NUMBER,item.getPhoneNumber());
                contentValues.put(ConstString.USER_COL_EMAIL,item.getEmail());
                contentValues.put(ConstString.USER_COL_ADDRESS,item.getAddress());
                contentValues.put(ConstString.USER_COL_BIRTHDAY,item.getBirthDay());
                db.insert(ConstString.USER_TABLE_NAME,null,contentValues);
            }

            db.close();
        }

    }

    private int getCount(){
        String query =  "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public ArrayList<User> getAll(){
        ArrayList<User> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                User User = new User(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),cursor.getString(5),
                        cursor.getString(6), cursor.getString(7),cursor.getString(8));
                list.add(User);
                cursor.moveToNext();
            }
            while( !cursor.isAfterLast() );
        }

        cursor.close();
        db.close();

        return list;
    }

    public ArrayList<Role> getRole(){
        ArrayList<Role> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.ROLE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Role Role = new Role(cursor.getInt(0),cursor.getString(1));
                list.add(Role);
                cursor.moveToNext();
            }
            while( !cursor.isAfterLast() );
        }

        cursor.close();
        db.close();

        return list;
    }

    public int getIdRole(int id){
        int idRole = 0;
        String selection =  ConstString.ROLE_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.USER_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(id)},
                null,null,null);
        if( cursor.moveToFirst() )
            idRole = cursor.getInt(1);
        cursor.close();
        db.close();
        return idRole;
    }

    public User getById(int id){
        User User = null;
        String selection =  ConstString.USER_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.USER_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(id)},
                null,null,null);
        if( cursor.moveToFirst() )
            User = new User(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),cursor.getString(8));
        cursor.close();
        db.close();
        return User;
    }

    public User getByLoginInformation(String userName, String password){
        User user = null;
        String selection =  ConstString.USER_COL_USERNAME + " = ? AND " +  ConstString.USER_COL_PASSWORD + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.USER_TABLE_NAME,
                null,
                selection,
                new String[]{userName, password},
                null,null,null);
        if( cursor.moveToFirst() )
            user = new User(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),cursor.getString(8));
        cursor.close();
        db.close();
        return user;
    }

    public void insert(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_ID_ROLE,user.getIdRole());
        contentValues.put(ConstString.USER_COL_FULL_NAME,user.getFullName());
        contentValues.put(ConstString.USER_COL_USERNAME,user.getUserName());
        contentValues.put(ConstString.USER_COL_PASSWORD,user.getPassword());
        contentValues.put(ConstString.USER_COL_PHONE_NUMBER,user.getPhoneNumber());
        contentValues.put(ConstString.USER_COL_EMAIL,user.getEmail());
        contentValues.put(ConstString.USER_COL_ADDRESS,user.getAddress());
        contentValues.put(ConstString.USER_COL_BIRTHDAY,user.getBirthDay());
        long i = db.insert(ConstString.USER_TABLE_NAME,null,contentValues);
        Log.i("DATABASE","INSERT USER : " + i);
        db.close();
    }

    public void update(User User){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(User.getId()) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_ID_ROLE,User.getIdRole());
        contentValues.put(ConstString.USER_COL_FULL_NAME,User.getFullName());
        contentValues.put(ConstString.USER_COL_USERNAME,User.getFullName());
        contentValues.put(ConstString.USER_COL_PASSWORD,User.getFullName());
        contentValues.put(ConstString.USER_COL_PHONE_NUMBER,User.getFullName());
        contentValues.put(ConstString.USER_COL_EMAIL,User.getFullName());
        contentValues.put(ConstString.USER_COL_ADDRESS,User.getFullName());
        contentValues.put(ConstString.USER_COL_BIRTHDAY,User.getFullName());
        db.update(ConstString.USER_TABLE_NAME,contentValues,selection,selectionArgs);
        Log.i("DATABASE","UPDATE User ");
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(ConstString.USER_TABLE_NAME,selection,selectionArgs);
        Log.i("DATABASE","DELETE USER ");
        db.close();
    }

    public void resetPassword(int idUser){
        SQLiteDatabase db =this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String [] selectionArgs = { String.valueOf(idUser) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_PASSWORD,ConstString.DEFAULT_PASSWORD);
        db.update(ConstString.USER_TABLE_NAME,contentValues,selection,selectionArgs);
        db.close();
    }

    public void changePassword(int idUser, String password) {
        SQLiteDatabase db =this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String [] selectionArgs = { String.valueOf(idUser) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_PASSWORD,password);
        db.update(ConstString.USER_TABLE_NAME,contentValues,selection,selectionArgs);
        db.close();
    }
}
