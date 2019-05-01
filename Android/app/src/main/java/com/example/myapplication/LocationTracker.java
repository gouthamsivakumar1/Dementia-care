package com.example.myapplication;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class LocationTracker extends AppCompatActivity {
    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracker);

        IntentFilter filter = new IntentFilter(RestFetcher.ACTION_GET_ALL);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);

        DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
        Log.i(TAG, "patient specific info2 health on create user name: "+ctx.user_name+" "+ctx.user_id);
        Intent i = new Intent(this, RestFetcher.class);
        i.setAction(RestFetcher.ACTION_GET_ALL);

        // Add extras to the bundle
        i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.user_id));
        i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, "0");
        // Start the service
        startService(i);
    }
    public void location_refresh(View v) {
        DcareAppCtx ctx = (DcareAppCtx) LocationTracker.this.getApplicationContext();
        Log.i(TAG, "clicked location refresh...");
        Intent i = new Intent(LocationTracker.this, RestFetcher.class);
        i.setAction(RestFetcher.ACTION_GET_ALL);
        i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.user_id));

        String cursor_str = "0";//String.valueOf(ctx.cursor);
        i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, cursor_str);
        // Start the service
        startService(i);
    }
    static int top_min_val = 100;
    static int top_max_val = 1000;
    static int top_reset_val = 500;

    public int calculate_location_point(int reader1, int reader2) {
        int reader_val = 0;
        if (reader1 != 0 && reader2 != 0) {
            if (reader2 > reader1) {
                reader_val = reader1;
            } else {
                reader_val = reader2;
            }
        } else {
            if (reader1 > reader2) {
                reader_val = reader1;
            } else {
                reader_val = reader2;
            }
        }
        int onepiece = (top_max_val - top_min_val)/100;
        Log.i(TAG, "one_piece="+String.valueOf(onepiece));

        if (reader_val==0) { // No value could be derived
            return top_reset_val;
        } else {
            if (reader1 == reader_val) {
                int reader_percentage = (int)(((float)(reader_val-39)/60)*100);

                Log.i(TAG, "reader_percentage="+String.valueOf(reader_percentage));
                int reader_calculated_val = top_min_val + (onepiece * reader_percentage);
                return reader_calculated_val;
            } else {
                int reader_percentage = ((reader_val-39)/60)*100;
                int reader_calculated_val = top_max_val - (onepiece * reader_percentage);
                return reader_calculated_val;
            }
        }
    }

    private BroadcastReceiver testReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "LocationTracker onReceive");
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            DcareAppCtx ctx = (DcareAppCtx) LocationTracker.this.getApplicationContext();
            if (resultCode == RESULT_OK) {
                RelativeLayout location_layout = findViewById(R.id.LocationIconLayout);

                ImageView location_drop = findViewById(R.id.LocationIcon);
                location_layout.removeView(location_drop);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, 200);

                int reader1 = 0;
                int reader2 = 0;
                List<RestAllResponse> patientHealth = (List<RestAllResponse>) intent.getSerializableExtra("LIST");
                if (patientHealth.size() > 0) {
                    for(RestAllResponse ph:patientHealth) {
                        if (ph.ReaderId.equalsIgnoreCase("reader1")) {
                            reader1=Integer.parseInt(ph.RSSI);
                            Log.i(TAG, "patient location reader1: "+String.valueOf(reader1));
                        }
                        if (ph.ReaderId.equalsIgnoreCase("reader2")) {
                            reader2=Integer.parseInt(ph.RSSI);
                            Log.i(TAG, "patient location reader2: "+String.valueOf(reader2));
                        }
                    }
                }
                int location_on_map = calculate_location_point(reader1, reader2);
                params.topMargin = location_on_map;
                location_drop.setLayoutParams(params);
                location_layout.addView(location_drop);
                Log.i(TAG, "Location TopMargin:"+String.valueOf(params.topMargin));

            }
        }
    };
}
