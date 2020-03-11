package com.t3h.hc_musicapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private final String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"onBind");
//        return null;
        return new MyBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    //lay ket noi giua service va activity
    public class MyBinder extends Binder {
        private MyService service;

        public MyBinder(MyService service) {
            this.service = service;
        }

        public MyService getService() {
            return service;
        }
    }
}
