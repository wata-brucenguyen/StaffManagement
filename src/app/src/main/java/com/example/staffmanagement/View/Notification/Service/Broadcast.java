package com.example.staffmanagement.View.Notification.Service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mTitle = intent.getStringExtra("Title");
        String mMessage = intent.getStringExtra("Message");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!TextUtils.isEmpty(mTitle)) {
            builder.setTitle(mTitle);
        }
        if(!TextUtils.isEmpty(mMessage)) {
            builder.setMessage(mMessage);
        }

       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });
        builder.show();
    }
}
