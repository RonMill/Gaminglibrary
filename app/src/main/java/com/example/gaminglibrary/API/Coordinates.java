package com.example.gaminglibrary.API;

public class Coordinates {

    private String cityName;
    private String lon;
    private String lat;

    public Coordinates() {

    }

    public Coordinates(String lon, String lat, String cityName) {
        this.cityName = cityName;
        this.lon = lon;
        this.lat = lat;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "cityName='" + cityName + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
