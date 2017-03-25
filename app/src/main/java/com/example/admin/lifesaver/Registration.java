package com.example.admin.lifesaver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.onClick;

public class Registration extends AppCompatActivity  {


    public static EditText name, phone_number, emergency_name, emergency_number;
    public static TextView fname, fnum, fename, fenum;
    public static Spinner spinner;

    Pattern vname = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
    Pattern vnumber = Pattern.compile("[0-9]{10}");
    Pattern vaddr = Pattern.compile("[0-9]{1,4}\\s([A-Za-z]+\\s?([A-Za-z]{2,})?)\\s[A-Za-z]+\\s[A-Za-z]{2,}");
    Pattern em_name = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
    Pattern em_number = Pattern.compile("[0-9]{10}");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = (EditText) findViewById(R.id.name_field);

        phone_number = (EditText) findViewById(R.id.number_field);
        phone_number.setInputType(InputType.TYPE_CLASS_PHONE);

        emergency_name = (EditText) findViewById(R.id.emname_feild);

        emergency_number = (EditText) findViewById(R.id.emnumber_feild);
        emergency_number.setInputType(InputType.TYPE_CLASS_PHONE);



        fname = (TextView) findViewById(R.id.format_name);
        fname.setVisibility(TextView.INVISIBLE);

        fnum = (TextView) findViewById(R.id.format_phone);
        fnum.setVisibility(TextView.INVISIBLE);

        fename = (TextView) findViewById(R.id.format_emergency_name);
        fename.setVisibility(TextView.INVISIBLE);

        fenum = (TextView) findViewById(R.id.format_emergency_number);
        fenum.setVisibility(TextView.INVISIBLE);

        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> categories = new ArrayList<String>();
        categories.add("A-");
        categories.add("A");
        categories.add("A+");
        categories.add("B-");
        categories.add("B");
        categories.add("B+");
        categories.add("0-");
        categories.add("0");
        categories.add("0+");
        categories.add("AB-");
        categories.add("AB");
        categories.add("AB+");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 parent.getItemAtPosition(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        }

    public void save()
    {
      /* SharedPreferences  sharedPreferences = getSharedPreferences(PREF_NAME, MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME,name.getText().toString());
        editor.putString(PHONE_NUMBER, phonenumber.getText().toString() );
        editor.putString(ADDRESS,address.getText().toString() );
        editor.putString(BLOOD_TYPE, Integer.toString(spinner.getSelectedItemPosition()));
        editor.putString(EMERGENCY_CONTACT,emergency_name.getText().toString() );
        editor.putString(EMERGENCY_NUMBER, emergency_number.getText().toString());
        editor.commit();*/

        Preferences.writeString(this, Preferences.NAME, name.getText().toString());
        Preferences.writeString(this, Preferences.PHONE_NUMBER, phone_number.getText().toString());
        Preferences.writeString(this, Preferences.BLOOD_TYPE, Integer.toString(spinner.getSelectedItemPosition()));
        Preferences.writeString(this, Preferences.EMERGENCY_CONTACT, emergency_name.getText().toString());
        Preferences.writeString(this, Preferences.EMERGENCY_NUMBER, emergency_number.getText().toString());

    }



    public boolean check()
    {
        boolean ans = false;
        boolean nm = false;
        boolean num = false;
        boolean bld = false;
        boolean emnum = false;
        boolean emname = false;

        if (name.getText().toString() != null) {
            Matcher mname = vname.matcher(name.getText().toString());

            if (mname.matches()) {
                nm = true;
                name.setTextColor(Color.BLACK);
                fname.setVisibility(TextView.INVISIBLE);
            } else {
                nm = false;
                Log.v("VALIDATIONS", "INVALID NAME");
                name.setTextColor(Color.RED);
                name.setHintTextColor(Color.RED);
                fname.setVisibility(TextView.VISIBLE);
                fname.setTextColor(Color.RED);
            }
        }
        if (phone_number.getText().toString() != null) {
            Matcher mnumber = vnumber.matcher(phone_number.getText().toString());
            if (mnumber.matches()) {
                num = true;
                phone_number.setTextColor(Color.BLACK);
                fnum.setVisibility(TextView.INVISIBLE);
            } else {
                num = false;
                phone_number.setTextColor(Color.RED);
                phone_number.setHintTextColor(Color.RED);
                fnum.setVisibility(TextView.VISIBLE);
                fnum.setTextColor(Color.RED);
            }
        }
        if (spinner.getSelectedItemPosition() != 0) {
            bld = true;

        }
        else {
            spinner.setBackgroundColor(Color.RED);
            bld = false;
        }
        if (emergency_name.getText().toString() != null) {
            Matcher memname = em_name.matcher(emergency_name.getText().toString());

            if (memname.matches()) {
                emname = true;
                emergency_name.setTextColor(Color.BLACK);
                fename.setVisibility(TextView.INVISIBLE);
            } else {
                emname = false;
                emergency_name.setTextColor(Color.RED);
                emergency_name.setHintTextColor(Color.RED);
                fename.setVisibility(TextView.VISIBLE);
                fename.setTextColor(Color.RED);
            }
        }
        if (emergency_number.getText().toString() != null) {
            Matcher memnumber = em_number.matcher(emergency_number.getText().toString());

            if (memnumber.matches()) {
                emnum = true;
                emergency_number.setTextColor(Color.BLACK);
                fenum.setVisibility(TextView.INVISIBLE);
            } else {
                emnum = false;
                emergency_number.setTextColor(Color.RED);
                emergency_number.setHintTextColor(Color.RED);
                fenum.setVisibility(TextView.VISIBLE);
                fenum.setTextColor(Color.RED);
            }
        }
        if (nm && num  && bld && emname && emnum) {
            ans = true;
        }
        return ans;

    }

    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.button1) {
            if (check()) {
                save();
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }
        } else if (id == R.id.button2) {
            this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onResume()
    {
        super.onResume();
    }

    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
}
