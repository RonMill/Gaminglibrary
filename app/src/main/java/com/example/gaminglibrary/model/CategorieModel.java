package com.example.gaminglibrary.model;

import androidx.annotation.NonNull;

import java.util.List;

public class CategorieModel {
    private int categoryID;
    private String categoryName;
    private List<GameModel> gameModelList;

    public CategorieModel(int categoryID, String categoryName, List<GameModel> gameModelList) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.gameModelList = gameModelList;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<GameModel> getGameModelList() {
        return gameModelList;
    }

    public void setGameModelList(List<GameModel> gameModelList) {
        this.gameModelList = gameModelList;
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
        return "CategorieModel{" +
                "categorieId=" + categoryID +
                ", categorieName='" + categoryName + '\'' +
                ", gameModelList=" + gameModelList +
                '}';
    }
}