package com.example.gaminglibrary.API;

public class Coordinates {

    private String lon;
    private String lat;

    public Coordinates(String lon, String lat){
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
