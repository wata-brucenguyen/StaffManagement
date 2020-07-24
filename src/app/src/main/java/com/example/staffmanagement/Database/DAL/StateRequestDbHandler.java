package com.example.staffmanagement.Database.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Entity.StateRequest;

import java.util.ArrayList;

public class StateRequestDbHandler extends DatabaseHandler{

    public StateRequestDbHandler(Context context) {
        super(context);
        initialize();
    }

    public void initialize() {
        Log.i("DATABASE", "INITIALIZE STATE REQUEST");
        if (getCount() == 0) {
            Log.i("DATABASE", "INITIALIZE STATE REQUEST GET DATA");
            ArrayList<StateRequest> list = SeedData.getStateList();
            SQLiteDatabase db = this.getWritableDatabase();

            for (StateRequest item : list) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ConstString.STATE_REQUEST_COL_ID, item.getId());
                contentValues.put(ConstString.STATE_REQUEST_COL_NAME, item.getName());
                db.insert(ConstString.STATE_REQUEST_TABLE_NAME, null, contentValues);
            }

            db.close();
        }
    }

    private int getCount(){
        String query =  "SELECT * FROM " + ConstString.STATE_REQUEST_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public ArrayList<StateRequest> getAll(){
        ArrayList<StateRequest> list = new ArrayList<>();
        String query =  "SELECT * FROM " + ConstString.STATE_REQUEST_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                StateRequest stateRequest = new StateRequest(cursor.getInt(0),cursor.getString(1));
                list.add(stateRequest);
                cursor.moveToNext();
            }
            while(cursor.isAfterLast() == false);
        }

        cursor.close();
        db.close();

        return list;
    }

    public StateRequest getById(int id){
        StateRequest stateRequest = null;
        String selection =  ConstString.STATE_REQUEST_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.STATE_REQUEST_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(id)},
                null,null,null);

        if( cursor.moveToFirst() )
            stateRequest = new StateRequest(cursor.getInt(0),cursor.getString(1));
        cursor.close();
        db.close();
        return stateRequest;
    }

    public void insert(StateRequest stateRequest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.STATE_REQUEST_COL_NAME,stateRequest.getName());
        long i = db.insert(ConstString.STATE_REQUEST_TABLE_NAME,null,contentValues);
        Log.i("DATABASE","INSERT STATE REQUEST : " + i);
        db.close();
    }

    public void update(StateRequest stateRequest){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.STATE_REQUEST_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(stateRequest.getId()) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstString.STATE_REQUEST_COL_NAME,stateRequest.getName());
        db.update(ConstString.STATE_REQUEST_TABLE_NAME,contentValues,selection,selectionArgs);
        Log.i("DATABASE","UPDATE STATE REQUEST ");
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ConstString.STATE_REQUEST_COL_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(ConstString.STATE_REQUEST_TABLE_NAME,selection,selectionArgs);
        Log.i("DATABASE","DELETE STATE REQUEST ");
        db.close();
    }

    public String getStateNameById(int idState) {
        String name = null;
        String selection =  ConstString.STATE_REQUEST_COL_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstString.STATE_REQUEST_TABLE_NAME,
                null,
                selection,
                new String[]{String.valueOf(idState)},
                null,null,null);
        if( cursor.moveToFirst() )
            name = cursor.getString(1);
        cursor.close();
        db.close();
        return name;
    }
}
