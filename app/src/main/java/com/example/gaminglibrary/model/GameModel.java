package com.example.gaminglibrary.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GameModel implements Parcelable {
    private int id;
    private String name;
    private float price;
    private int rating;
    private int listID;
    private String imageFromPath;

    public GameModel(int id, String name, float price, int rating, int listID, String imageFromPath) {
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

    protected GameModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readFloat();
        rating = in.readInt();
        listID = in.readInt();
        imageFromPath = in.readString();
    }

    public static final Creator<GameModel> CREATOR = new Creator<GameModel>() {
        @Override
        public GameModel createFromParcel(Parcel in) {
            return new GameModel(in);
        }

        @Override
        public GameModel[] newArray(int size) {
            return new GameModel[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeFloat(price);
        parcel.writeInt(rating);
        parcel.writeInt(listID);
        parcel.writeString(imageFromPath);
    }
}