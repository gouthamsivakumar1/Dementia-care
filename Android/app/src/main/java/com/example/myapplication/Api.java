package com.example.myapplication;

//import com.test.dcare.RestAllResponse;
import com.example.myapplication.RestAllResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "http://10.0.2.2:5000";
    @GET("all/{cursor}")
    Call<List<RestAllResponse>> getPatientAll(@Path("cursor") String cursor);
    @GET("patient/{patient_id}/{cursor}")
    Call<List<RestAllResponse>> getPatientWithId(@Path("patient_id") String patient_id, @Path("cursor") String cursor);
    @GET("patient-login/{uname}/{upass}")
    Call<List<RestLoginResponse>> getPatientLogin(@Path("uname") String username, @Path("upass") String password);
}
