package com.example.gaminglibrary.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CategorieModel {
    private int categorieId;
    private String categorieName;
    private List<GameModel> gameModelList;

    public CategorieModel(int categorieId, String categorieName, List<GameModel> gameModelList) {
        this.categorieId = categorieId;
        this.categorieName = categorieName;
        this.gameModelList = gameModelList;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    public String getCategorieName() {
        return categorieName;
    }

    public void setCategorieName(String categorieName) {
        this.categorieName = categorieName;
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
                "categorieId=" + categorieId +
                ", categorieName='" + categorieName + '\'' +
                ", gameModelList=" + gameModelList +
                '}';
    }
}