package com.example.gaminglibrary.model;

import android.net.Uri;

import androidx.annotation.NonNull;

public class GameModel implements Comparable<GameModel>{
    private int id;
    private String name;
    private float price;
    private int rating;
    private int listID;
    private Uri imageFromPath;

    public GameModel(int id, String name, float price, int rating, int listID, Uri imageFromPath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.listID = listID;
        this.imageFromPath = imageFromPath;
    }
    public GameModel(int id, String name, float price, int rating, int listID) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.listID = listID;
    }

    public Uri getUri(){
        return this.imageFromPath;
    }

    public void setUri(Uri imageFromPath){
        this.imageFromPath = imageFromPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    @NonNull
    @Override
    public String toString() {
        return "GameModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", listId=" + listID +
                '}';
    }

    @Override
    public int compareTo(GameModel other) {
        return this.name.compareTo(other.getName());
    }
}