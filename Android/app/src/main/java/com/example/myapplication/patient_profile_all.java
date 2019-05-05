package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class patient_profile_all extends AppCompatActivity {

    public String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_all);

     Log.i(TAG, "patient health on create");
    Intent i = new Intent(this, RestFetcher.class);
        i.setAction("com.example.myapplication.action.GET_ALL");

    // Add extras to the bundle
        i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, "all");
    DcareAppCtx ctx = (DcareAppCtx) this.getApplicationContext();
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
            if (resultCode == RESULT_OK) {
                DcareAppCtx ctx1 = (DcareAppCtx)getApplicationContext();
                //String resultValue = intent.getStringExtra("resultValue");
                List<RestAllResponse> patientHealth = (List<RestAllResponse>) intent.getSerializableExtra("LIST");
                String result="{";
                String result1="";
                String result2="";
                //String item[]= new String[6];
                for(RestAllResponse patientHealth1:patientHealth) {

                    /*ListView simpleList;
                    for(int i=0;i<6;i++) {
                        BufferedReader bo = new BufferedReader(new InputStreamReader(System.in));
                         String name1=patientHealth.get(i).Date_n_Time;
                          item[i] =name1;
                    }*/

                    String item[] = {"1","2","3","4","5","6"};
                    String  SubItem[] = {"The apple tree is a deciduous tree in the rose family best known for its sweet, pomaceous fruit, the apple.",
                            "The banana is an edible fruit – botanically a berry – produced by several kinds of large herbaceous flowering plants in the genus Musa.",
                            "The lemon, Citrus limon Osbeck, is a species of small evergreen tree in the flowering plant family Rutaceae, native to Asia.",
                            "A cherry is the fruit of many plants of the genus Prunus, and is a fleshy drupe.",
                            "The garden strawberry is a widely grown hybrid species of the genus Fragaria, collectively known as the strawberries.",
                            "The avocado is a tree, long thought to have originated in South Central Mexico, classified as a member of the flowering plant family Lauraceae."};
                    int flags[] = {R.mipmap.icon_round, R.mipmap.icon_round,R.mipmap.icon_round,R.mipmap.icon_round, R.mipmap.icon_round,R.mipmap.icon_round,};
                    //result += patientHealth1.Patient_Id+"\n";
                    //result1+= ctx1.user_name+"\n";
                    //result2+=patientHealth1.HospitalId+"\n";
                    //simpleList = (ListView)findViewById(R.id.ListView);
                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),item,SubItem, flags);
                    //simpleList.setAdapter(customAdapter);
                }

                //TextView patientHealth_view = (TextView)findViewById(R.id.patient_name1);
                //patientHealth_view.setText(result + patientHealth_view.getText());
                //TextView patientHealth_view2 = (TextView)findViewById(R.id.bystanders_name1);
                //patientHealth_view2.setText(result1 + patientHealth_view2.getText());
                //TextView patientHealth_view3 = (TextView)findViewById(R.id.patient_id1);
                //patientHealth_view3.setText(result2 + patientHealth_view3.getText());


                   Intent i = new Intent(patient_profile_all.this, RestFetcher.class);
                i.setAction("com.example.myapplication.action.GET_ALL");

                // Add extras to the bundle
                i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, "all");

                DcareAppCtx ctx = (DcareAppCtx) patient_profile_all.this.getApplicationContext();
                String cursor_str = String.valueOf(ctx.cursor);
                i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, cursor_str);
                // Start the service
                startService(i);
            }
        }
    };
}


