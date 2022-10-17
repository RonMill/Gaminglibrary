package com.example.gaminglibrary.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TagModel {
    private int id;
    private List<GameModel> gameModelList;
    private String tagName;

    public TagModel(int id, List<GameModel> gameModelList, String tagName) {
        this.id = id;
        this.gameModelList = gameModelList;
        this.tagName = tagName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GameModel> getGameModelList() {
        return gameModelList;
    }

    public void setGameModelList(List<GameModel> gameModelList) {
        this.gameModelList = gameModelList;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void addGameToGameModelList(GameModel gameModel){
        gameModelList.add(gameModel);
    }
    public int deleteGameFromGameModelList(GameModel gameModel){
        if(gameModelList.contains(gameModel)){
            gameModelList.remove(gameModel);
            return 1;
        }else{
            return -1;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "TagModel{" +
                "id=" + id +
                ", gameModelList=" + gameModelList +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}