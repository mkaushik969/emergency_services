package com.example.admin.lifesaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * Created by HP on 23-11-2016.
 */

public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
     //   Toast.makeText(context,"onReceive() called!!!",Toast.LENGTH_SHORT).show();

        SensorManager sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(new ShakeEventListener(context), sensor, SensorManager.SENSOR_DELAY_NORMAL); // or other delay
    }
}
