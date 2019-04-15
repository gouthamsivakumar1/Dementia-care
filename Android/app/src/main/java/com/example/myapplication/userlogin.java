package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.reminder.patientHealth;

public class userlogin extends AppCompatActivity {

    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
    }

    public void sendMsg(View v)
    {
        Intent intent =new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
    public void patient_health(View v)
    {
        Log.i(TAG, "hit patient_health");
        Intent intent =new Intent(this, patientHealth.class);
        startActivity(intent);

    }
}
