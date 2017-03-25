package com.example.admin.lifesaver;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;

    public LocationService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

    //    Toast.makeText(getApplicationContext(),"Location Service started",Toast.LENGTH_SHORT).show();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();


        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        Preferences.writeString(getApplicationContext(),Preferences.LATITUDE,location.getLatitude()+"");
        Preferences.writeString(getApplicationContext(),Preferences.LONGITUDE,location.getLongitude()+"");
      //  Toast.makeText(getApplicationContext(),"Location Changed:"+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

  //      Toast.makeText(getApplicationContext(),"Location Service connected",Toast.LENGTH_SHORT).show();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
 //       Toast.makeText(getApplicationContext(),"Location Service conn suspended",Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
   //     Toast.makeText(getApplicationContext(),"Location Service conn failed",Toast.LENGTH_SHORT).show();

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

  //      Toast.makeText(getApplicationContext(),"Location Service stopped",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
