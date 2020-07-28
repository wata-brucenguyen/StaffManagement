package com.example.staffmanagement.View.Ultils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class GeneralFunc {

    public static void logout(Context context, Class navigationClass) {
        Intent intent = new Intent(context, navigationClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constant.SHARED_PREFERENCE_IS_LOGIN);
        editor.remove(Constant.SHARED_PREFERENCE_ID_USER);
        editor.apply();

        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static boolean isTheLastActivity(Context context) {
        ActivityManager mgr = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mgr.getRunningTasks(10);

        if (taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(context.getClass().getName())) {
            return true;
        }
        return false;
    }

    public static String convertMilliSecToDateString(long milliSecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(milliSecond);
        return format.format(date);
    }

    public static long convertDateStringToLong(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("DATE", "GG " + format.format(date.getTime()));
        return date.getTime();
    }

    public static boolean checkChangeProfile(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        boolean b =sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_IS_CHANGE_PROFILE,false);
        if(b == true && isTheLastActivity(context)){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(Constant.SHARED_PREFERENCE_IS_CHANGE_PROFILE);
            editor.apply();
            return true;
        }
        return false;
    }

    public static void setStateChangeProfile(Context context,boolean b){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.SHARED_PREFERENCE_IS_CHANGE_PROFILE,b);
        editor.apply();
    }
}
