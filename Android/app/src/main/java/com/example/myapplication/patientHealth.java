package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class patientHealth extends AppCompatActivity {
    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_health);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG, "patient health on create");
        Intent i = new Intent(this, RestFetcher.class);
        i.setAction("com.example.myapplication.action.GET_ALL");

        // Add extras to the bundle
        i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, "all");
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, "0");
        // Start the service
        startService(i);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(RestFetcher.ACTION_GET_ALL);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);
        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(testReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                //String resultValue = intent.getStringExtra("resultValue");
                List<RestAllResponse> patientHealth = (List<RestAllResponse>) intent.getSerializableExtra("LIST");
                String result="";
                for(RestAllResponse patientHealth1:patientHealth) {
                    result += patientHealth1.Date_n_Time + " "+ patientHealth1.Patient_Id +"\n";
                }
                //Log.i(TAG, "reveived from service: " + resultValue);
                TextView patientHealth_view = (TextView)findViewById(R.id.patient_health_scroll_view);
                patientHealth_view.setText(result + patientHealth_view.getText());
                //Toast.makeText(patientHealth.this, resultValue, Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException ex) {
                    Log.d(TAG,"finishing sleep...");

                }
                Intent i = new Intent(patientHealth.this, RestFetcher.class);
                i.setAction("com.example.myapplication.action.GET_ALL");

                // Add extras to the bundle
                i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, "all");

                DcareAppCtx ctx = (DcareAppCtx) patientHealth.this.getApplicationContext();
                String cursor_str = String.valueOf(ctx.cursor);
                i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, cursor_str);
                // Start the service
                startService(i);
            }
        }
    };
}
