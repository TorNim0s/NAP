package com.example.login;

import java.util.ArrayList;

public class UserModel {

    private String name, number, email;
    private ArrayList<ParkingModel> parkings;

    public UserModel(){

    }

    public UserModel(String name, String number, String email){
        this.name = name;
        this.number = number;
        this.email = email;
        parkings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addParking(ParkingModel parking){
        parkings.add(parking);
    }
}