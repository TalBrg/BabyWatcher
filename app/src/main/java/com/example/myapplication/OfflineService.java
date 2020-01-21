package com.example.myapplication;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.android.gms.signin.internal.SignInClientImpl.ACTION_START_SERVICE;

public class OfflineService extends Service {

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    private static BroadcastReceiver m_ScreenOffReceiver;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private Timer t;
    private boolean mActivityStarted = false;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            System.out.println("!!sdfhlkrg!! onConnectionStateChange");
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                popNotification("connected");
                Intent mainActivity = getMainActivity();
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                closeConnections();
                mActivityStarted = true;
                startActivity(mainActivity);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                mActivityStarted = false;
//                popNotification("disconnected");
            } else {
                popNotification("$%#%$%#$^$%%@#$");
            }
        }
    };


    private void closeConnections() {
        t.cancel();
        bluetoothAdapter = null;
//        mBluetoothGatt.disconnect();
        if(mBluetoothGatt != null){
            mBluetoothGatt.close();
        }
        mBluetoothGatt = null;
        bluetoothManager = null;
    }

    private Intent getMainActivity(){
        return new Intent(this, MainActivity.class);
    }


    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerScreenOffReceiver();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("kjlskdjflsd")
                    .setContentText("falwiejf,kszc").build();

            startForeground(1, notification);
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent.getBooleanExtra("initBT",true)){
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (intent != null) {
                t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        myAction();
                    }
                }, 5000,5000);

            }
            return START_STICKY;
        }

        return START_STICKY;
    }

    private void popNotification(String header){
        BabyNotification babyNotification = new BabyNotification(this, "001");
        babyNotification.babyNotify(header);
    }

    private void myAction(){



        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice("CC:78:AB:AE:1C:85");
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
        } else {
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);

        }

    }




    private void registerScreenOffReceiver()
    {
        m_ScreenOffReceiver = new OffBroadcastReceiver();

        IntentFilter filter = new IntentFilter("PleaseStop");
        registerReceiver(m_ScreenOffReceiver, filter);
    }


}
