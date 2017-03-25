package com.example.admin.lifesaver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import static android.R.attr.gravity;
import static android.view.KeyCharacterMap.ALPHA;

public class ShakeEventListener implements SensorEventListener {

    Context context;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    public ShakeEventListener(Context context) {
        this.context = context;
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {

        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > 10) {

            SmsManager smsmanager = SmsManager.getDefault();
            String phno=Preferences.readString(context,Preferences.EMERGENCY_NUMBER);
            //smsmanager.sendTextMessage(phno, null, "hi", null, null);
            Toast.makeText(context, "Device has shaken."+phno, Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
