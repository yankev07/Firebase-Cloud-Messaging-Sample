package com.kevappsgaming.myapplication.Helper;

public class UserInformation {

    public String uid;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String departure;
    public String destination;

    public UserInformation(){

    }

    public UserInformation(String uid, String firstName, String lastName, String email, String password, String departure, String destination) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.departure = departure;
        this.destination = destination;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }
}

