package com.t3h.hc_musicapp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.t3h.hc_musicapp.MainActivity;
import com.t3h.hc_musicapp.R;
import com.t3h.hc_musicapp.databinding.ActivityMainBinding;

public class MusicService extends Service {

    public static final String EXTRA_NEED_STOP = "EXTRA_NEED_STOP";
    private final String TAG = "MusicService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
        pushNotification();
    }

    private void pushNotification() {
        String CHANNEL_ID = "CHANNEL_ID";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this,getClass());
        intent.putExtra(EXTRA_NEED_STOP,true);
        PendingIntent pending = PendingIntent.getService(this,0,intent,0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_music);
        builder.setTicker("Push notification");
        builder.setContentTitle("Keep service running");
        builder.setContentText("Using foreground service");

        builder.setContentIntent(pending);//xu ly su kien click vao notification

        startForeground(1213232,builder.build());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){

            boolean needStop = intent.getBooleanExtra(EXTRA_NEED_STOP,false);
            if(needStop){
                stopSelf();
                return START_NOT_STICKY;
            }

        }
        Log.e(TAG,"onStartCommand");
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }




    @Override
    public void onDestroy() {
//        Log.e(TAG,"onDestroy");
//        stopForeground(true);
        super.onDestroy();
        Log.e(TAG,"onDestroy");
        stopForeground(true);
//        stopSelf();
//        stopService(new Intent(this,MusicService.class));
        android.os.Process.killProcess(android.os.Process.myPid());//kill service tat nhac.
    }
}
