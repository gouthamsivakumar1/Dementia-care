package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class userlogin extends AppCompatActivity {

    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        Log.i(TAG, "user_name= "+ctx.user_name + " use id= "+ ctx.user_id+" cat="+ctx.user_category);
        if (ctx.user_category == 2) {
            start_alarm_service();
        }
    }
    public void patientprofile(View v)
    {
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        if(ctx.user_category == 1||ctx.user_category == 2) {
            Intent intent = new Intent(this, patient_profile.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, patient_profile_all.class);
            startActivity(intent);
        }

    }

    public void sendMsg(View v)
    {
        Intent intent =new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
    public void patient_health(View v)
    {
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        if (ctx.user_category == 1 || ctx.user_category == 2) {
            this.patient_health_with_id(v);
        } else {
            this.patient_health_all(v);
        }
    }
    public void patient_health_with_id(View v)
    {
        Log.i(TAG, "hit patient_health id");
        Intent intent =new Intent(this, PatientSpecificInfo2.class);
        startActivity(intent);

    }
    public void patient_health_all(View v)
    {
        Log.i(TAG, "hit patient_health");
        Intent intent =new Intent(this, patientHealth.class);
        startActivity(intent);

    }
    public void patient_location(View v)
    {
        Log.i(TAG, "hit patient_location tracker");
        Intent intent =new Intent(this, LocationTracker.class);
        startActivity(intent);
    }
    public void start_alarm_service()
    {
        // Construct an intent that will execute the AlarmReceiver
        Log.i(TAG, "Starting alarm service...");
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver2.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0/*MyAlarmReceiver2.REQUEST_CODE*/,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;

        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, interval, pIntent);
    }

}

