package com.example.gaminglibrary.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ListModel {
    private int id;
    private String name;
    private List<GameModel> games;

    public ListModel(int id, String name, List<GameModel> games) {
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

    public List<GameModel> getGames() {
        return games;
    }

    public void setGames(List<GameModel> games) {
        this.games = games;
    }

    public void addGameToList(GameModel gameModel){
        games.add(gameModel);
    }
    public int deleteGameFromList(GameModel gameModel){
        if(games.contains(gameModel)) {
            games.remove(gameModel);
            return 1;
        }else{
            return -1;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ListModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
