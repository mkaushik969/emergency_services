package com.example.admin.lifesaver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

/**
 * Created by Admin on 10/13/2016.
 */

public class Help extends Service implements AccelerometerListener{

    String str_address;


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {

            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }

    @Override
    public void onShake(float force) {

    }
}
