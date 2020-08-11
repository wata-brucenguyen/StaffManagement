package com.example.staffmanagement.View.Notification.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.staffmanagement.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    int id = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // handle a notification payload.

        if (remoteMessage.getData().size()> 0) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("Title"));
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("Message"));
            sendNotification(remoteMessage.getData().get("Title"),remoteMessage.getData().get("Message"));
            Intent intent = new Intent();
            intent.putExtra("Title",remoteMessage.getData().get("Title"));
            intent.putExtra("Message",remoteMessage.getData().get("Message"));
            intent.setAction("Notification");
            sendBroadcast(intent);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body gg : " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String title,String messageBody) {

        String channelId = "StaffManagement";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        id++;
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notificationBuilder.build());
        Log.d("Key__"," Check gg");
    }
}
