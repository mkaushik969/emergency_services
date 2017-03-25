package com.example.admin.lifesaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.updateEM_button)
        {
           startActivity(new Intent(this,UpdateEM.class));
        }
        else if (id == R.id.updateUser_button)
        {
           startActivity(new Intent(this,UpdateUser.class));
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause(){
        super.onPause();
    }

    public void onResume(){
        super.onResume();
    }
}
