package com.example.myapplication;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RestFetcher extends IntentService {
    public String TAG="Dcare";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_GET_ALL = "com.example.myapplication.action.GET_ALL";
    private static final String ACTION_BAZ = "com.example.myapplication.action.BAZ";

    // TODO: Rename parameters
    //private static final String EXTRA_PARAM1 = "com.example.myapplication.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "com.example.myapplication.extra.PARAM2";

    public static final String EXTRA_PARAM1 = "com.example.myapplication.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.myapplication.extra.PARAM2";
    public RestFetcher() {
        super("RestFetcher");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetAll(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RestFetcher.class);
        intent.setAction(ACTION_GET_ALL);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RestFetcher.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent called");
        if (intent != null) {
            final String action = intent.getAction();Log.i(TAG, "onHandleIntent action=" + action);
            Log.i(TAG, "onHandleIntent action=" + action);
            final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            Log.i(TAG, "onHandleIntent params=" + param1+ " param2="+param2);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();
            Api api = retrofit.create(Api.class);
            Call<List<RestAllResponse>> call = api.getPatient();

            Log.i(TAG, "Calling Rest...");
            call.enqueue(new Callback<List<RestAllResponse>>() {
                @Override
                public void onResponse(Call<List<RestAllResponse>> call, Response<List<RestAllResponse>> response) {

                    List<RestAllResponse> patientHealth = response.body();
                    Log.i(TAG, "Rest response success= " + patientHealth.get(0).Date_n_Time);
                    Intent in = new Intent(ACTION_GET_ALL);
                    in.putExtra("resultCode", Activity.RESULT_OK);
                    in.putExtra("resultValue", "My Result Value. Passed in: " + patientHealth.get(0).Date_n_Time);
                    LocalBroadcastManager.getInstance(RestFetcher.this).sendBroadcast(in);

                    //patientList.get(0)
                    /*
                    Log.i(TAG, "Rest response success= "+ patientList.get(0).Patient_Id);
                    String pId = patientList.get(0).Patient_Id;
                    if (pId.equalsIgnoreCase("unknown") == false) {
                        Intent intent = new Intent(MainActivity.this, userlogin.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                    */
                    //now we can do whatever we want with this list
                }

                @Override
                public void onFailure(Call<List<RestAllResponse>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Rest fail..");
                    //Toast.makeText(MainActivity.this, "Failed to connect to server..", Toast.LENGTH_SHORT).show();
                }
            });

            /*
            if (ACTION_FOO.equals(action)) {
                Log.i(TAG, "Hit ACTION_FOO");
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                Log.i(TAG, "Hit ACTION_BAZ");
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
            */
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
