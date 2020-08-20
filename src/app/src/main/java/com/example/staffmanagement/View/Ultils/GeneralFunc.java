package com.example.staffmanagement.View.Ultils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        final String token = sharedPreferences.getString(Constant.SHARED_PREFERENCE_TOKEN, "");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tokens");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (String.valueOf(UserSingleTon.getInstance().getUser().getId()).equals(s.getKey())) {
                        for (DataSnapshot d : s.getChildren()) {
                            if (token.equals(d.getValue())) {
                                d.getRef().removeValue();
                                break;
                            }
                        }
                        break;
                    }
                }
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constant.SHARED_PREFERENCE_IS_LOGIN);
        editor.remove(Constant.SHARED_PREFERENCE_ID_USER);
        editor.remove(Constant.SHARED_PREFERENCE_TOKEN);
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

    public static boolean checkChangeProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_IS_CHANGE_PROFILE, false);

        if (b == true && isTheLastActivity(context)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(Constant.SHARED_PREFERENCE_IS_CHANGE_PROFILE);
            editor.apply();
            return true;
        }
        return false;
    }

    public static void setStateChangeProfile(Context context, boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.SHARED_PREFERENCE_IS_CHANGE_PROFILE, b);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradient(Activity activity) {
        Window window = activity.getWindow();
        Drawable background = activity.getResources().getDrawable(R.drawable.bg_gradient_admin);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
    }
}
