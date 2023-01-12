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

    public String getStartDataAsString(){
        return this.startTime.toString().substring(0, this.startTime.toString().indexOf(" GMT"));
    }

    public String getEndDataAsString(){
        return this.endTime.toString().substring(0, this.endTime.toString().indexOf(" GMT"));
    }
}
