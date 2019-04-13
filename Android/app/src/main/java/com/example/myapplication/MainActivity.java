package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {
    String TAG="Dcare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

    }
    public void sendMsg (View view)
    {
        final EditText usr = (EditText)findViewById(R.id.editText);
        final EditText pas = (EditText)findViewById(R.id.editText2);

        /*
        if (usr.getText().toString().equals("admin")&&pas.getText().toString().equals("admin")) {
            Intent intent = new Intent(this,userlogin.class);
            startActivity(intent);
        }
       else if (usr.getText().toString().equals("dem")&&pas.getText().toString().equals("dem")) {
            Intent intent = new Intent(this, userlogin.class);
            startActivity(intent);
        }
        else
            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<RestLoginResponse>> call = api.getPatientLogin(usr.getText().toString(), pas.getText().toString());

        call.enqueue(new Callback<List<RestLoginResponse>>() {
            @Override
            public void onResponse(Call<List<RestLoginResponse>> call, Response<List<RestLoginResponse>> response) {

                //In this point we got our hero list
                //thats damn easy right ;)
                List<RestLoginResponse> patientList = response.body();
                Log.i(TAG, "Rest response success= "+ patientList.get(0).Patient_Id);
                String pId = patientList.get(0).Patient_Id;
                if (pId.equalsIgnoreCase("unknown") == false) {
                    Intent intent = new Intent(MainActivity.this, userlogin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
                //now we can do whatever we want with this list
            }
            @Override
            public void onFailure(Call<List<RestLoginResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Rest fail..");
                Toast.makeText(MainActivity.this, "Failed to connect to server..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
