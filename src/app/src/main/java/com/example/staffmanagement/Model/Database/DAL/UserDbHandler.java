package com.example.staffmanagement.Model.Database.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.staffmanagement.Model.Database.Data.SeedData;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Entity.UserBuilder.UserBuilder;
import com.example.staffmanagement.View.Ultils.Constant;

import java.util.ArrayList;
import java.util.Map;

public class UserDbHandler extends DatabaseHandler {

    public UserDbHandler(Context context) {
        super(context);
        initialize();
    }

    public void initialize() {
        Log.i("DATABASE", "INITIALIZE USER");
        if (getCount() == 0) {
            Log.i("DATABASE", "INITIALIZE User GET DATA");
            ArrayList<User> list = SeedData.getUserList();
            SQLiteDatabase db = this.getWritableDatabase();

            for (User item : list) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ConstString.USER_COL_ID, item.getId());
                contentValues.put(ConstString.USER_COL_ID_ROLE, item.getIdRole());
                contentValues.put(ConstString.USER_COL_FULL_NAME, item.getFullName());
                contentValues.put(ConstString.USER_COL_USERNAME, item.getUserName());
                contentValues.put(ConstString.USER_COL_PASSWORD, item.getPassword());
                contentValues.put(ConstString.USER_COL_PHONE_NUMBER, item.getPhoneNumber());
                contentValues.put(ConstString.USER_COL_EMAIL, item.getEmail());
                contentValues.put(ConstString.USER_COL_ADDRESS, item.getAddress());
                contentValues.put(ConstString.USER_COL_AVATAR, item.getAvatar());
                db.insert(ConstString.USER_TABLE_NAME, null, contentValues);
            }

            db.close();
        }

    }

    public ArrayList<User> getLimitListUser(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        ArrayList<User> list = new ArrayList<>();
        String searchString = (String) criteria.get(Constant.SEARCH_NAME_IN_ADMIN);
        if (TextUtils.isEmpty(searchString))
            searchString = "";
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U1 WHERE FullName LIKE '%" + searchString + "%' AND NOT EXISTS ( SELECT * FROM " + ConstString.USER_TABLE_NAME;
        query += " U2 WHERE U1.Id = U2.Id AND U2.Id = " + idUser + " ) ";
        query += " LIMIT " + offset + "," + numRow;
        Log.i("GETDATA", query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new UserBuilder()
                        .buildId(cursor.getInt(0))
                        .buildIdRole(cursor.getInt(1))
                        .buildFullName(cursor.getString(2))
                        .buildUserName( cursor.getString(3))
                        .buildPassword( cursor.getString(4))
                        .buildPhoneNumber(cursor.getString(5))
                        .buildEmail(cursor.getString(6))
                        .buildAddress(cursor.getString(7))
                        .buildAvatar(cursor.getBlob(8))
                        .build();
                list.add(user);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        db.close();

        return list;
    }

    private int getCount() {
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public ArrayList<User> getAll() {
        ArrayList<User> list = new ArrayList<>();
        String query = "SELECT * FROM " + ConstString.USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new UserBuilder()
                        .buildId(cursor.getInt(0))
                        .buildIdRole(cursor.getInt(1))
                        .buildFullName(cursor.getString(2))
                        .buildUserName( cursor.getString(3))
                        .buildPassword( cursor.getString(4))
                        .buildPhoneNumber(cursor.getString(5))
                        .buildEmail(cursor.getString(6))
                        .buildAddress(cursor.getString(7))
                        .buildAvatar(cursor.getBlob(8))
                        .build();
                list.add(user);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }

        cursor.close();
        db.close();

        return list;
    }



    public ArrayList<Role> getAllRole() {
        ArrayList<Role> list = new ArrayList<Role>();

        String query = "SELECT * FROM " + ConstString.ROLE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Role Role = new Role(cursor.getInt(0), cursor.getString(1));
                list.add(Role);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }

        cursor.close();
        db.close();

        return list;
    }

    public User getById(int id) {
        User user = null;
        String selection = ConstString.USER_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.USER_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.moveToFirst())
            user = new UserBuilder()
                    .buildId(cursor.getInt(0))
                    .buildIdRole(cursor.getInt(1))
                    .buildFullName(cursor.getString(2))
                    .buildUserName( cursor.getString(3))
                    .buildPassword( cursor.getString(4))
                    .buildPhoneNumber(cursor.getString(5))
                    .buildEmail(cursor.getString(6))
                    .buildAddress(cursor.getString(7))
                    .buildAvatar(cursor.getBlob(8))
                    .build();
        cursor.close();
        db.close();
        return user;
    }

    public User getByLoginInformation(String userName, String password) {
        User user = null;
        String selection = ConstString.USER_COL_USERNAME + " = ? AND " + ConstString.USER_COL_PASSWORD + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.USER_TABLE_NAME,
                null,
                selection,
                new String[]{userName, password},
                null, null, null);
        if (cursor.moveToFirst())
            user = new UserBuilder()
                    .buildId(cursor.getInt(0))
                    .buildIdRole(cursor.getInt(1))
                    .buildFullName(cursor.getString(2))
                    .buildUserName( cursor.getString(3))
                    .buildPassword( cursor.getString(4))
                    .buildPhoneNumber(cursor.getString(5))
                    .buildEmail(cursor.getString(6))
                    .buildAddress(cursor.getString(7))
                    .buildAvatar(cursor.getBlob(8))
                    .build();
        cursor.close();
        db.close();
        return user;
    }

    public User insert(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_ID_ROLE, user.getIdRole());
        contentValues.put(ConstString.USER_COL_FULL_NAME, user.getFullName());
        contentValues.put(ConstString.USER_COL_USERNAME, user.getUserName());
        contentValues.put(ConstString.USER_COL_PASSWORD, user.getPassword());
        contentValues.put(ConstString.USER_COL_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(ConstString.USER_COL_EMAIL, user.getEmail());
        contentValues.put(ConstString.USER_COL_ADDRESS, user.getAddress());
        contentValues.put(ConstString.USER_COL_AVATAR, user.getAvatar());

        long i = db.insert(ConstString.USER_TABLE_NAME, null, contentValues);
        db.close();
        return getById((int) i);
    }

    public void update(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(user.getId())};
        ContentValues contentValues = new ContentValues();

        contentValues.put(ConstString.USER_COL_ID_ROLE, user.getIdRole());
        contentValues.put(ConstString.USER_COL_FULL_NAME, user.getFullName());
        contentValues.put(ConstString.USER_COL_USERNAME, user.getUserName());
        contentValues.put(ConstString.USER_COL_PASSWORD, user.getPassword());
        contentValues.put(ConstString.USER_COL_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(ConstString.USER_COL_EMAIL, user.getEmail());
        contentValues.put(ConstString.USER_COL_ADDRESS, user.getAddress());
        contentValues.put(ConstString.USER_COL_AVATAR, user.getAvatar());

        db.update(ConstString.USER_TABLE_NAME, contentValues, selection, selectionArgs);
        Log.i("DATABASE", "UPDATE User ");
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(ConstString.USER_TABLE_NAME, selection, selectionArgs);
        Log.i("DATABASE", "DELETE USER ");
        db.close();
    }

    public void resetPassword(int idUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(idUser)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_PASSWORD, ConstString.DEFAULT_PASSWORD);
        db.update(ConstString.USER_TABLE_NAME, contentValues, selection, selectionArgs);
        db.close();
    }

    public void changePassword(int idUser, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(idUser)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_PASSWORD, password);
        db.update(ConstString.USER_TABLE_NAME, contentValues, selection, selectionArgs);
        db.close();
    }

    public void changeAvatar(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.USER_COL_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(user.getId())};

        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.USER_COL_AVATAR, user.getAvatar());

        db.update(ConstString.USER_TABLE_NAME, contentValues, selection, selectionArgs);
        db.close();
    }

    public boolean checkUserNameIsExisted(String userName) {
        String query = " SELECT * FROM " + ConstString.USER_TABLE_NAME + "  WHERE UserName = '" + userName + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
            return true;
        cursor.close();
        db.close();
        return false;
    }
}
