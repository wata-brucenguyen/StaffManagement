package com.example.staffmanagement.View.Ultils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class GeneralFunc {

    public static void logout(Context context, Class navigationClass){
        Intent intent = new Intent(context, navigationClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Const.SHARED_PREFERENCE_IS_LOGIN);
        editor.remove(Const.SHARED_PREFERENCE_ID_USER);
        editor.commit();

        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static boolean isTheLastActivity(Context context){
        ActivityManager mgr = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = mgr.getRunningTasks(10);

        if(taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(context.getClass().getName())) {
            return true;
        }
        return false;
    }
}
