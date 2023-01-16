/*  class ActiveParking.java
    it represents a parking that is currently active.
    It contains several fields such as startTime, endTime, price,
    parkingId, status, renterId, and ownerId.
    These fields store information about the parking, including the start and end times,
    the price, the parking's ID, the status, the renter's ID, and the owner's ID.
    The class also contains several getter and setter methods to access these fields.
    Additionally, it has two methods, getStartDataAsString and getEndDataAsString,
    which return the start and end time in a formatted string respectively.  */

package com.example.login;

import java.sql.Time;
import java.util.Date;

public class ActiveParking {

    Date startTime, endTime;
    String price;
    String parkingId, status;
    String renterId, ownerId;

    public ActiveParking() {
    }

    // constructor that initializes the startTime, endTime, price, parkingId, ownerId,
    // and status of the parking
    public ActiveParking(long startTime, long endTime, String price, String parkingId, String ownerId, String status) {
        this.startTime = new Time(startTime);
        this.endTime = new Time(endTime);
        this.price = price;
        this.parkingId = parkingId;
        this.status = status;
        this.ownerId = ownerId;
        this.renterId = "";
    }

    public String getPrice() {
        return price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public String getStatus() {
        return status;
    }

    public String getRenterId() {
        return renterId;
    }

    public void setRenterId(String renterId) {
        this.renterId = renterId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    // returns the start time in a formatted string
    public String getStartDataAsString(){
        return this.startTime.toString();
//        return this.startTime.toString().substring(0, this.startTime.toString().indexOf(" GMT"));
    }

    // returns the end time in a formatted string
    public String getEndDataAsString(){
        return this.endTime.toString();
//        return this.endTime.toString().substring(0, this.endTime.toString().indexOf(" GMT"));
    }
}
