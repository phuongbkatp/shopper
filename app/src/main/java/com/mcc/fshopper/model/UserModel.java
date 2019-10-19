package com.mcc.fshopper.model;

import java.io.Serializable;

/**
 * Created by Ashiq on 5/31/16.
 */
public class UserModel implements Serializable {

    private String name;
    private String mobile;
    private String email;
    private String facebookId;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String googleAccountName;

    public UserModel(String name, String mobile, String email, String facebookId,
                     String address, String city, String state, String postalCode, String country) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.facebookId = facebookId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setGoogleAccountName(String googleAccountName) {
        this.googleAccountName = googleAccountName;
    }

    public String getGoogleAccountName() {
        return googleAccountName;
    }
}
