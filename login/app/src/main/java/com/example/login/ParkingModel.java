package com.example.login;

public class ParkingModel {

    private String ownerId;
    private String city;
    private String street;

    private int homeNum;
    private int ParkingNum;

    public ParkingModel(){}

    public ParkingModel(String id, String city, String street, int homeNum, int parkingNum){
        this.ownerId = id;
        this.city = city;
        this.street = street;
        this.homeNum = homeNum;
        this.ParkingNum = parkingNum;

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

    public int getParkingNum() {
        return ParkingNum;
    }

    public void setParkingNum(int parkingNum) {
        ParkingNum = parkingNum;
    }

    public String getOwnerId() {
        return ownerId;
    }
}