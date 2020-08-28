package com.example.staffmanagement.View.Ultils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
        return date.getTime();
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return format.format(date);
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

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Toast.makeText(context,"No Connectivity Manager",Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null) {
            Toast.makeText(context,"No default network is currently active",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!networkInfo.isConnected()) {
            Toast.makeText(context,"Network is not connected",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!networkInfo.isAvailable()){
            Toast.makeText(context,"Network is not available",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean checkInternetConnectionNoToast(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null) {
            return false;
        }
        if(!networkInfo.isConnected()) {
            return false;
        }
        if(!networkInfo.isAvailable()){
            return false;
        }

        return true;
    }
    // hide keyboard when touch outside
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    @SuppressLint("ClickableViewAccessibility")
    public static void setupUI(View view, Activity activity) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(activity);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView,activity);
            }
        }
    }
}
