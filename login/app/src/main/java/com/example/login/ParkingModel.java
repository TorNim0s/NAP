package com.example.login;

import com.google.firebase.database.Exclude;

/**
 * The class ParkingModel is used to store information about a parking spot.
 * It has several fields such as ownerId, city, street, homeNum, and ParkingNum
 * which are used to store information about the owner of the parking spot,
 * the city where the parking spot is located,
 * the street where the parking spot is located,
 * the home number and the parking spot number.
 * The class also has a constructor that takes in the
 * ownerId, city, street, homeNum, and parkingNum
 * which are used to initialize the fields of the class.
 * Additionally, the class has getters and setters for each field
 * and an additional method setParkingId(String parkingId) which is used to set the parkingId of the parking spot.
 */
public class ParkingModel {

    // Exclude this field from serialization
    @Exclude
    private String parkingId;

    private String ownerId;
    private String city;
    private String street;

    private int homeNum;
    private int ParkingNum;

    // Empty constructor
    public ParkingModel() {
    }

    // Constructor with initial values
    public ParkingModel(String id, String city, String street, int homeNum, int parkingNum) {
        this.ownerId = id;
        this.city = city;
        this.street = street;
        this.homeNum = homeNum;
        this.ParkingNum = parkingNum;

    }

    // Getters and setters for the class fields

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

    public String getParkingId() {
        return parkingId;
    }

    // Method to set ParkingId, returns the same object
    public ParkingModel setParkingId(String parkingId) {
        this.parkingId = parkingId;
        return this;
    }
}