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

public class InsertGameActivity extends AppCompatActivity implements View.OnClickListener {
    ListenDatenbank db;
    Button addGame;
    EditText gameName, gamePrice, gameReview;
    int listID;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_game);
        db = new ListenDatenbank(this);
        Intent i1 = getIntent();


        listID = i1.getIntExtra("LISTID", 0);
        size = i1.getIntExtra("LISTSIZE", 0);


        addGame = (Button) findViewById(R.id.ADD_GAME);
        gameName = (EditText) findViewById(R.id.GAME_NAME);
        gamePrice = (EditText) findViewById(R.id.GAME_PRICE);
        gameReview = (EditText) findViewById(R.id.GAME_REVIEW);


        addGame.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addGame.getId()) {
            Float price = Float.parseFloat(String.valueOf(gamePrice.getText()));
            int review = Integer.parseInt(String.valueOf(gameReview.getText()));
            db.insertSpiel(gameName.getText().toString(), price, review, listID);
            Toast t = Toast.makeText(this, "Spiel hinzugefügt", Toast.LENGTH_SHORT);
            t.show();


            this.finish();
        }
    }
}

