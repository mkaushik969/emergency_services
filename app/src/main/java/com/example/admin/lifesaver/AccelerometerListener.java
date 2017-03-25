package com.example.admin.lifesaver;

/**
 * Created by Admin on 10/10/2016.
 */

public interface AccelerometerListener {
    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}
