package com.example.admin.lifesaver;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateEM extends AppCompatActivity {

    public static EditText emergency_name, emergency_number;
    public static TextView fename, fenum;

    Pattern em_name = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
    Pattern em_number = Pattern.compile("[0-9]{10}|[0-9]{7}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_em);

        emergency_name = (EditText) findViewById(R.id.emname_feild);

        emergency_number = (EditText) findViewById(R.id.emnumber_feild);
        emergency_number.setInputType(InputType.TYPE_CLASS_PHONE);

        emergency_name.setText(Preferences.readString(this,Preferences.EMERGENCY_CONTACT));
        emergency_number.setText(Preferences.readString(this,Preferences.EMERGENCY_NUMBER));

        fename = (TextView) findViewById(R.id.format_emergency_name);
        fename.setVisibility(TextView.INVISIBLE);

        fenum = (TextView) findViewById(R.id.format_emergency_number);
        fenum.setVisibility(TextView.INVISIBLE);
    }

    public void save()
    {
        Preferences.writeString(this, Preferences.EMERGENCY_CONTACT, emergency_name.getText().toString());
        Preferences.writeString(this, Preferences.EMERGENCY_NUMBER, emergency_number.getText().toString());

    }

    public boolean check()
    {
        boolean ans = false;
        boolean emnum = false;
        boolean emname = false;

        if (emergency_name.getText().toString() != null) {
            Matcher memname = em_name.matcher(emergency_name.getText().toString());

            if (memname.matches()) {
                emname = true;
                emergency_name.setTextColor(Color.BLACK);
                fename.setVisibility(TextView.INVISIBLE);
            } else {
                emname = false;
                Log.v("VALIDATIONS", "INVALID EMERGENCY NAME");
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
                Log.v("VALIDATIONS", "INVALID EMERGENCY NUMBER");
                emergency_number.setTextColor(Color.RED);
                emergency_number.setHintTextColor(Color.RED);
                fenum.setVisibility(TextView.VISIBLE);
                fenum.setTextColor(Color.RED);
            }
        }
        if (emname && emnum) {
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
