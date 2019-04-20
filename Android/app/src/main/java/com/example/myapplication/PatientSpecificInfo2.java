package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class PatientSpecificInfo2 extends AppCompatActivity {
    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_specific_info2);
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        Log.i(TAG, "patient specific info2 health on create user name: "+ctx.user_name+" "+ctx.user_id);
        Intent i = new Intent(this, RestFetcher.class);
        i.setAction("com.example.myapplication.action.GET_ALL");

        // Add extras to the bundle
        i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.user_id));
        i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, "0");
        // Start the service
        startService(i);
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
            DcareAppCtx ctx = (DcareAppCtx) PatientSpecificInfo2.this.getApplicationContext();
            if (resultCode == RESULT_OK) {
                //String resultValue = intent.getStringExtra("resultValue");
                List<RestAllResponse> patientHealth = (List<RestAllResponse>) intent.getSerializableExtra("LIST");
                String result="";
                for(RestAllResponse patientHealth1:patientHealth) {
                    result += patientHealth1.Date_n_Time + " "+ patientHealth1.Patient_Id +"\n";
                }
                Log.i(TAG, "reveived from service: " + result);
               // TextView patientHealth_view = (TextView)findViewById(R.id.patient_health_scroll_view);
                //patientHealth_view.setText(result + patientHealth_view.getText());
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException ex) {
                    Log.d(TAG,"finishing sleep...");
                }
                Intent i = new Intent(PatientSpecificInfo2.this, RestFetcher.class);

                i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.user_id));


                String cursor_str = String.valueOf(ctx.cursor);
                i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, cursor_str);
                // Start the service
                startService(i);
            }
        }
    };
}
