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

import java.util.List;



import java.util.List;

public class patient_profile extends AppCompatActivity {
    public String TAG="Dcare";
    //public TextView welcome_cmt =findViewById(R.id.welcome_cmt);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        //DcareMainActivity ctx1= (DcareMainActivity)this.getApplicationContext();
        // welcome_cmt.setText(ctx1.usr.getText().toString());
        TextView welcome_cmt =findViewById(R.id.welcome_cmt);
        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        welcome_cmt.setText("Welcome\t"+ctx.user_name);
        TextView hos_text = findViewById(R.id.hospital_id);
        TextView bystander_text = findViewById(R.id.bystanders_name);
        TextView patient_text = findViewById(R.id.patient_id);
        hos_text.setText(ctx.hostpitalName);
        patient_text.setText(ctx.patientName);
        bystander_text.setText(ctx.ByStander_Name);
        if (ctx.user_category == 1) {

        }
        //Log.i(TAG, "patient specific info2 health on create user name: "+ctx.user_name+" "+ctx.user_id);
        //Intent i = new Intent(this, RestFetcher.class);
        //i.setAction(RestFetcher.ACTION_GET_ALL);

        // Add extras to the bundle
        //i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.user_id));
        //i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, "0");
        // Start the service
        //startService(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(RestFetcher.ACTION_GET_ALL);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);

    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(testReceiver);
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            DcareAppCtx ctx = (DcareAppCtx) patient_profile.this.getApplicationContext();
            TextView welcome_cmt =findViewById(R.id.welcome_cmt);

            //MainActivity ctx1 = (MainActivity)  PatientSpecificInfo2.this.getApplicationContext();
            if (resultCode == RESULT_OK) {
                //String resultValue = intent.getStringExtra("resultValue");
                List<RestAllResponse> patientHealth = (List<RestAllResponse>) intent.getSerializableExtra("LIST");
                String result="";
                TextView cal_text = findViewById(R.id.hospital_id);
                TextView step_text = findViewById(R.id.bystanders_name);
                TextView heart_text = findViewById(R.id.patient_id);
                ;
                if (patientHealth.size()>0) {

                    step_text.setText(ctx.user_name);
                    //Log.i(TAG,"get usrid:"+ctx1.usr);
                    int last_record = patientHealth.size()-1;

                    cal_text.setText(patientHealth.get(last_record).HospitalId);

                    heart_text.setText(patientHealth.get(last_record).Patient_Id);
                }
                Log.i(TAG, "set cal_text");
                for(RestAllResponse patientHealth1:patientHealth) {
                    result += patientHealth1.Date_n_Time + " "+ patientHealth1.Patient_Id +"\n";
                }
                Log.i(TAG, "reveived from service: " + result);
                // TextView patientHealth_view = (TextView)findViewById(R.id.patient_health_scroll_view);
                //patientHealth_view.setText(result + patientHealth_view.getText());
                /*try {
                    Thread.sleep(10000);
                } catch(InterruptedException ex) {
                    Log.d(TAG,"finishing sleep...");
                }*/
                Intent i = new Intent(patient_profile.this, RestFetcher.class);
                i.setAction(RestFetcher.ACTION_GET_ALL);
                i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.patient_id));


                String cursor_str = String.valueOf(ctx.cursor);
                i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, cursor_str);
                // Start the service
                startService(i);
            }
        }
    };
}


