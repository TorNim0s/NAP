package com.example.login;

import com.google.firebase.database.PropertyName;

public class ParkingModel {

    @PropertyName("city")
    private String city;

    @PropertyName("street")
    private String street;

    @PropertyName("homeNum")
    private int homeNum;

    public ParkingModel(){

    }

    public ParkingModel(String city, String street, int homeNum){
        this.city = city;
        this.street = street;
        this.homeNum = homeNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHomeNum() {
        return homeNum;
    }

    public void setHomeNum(int homeNum) {
        this.homeNum = homeNum;
    }
}