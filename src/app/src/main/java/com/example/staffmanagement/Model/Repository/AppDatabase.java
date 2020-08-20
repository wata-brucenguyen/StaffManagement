package com.example.staffmanagement.MVVM.Model.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.Model.LocalDb.RequestDAO;
import com.example.staffmanagement.MVVM.Model.LocalDb.RoleDAO;
import com.example.staffmanagement.MVVM.Model.LocalDb.StateRequestDAO;
import com.example.staffmanagement.MVVM.Model.LocalDb.UserDAO;
import com.example.staffmanagement.MVVM.Model.LocalDb.UserStateDAO;
import com.example.staffmanagement.MVVM.Model.Ultils.ConstString;


@Database(entities = {User.class, Request.class, Role.class, StateRequest.class, UserState.class}, version = ConstString.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract UserDAO userDAO();
    public abstract RequestDAO requestDAO();
    public abstract RoleDAO roleDAO();
    public abstract StateRequestDAO stateRequestDAO();
    public abstract UserStateDAO userStateDAO();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, ConstString.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void onDestroy(){
        if( instance != null)
            instance = null;
    }

    public static AppDatabase getDb(){
        return instance;
    }
}
