package com.example.admin.lifesaver;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.*;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int HOSPITAL = 1;
    private static final int POLICE = 2;
    private static final int FIRESTATION = 3;
    private static final int AMBULANCE = 4;
    private static final int BLOODBANK = 5;

    LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Preferences.readString(this, Preferences.NAME) == null) {
            startActivity(new Intent(this, Registration.class));

            if (Preferences.readString(this, Preferences.NAME) == null) {
                this.finish();
            }
        } else {

            setContentView(R.layout.activity_main);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
                    }, 1);
              //      Toast.makeText(MainActivity.this, "Take permissions", Toast.LENGTH_SHORT).show();

                } else {
            //        Toast.makeText(MainActivity.this, "Already allowed", Toast.LENGTH_SHORT).show();
                    startService(new Intent(this, LocationService.class));
                    startService(new Intent(this, ShakeListenerService.class));
                }
            }
            else
            {
          //      Toast.makeText(MainActivity.this, "Less tha lollipop", Toast.LENGTH_SHORT).show();
                startService(new Intent(this, LocationService.class));
                startService(new Intent(this, ShakeListenerService.class));
            }


            FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        StringBuilder number = new StringBuilder();
                        number.append("tel:");
                        number.append(Preferences.readString(MainActivity.this, Preferences.EMERGENCY_NUMBER));
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number.toString()));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                    catch (ActivityNotFoundException activityException) {
                        Log.e("First Response", "Call failed");
//                StringBuilder number1 = new StringBuilder();
                        //              number1.append("sms:");
                        //            Intent intentSms = new Intent(Intent.ACTION_SENDTO, Uri.parse(number1.toString()));
                        //          intentSms.putExtra("sms_body", "Please Help, I am in trouble.");
                        //        startActivity(intentSms);
                        Toast.makeText(MainActivity.this, "Could not make a call",Toast.LENGTH_SHORT).show();
                        String latitude,longitude;
                        latitude = Preferences.readString(getApplicationContext(), Preferences.LATITUDE);
                        longitude = Preferences.readString(getApplicationContext(), Preferences.LONGITUDE);

                        String msg = "Please help me...!!! ";

                        if (latitude != null && longitude != null)
                            msg = msg + "http://maps.google.com/?q="+latitude+","+longitude;
                        SmsManager smsmanager = SmsManager.getDefault();
                        String phno = Preferences.readString(getApplicationContext(), Preferences.EMERGENCY_NUMBER);
                        smsmanager.sendTextMessage(phno, null, msg , null, null);

                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int i;
        for( i=0;i<grantResults.length;i++)
        {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                break;
            }
        }

        if(i!=grantResults.length)
            Toast.makeText(this,"Some permissions were not allowed, app may not work properly",Toast.LENGTH_SHORT);
        else
        {
            startService(new Intent(this,LocationService.class));
        }
        startService(new Intent(this,ShakeListenerService.class));
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.firestation_button) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("key", FIRESTATION);
            startActivity(intent);
        } else if (id == R.id.hospital_button) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("key", HOSPITAL);
            startActivity(intent);
        } else if (id == R.id.ambulance_button) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("key", AMBULANCE);
            startActivity(intent);
        } else if (id == R.id.police_button) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("key", POLICE);
            startActivity(intent);
        } else if (id == R.id.bloodbank_button) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("key", BLOODBANK);
            startActivity(intent);
        }
        else if (id == R.id.compass_button) {
            startActivity(new Intent(this,Compass.class));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.ma_menu_update_em)
        {
            startActivity(new Intent(this,UpdateEM.class));
            return true;
        }
        else if(id==R.id.ma_menu_update_user)
        {
            startActivity(new Intent(this,UpdateUser.class));
            return true;
        }
        else if(id==R.id.ma_menu_share_loc)
        {
            String latitude,longitude;

            latitude = Preferences.readString(getApplicationContext(), Preferences.LATITUDE);
            longitude = Preferences.readString(getApplicationContext(), Preferences.LONGITUDE);

//            Toast.makeText(getApplicationContext(), "lat"+latitude+" lng:"+longitude, Toast.LENGTH_LONG).show();


            String msg = "My location is ";

            if (latitude != null && longitude != null)
            {
                msg = msg + "http://maps.google.com/?q="+latitude+","+longitude;

                SmsManager smsmanager = SmsManager.getDefault();
                String phno = Preferences.readString(getApplicationContext(), Preferences.EMERGENCY_NUMBER);
                smsmanager.sendTextMessage(phno, null, msg , null, null);
                Toast.makeText(getApplicationContext(), "Message sent to " + phno, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,LocationService.class));
    }
}
