package com.example.gaminglibrary.API;

public class Temperature {
    private String current;
    private String feels_like_temp;
    private String min;
    private String max;

    public Temperature() {
    }
    public Temperature(String current, String feels_like_temp, String min, String max) {
        this.current = current;
        this.feels_like_temp = feels_like_temp;
        this.min = min;
        this.max = max;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getFeels_like_temp() {
        return feels_like_temp;
    }

    public void setFeels_like_temp(String feels_like_temp) {
        this.feels_like_temp = feels_like_temp;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "current=" + current +
                ", feels_like_temp=" + feels_like_temp +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
