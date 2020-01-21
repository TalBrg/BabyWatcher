package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity.BabyBroadcastReceiver;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class BabyNotification {

    private NotificationManager manager;
    private String NOTIFICATION_ID = "001";

    private Context mContext;

    public BabyNotification(Context context, String notificationId) {
        this.mContext = context;
        this.NOTIFICATION_ID = notificationId;
        manager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public void babyNotify(String header){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 200, 500, 200, 1200});
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext,NOTIFICATION_ID);

        builder.setAutoCancel(true);
        builder.setTicker("this is ticker text");
        builder.setContentTitle(header);
        builder.setContentText(header);
        builder.setSmallIcon(R.drawable.bt_icon);
//        builder.setTimeoutAfter(5000);
//        builder.setContentIntent(pendingIntent);
//        builder.setOngoing(true);
        builder.setSubText("Baby Alert");   //API level 16
        builder.setNumber(100);

//        builder.setDefaults(DEFAULT_ALL);
        Notification myNotication = builder.build();
        if("002".equals(NOTIFICATION_ID)){
            manager.notify(22,myNotication);
        } else {
            manager.notify(11, myNotication);
        }

    }


}
