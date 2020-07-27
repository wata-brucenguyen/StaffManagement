package com.example.staffmanagement.Model.Database.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.staffmanagement.Model.Database.Data.SeedData;
import com.example.staffmanagement.Model.Database.Entity.Request;

import java.util.ArrayList;

public class RequestDbHandler extends DatabaseHandler{
    public RequestDbHandler(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        Log.i("DATABASE","INITIALIZE REQUEST");
        if(getCount() == 0){
            Log.i("DATABASE","INITIALIZE Request GET DATA");
            ArrayList<Request> list= SeedData.getRequestList();
            SQLiteDatabase db=this.getWritableDatabase();
            for(Request request : list){
                ContentValues contentValues = new ContentValues();
                contentValues.put(ConstString.REQUEST_COL_ID,request.getId());
                contentValues.put(ConstString.REQUEST_COL_ID_USER,request.getIdUser());
                contentValues.put(ConstString.REQUEST_COL_ID_STATE,request.getIdState());
                contentValues.put(ConstString.REQUEST_COL_TITLE,request.getTitle());
                contentValues.put(ConstString.REQUEST_COL_CONTENT,request.getContent());
                contentValues.put(ConstString.REQUEST_COL_DATETIME,request.getDateTime());

                db.insert(ConstString.REQUEST_TABLE_NAME,null,contentValues);
            }
            db.close();
        }

    }

    private int getCount() {
        String query =  "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getCountWaitingForUser(int idUser) {
        String query =  "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME
                +" WHERE IdUser = "+idUser+" AND IdState = 1 ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public String getRoleNameById(int idRole){
        String name = null;
        String selection =  ConstString.ROLE_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.ROLE_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(idRole)},
                null,null,null);
        if( cursor.moveToFirst() )
            name = cursor.getString(1);
        cursor.close();
        db.close();
        return name;
    }

    public ArrayList<Request> getAllRequestForUser(int idUser){
        ArrayList<Request> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE IdUser = " + idUser;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Request request = new Request(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4),cursor.getString(5));
                list.add(request);
                cursor.moveToNext();
            }
            while( !cursor.isAfterLast() );
        }

        cursor.close();
        db.close();

        return list;
    }

    public ArrayList<Request> findRequestByTitle(int idUser, String title){
        ArrayList<Request> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME + " WHERE  IdUser = " + idUser + " AND Title LIKE '%" + title +"%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Request request = new Request(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4),cursor.getString(5));
                list.add(request);
                cursor.moveToNext();
            }
            while( !cursor.isAfterLast() );
        }

        cursor.close();
        db.close();

        return list;
    }

    public String getTitleById(int idRequest){
        String title = null;
        String selection =  ConstString.REQUEST_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.REQUEST_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(idRequest)},
                null,null,null);
        if( cursor.moveToFirst() )
            title = cursor.getString(3);
        cursor.close();
        db.close();
        return title;
    }

    public String getDateTimeById(int idRequest) {
        String dateTime = null;
        String selection =  ConstString.REQUEST_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.REQUEST_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(idRequest)},
                null,null,null);
        if( cursor.moveToFirst() )
            dateTime = cursor.getString(5);
        cursor.close();
        db.close();
        return dateTime;
    }

    public String getFullNameById(int idUser) {
        String fullName = null;
        String selection =  ConstString.USER_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.USER_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(idUser)},
                null,null,null);
        if( cursor.moveToFirst() )
            fullName = cursor.getString(2);
        cursor.close();
        db.close();
        return fullName;
    }

    public int getIdStateById(int idRequest) {
        int idState = 0;
        String selection =  ConstString.REQUEST_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.REQUEST_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(idRequest)},
                null,null,null);
        if( cursor.moveToFirst() )
            idState = cursor.getInt(2);
        cursor.close();
        db.close();
        return idState;
    }
    
    public ArrayList<Request> getAll(){
        ArrayList<Request> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.REQUEST_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Request request = new Request(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4),cursor.getString(5));
                list.add(request);
                cursor.moveToNext();
            }
            while( !cursor.isAfterLast() );
        }

        cursor.close();
        db.close();

        return list;
    }

    public Request getById(int id){
        Request request = null;
        String selection =  ConstString.REQUEST_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.REQUEST_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(id)},
                null,null,null);
        if( cursor.moveToFirst() )
            request = new Request(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                    cursor.getString(3), cursor.getString(4),cursor.getString(5));
        cursor.close();
        db.close();
        return request;
    }

    public Request insert(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.REQUEST_COL_ID_USER, request.getIdUser());
        contentValues.put(ConstString.REQUEST_COL_ID_STATE, request.getIdState());
        contentValues.put(ConstString.REQUEST_COL_TITLE, request.getTitle());
        contentValues.put(ConstString.REQUEST_COL_CONTENT, request.getContent());
        contentValues.put(ConstString.REQUEST_COL_DATETIME, request.getDateTime());

        long i = db.insert(ConstString.REQUEST_TABLE_NAME,null,contentValues);
        db.close();
        return getById((int) i);
    }

    public void update(Request request){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.REQUEST_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(request.getId()) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.REQUEST_COL_ID_USER,request.getIdUser());
        contentValues.put(ConstString.REQUEST_COL_ID_STATE,request.getIdState());
        contentValues.put(ConstString.REQUEST_COL_TITLE,request.getTitle());
        contentValues.put(ConstString.REQUEST_COL_CONTENT,request.getContent());
        contentValues.put(ConstString.REQUEST_COL_DATETIME,request.getDateTime());


        db.update(ConstString.REQUEST_TABLE_NAME,contentValues,selection,selectionArgs);
        Log.i("DATABASE","UPDATE REQUEST ");
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.REQUEST_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(ConstString.REQUEST_TABLE_NAME,selection,selectionArgs);
        Log.i("DATABASE","REQUEST USER ");
        db.close();
    }


    
}
