package com.example.knowyourgovernment;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

public class Government implements Serializable {
    private String officeTitle;
    private ArrayList<Integer> officialIndices;
    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private String party;
    private String phoneNum;
    private String url;
    private String email;
    private String photoUrl;
    private String channels;

    public Government(String officeTitle, String streetAddress, String city, String state, String zip, String party, String phoneNum, String url, String email, String photoUrl, String name, String channels) {
        this.officeTitle = officeTitle;
        //this.officialIndices = officialIndices;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.party = party;
        this.phoneNum = phoneNum;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.channels = channels;
        this.name = name;
    }

    public String getOfficeTitle() {
        return officeTitle;
    }

//    public ArrayList<Integer> getOfficialIndices() {
//        return officialIndices;
//    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getParty() {
        return party;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getChannels() {
        return channels;
    }

    public String getName() {
        return name;
    }
}
