package com.example.login.Model;

public class RentParkingModel {
    private String parkingId;
    private String renterId;
    private String status;
    private String message;
    // getter setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getRenterId() {
        return renterId;
    }

    public void setRenterId(String renterId) {
        this.renterId = renterId;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
