package com.example.myapplication;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class OffBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent !=null && intent.getAction() != null && intent.getAction().equals("PleaseStop")){
            context.stopService(new Intent(context, OfflineService.class));
            return;
        }

        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, OfflineService.class));


        } else {
            context.startService(new Intent(context, OfflineService.class));
        }
    }

}

