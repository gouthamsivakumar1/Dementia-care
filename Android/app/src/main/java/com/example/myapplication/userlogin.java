package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class userlogin extends AppCompatActivity {

    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        Log.i(TAG, "user_name= "+ctx.user_name + " use id= "+ ctx.user_id+" cat="+ctx.user_category);
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
}
