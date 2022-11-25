package com.example.gaminglibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListModel implements Parcelable, Serializable {
    private int id;
    private String name;
    private ArrayList<GameModel> games;

    public ListModel(int id, String name, ArrayList<GameModel> games) {
        this.id = id;
        this.name = name;
        this.games = games;
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

    public ArrayList<GameModel> getGames() {
        return games;
    }

    public void setGames(ArrayList<GameModel> games) {
        this.games = games;
    }

    public void addGameToList(GameModel gameModel) {
        games.add(gameModel);
    }

    public int deleteGameFromList(GameModel gameModel) {
        if (games.contains(gameModel)) {
            games.remove(gameModel);
            return 1;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ListModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", games=" + games.toString() +
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
        parcel.writeList(games);
    }
}
