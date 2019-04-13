package com.example.myapplication;

//import com.test.dcare.RestAllResponse;
import com.example.myapplication.RestAllResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "http://10.0.2.2:5000";
    @GET("all")
    Call<List<RestAllResponse>> getPatient();
    @GET("patient/{uname}/{upass}")
    Call<List<RestLoginResponse>> getPatientLogin(@Path("uname") String username, @Path("upass") String password);
}
