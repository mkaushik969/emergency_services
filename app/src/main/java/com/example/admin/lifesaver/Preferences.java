package com.example.admin.lifesaver;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 9/28/2016.
 */

public class Preferences {
    public static final String PREF_NAME = "LOGIN";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String NAME = "username";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String  ADDRESS = "address";
    public static final String EMERGENCY_CONTACT = "emergency_name";
    public static final String EMERGENCY_NUMBER = "emergency_number";
    public static final String BLOOD_TYPE = "blood_type";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";



    public static SharedPreferences sharedPreferences(Context context)
    {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context)
    {
        return sharedPreferences(context).edit();
    }

    public static void writeString(Context context, String key, String value)
    {
        getEditor(context).putString(key, value).commit();

    }


    public static String readString(Context context, String key)
    {
        return sharedPreferences(context).getString(key, null);
    }


}
