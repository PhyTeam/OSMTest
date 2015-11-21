package com.example.phuc.osmtest;

import org.osmdroid.util.GeoPoint;

/**
 * Created by NhatNam-PC on 11/21/2015.
 */
public class UserData {
    private String username;

    private GeoPoint loc;

    public UserData(String name, GeoPoint geo){
        this.username = name;
        this.loc = geo;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }
}
