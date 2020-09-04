package com.example.staffmanagement.View.Ultils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.staffmanagement.R;

import java.util.List;

public class CheckNetwork {
    private ConnectivityManager mConnectivityManager;
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    public CheckNetwork(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                NetworkState.isNetworkConnected = true;
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (isAppOnForeground(context))
                    GeneralFunc.showCustomToast(context,"Lost network connection",R.drawable.ic_baseline_signal_wifi_off_24);
                NetworkState.isNetworkConnected = false;
            }
        };
    }

    public void registerCheckingNetwork() {
        if (mConnectivityManager == null)
            return;
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback);
    }

    public void unRegisterCheckingNetwork() {
        if (mConnectivityManager != null && mNetworkCallback != null) {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
            mConnectivityManager = null;
            mNetworkCallback = null;
        }
    }


    public static boolean checkInternetConnection(Context context) {
        if (!NetworkState.isNetworkConnected) {
            GeneralFunc.showCustomToast(context,"No network connection",R.drawable.ic_baseline_signal_wifi_off_24);
            return false;
        } else
            return true;
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
