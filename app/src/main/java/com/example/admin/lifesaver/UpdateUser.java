package com.example.admin.lifesaver;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateUser extends AppCompatActivity {
    public static EditText name,  phone_number;
    public static TextView fname, fnum;
    public static Spinner spinner;

    Pattern vname = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
    Pattern vnumber = Pattern.compile("[0-9]{10}|[0-9]{7}");
    Pattern vaddr = Pattern.compile("[0-9]{1,4}\\s([A-Za-z]+\\s?([A-Za-z]{2,})?)\\s[A-Za-z]+,\\s[A-Za-z]{2,}");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        name = (EditText)findViewById(R.id.name_field);
        phone_number = (EditText)findViewById(R.id.number_field);
        phone_number.setInputType(InputType.TYPE_CLASS_PHONE);

        name.setText(Preferences.readString(this,Preferences.NAME));
        phone_number.setText(Preferences.readString(this,Preferences.PHONE_NUMBER));

        fname = (TextView) findViewById(R.id.format_name);
        fname.setVisibility(TextView.INVISIBLE);

        fnum = (TextView) findViewById(R.id.format_phone);
        fnum.setVisibility(TextView.INVISIBLE);

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

        //Toast.makeText(this,categories.indexOf(Preferences.readString(this,Preferences.BLOOD_TYPE))+":"+Preferences.readString(this,Preferences.BLOOD_TYPE),Toast.LENGTH_SHORT).show();
        spinner.setSelection(Integer.parseInt(Preferences.readString(this,Preferences.BLOOD_TYPE)));

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

    public void save() {
        Preferences.writeString(this, Preferences.NAME, name.getText().toString());
        Preferences.writeString(this, Preferences.PHONE_NUMBER, phone_number.getText().toString());
        Preferences.writeString(this, Preferences.BLOOD_TYPE, Integer.toString(spinner.getSelectedItemPosition()));
    }

    public boolean check()
    {
        boolean ans = false;
        boolean nm = false;
        boolean num = false;
        boolean bld = false;

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
                Log.v("VALIDATIONS", "INVALID NUMBER");
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

        if (nm && num  && bld) {
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
