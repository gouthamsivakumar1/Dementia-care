package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAlarmReceiver2 extends BroadcastReceiver {
    static private String TAG= "Dcare";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG, "MyAlarmReceiver2 onReceive called...");
        DcareAppCtx ctx = (DcareAppCtx) context.getApplicationContext();
        Log.i(TAG, "MyAlarmReceiver2 ctx.user_id="+ctx.user_id+" cursor="+ctx.cursor);

        Intent i = new Intent(context, RestFetcher.class);
        i.setAction(RestFetcher.ACTION_ALERT);
        i.putExtra(RestFetcher.EXTRA_PARAM1_REQ_TYPE, String.valueOf(ctx.user_id));

        String cursor_str = String.valueOf(ctx.cursor);
        i.putExtra(RestFetcher.EXTRA_PARAM2_CURSOR, cursor_str);
        // Start the service
        context.startService(i);

    }
}
