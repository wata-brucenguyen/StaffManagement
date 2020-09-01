package com.example.staffmanagement.View.Ultils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
                if(isAppOnForeground(context))
                    Toast.makeText(context, "Lost network connection", Toast.LENGTH_SHORT).show();
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

    public void unRegisterCheckingNetwork(){
        if(mConnectivityManager != null && mNetworkCallback != null){
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
        }
    }


    public static boolean checkInternetConnection(Context context) {
        if (!NetworkState.isNetworkConnected) {
            Toast.makeText(context, "No network connection", Toast.LENGTH_SHORT).show();
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
