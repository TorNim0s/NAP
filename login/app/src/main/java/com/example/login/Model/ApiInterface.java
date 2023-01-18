package com.example.login.Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/rent-parking")
    Call<RentParkingModel> rentParking(@Body RentParkingModel rentParkingModel);

}