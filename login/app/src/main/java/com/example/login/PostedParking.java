package com.example.login;

public class PostedParking {

    String availableHours, price;
    String parkingId, status;
    String renterId;

    public PostedParking (){}

    public PostedParking(String availableHours, String price, String parkingId, String status) {
        this.availableHours = availableHours;
        this.price = price;
        this.parkingId = parkingId;
        this.status = status;
        this.renterId = "";
    }

    public String getAvailableHours() {
        return availableHours;
    }

    public String getPrice() {
        return price;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
