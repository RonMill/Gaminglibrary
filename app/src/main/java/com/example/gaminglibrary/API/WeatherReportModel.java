package com.example.gaminglibrary.API;

public class WeatherReportModel {
    private Coordinates coordinates;
    private String description;
    private Temperature temperature;


    public WeatherReportModel() {
    }

    public WeatherReportModel(Coordinates coordinates, String description, Temperature temperature) {
        this.coordinates = coordinates;
        this.description = description;
        this.temperature = temperature;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "WeatherReportModel{" +
                "coordinates=" + coordinates +
                ", description='" + description + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}
