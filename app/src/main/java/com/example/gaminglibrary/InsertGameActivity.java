package com.example.gaminglibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;

public class InsertGameActivity extends AppCompatActivity implements View.OnClickListener {
    ListenDatenbank db;
    Button addGame;
    EditText gameName, gamePrice, gameReview;
    int listID;
    int size;
    int lastIndex;
    ListModel currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_game);
        db = new ListenDatenbank(this);
        Intent i1 = getIntent();


        //currentList = (ListModel) i1.getSerializableExtra("CURRENTLISt");


        addGame = (Button) findViewById(R.id.ADD_GAME);
        gameName = (EditText) findViewById(R.id.GAME_NAME);
        gamePrice = (EditText) findViewById(R.id.GAME_PRICE);
        gameReview = (EditText) findViewById(R.id.GAME_REVIEW);


        addGame.setOnClickListener(this);


    }
    //TODO: Insert in DB funkt net
    @Override
    public void onClick(View view) {
        if (view.getId() == addGame.getId()) {
            Float price = Float.parseFloat(String.valueOf(gamePrice.getText()));
            int review = Integer.parseInt(String.valueOf(gameReview.getText()));
            db.insertSpiel(currentList.getGames().size() + 1, gameName.getText().toString(), price, review, currentList.getId());
            if(currentList.getGames() == null){
                currentList.setGames(new ArrayList<GameModel>());
            }
            currentList.getGames().add(new GameModel(currentList.getGames().size() + 1, gameName.getText().toString(), price, review, currentList.getId()));
            Toast t = Toast.makeText(this, "Spiel hinzugefügt", Toast.LENGTH_SHORT);
            t.show();

            this.finish();
        }
    }
}

