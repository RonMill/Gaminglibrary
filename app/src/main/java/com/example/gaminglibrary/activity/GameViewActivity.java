package com.example.gaminglibrary.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.R;
import com.example.gaminglibrary.database.ListDatabase;
import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.io.File;
import java.io.IOException;

public class GameViewActivity extends AppCompatActivity {
    ListDatabase db;
    TextView gameName, gamePrice, gameReview;
    ImageView currentGameImage;
    ListModel currentList;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    private String filePath;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        db = new ListDatabase(this);
        Intent i1 = getIntent();

        currentList = (ListModel) i1.getSerializableExtra("CURRENTLIST");
        this.setTitle(currentList.getName());

        gameName = (TextView) findViewById(R.id.GAME_NAME);
        gamePrice = (TextView) findViewById(R.id.GAME_PRICE);
        gameReview = (TextView) findViewById(R.id.GAME_REVIEW);
        currentGameImage = (ImageView) findViewById(R.id.GAME_CREATE_IMAGE);


        index = i1.getIntExtra("INDEX", -1);

        if (index != -1) {
            GameModel gameModel = currentList.getGames().get(index);

            if (gameModel.getImageFromPath() != null) {
                Bitmap imageToStore = BitmapFactory.decodeFile(gameModel.getImageFromPath());
                currentGameImage.setImageBitmap(imageToStore);
            } else {
                currentGameImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            gameName.setText(gameModel.getName());
            gamePrice.setText(String.valueOf(gameModel.getPrice())+" €");
            gameReview.setText(String.valueOf(gameModel.getRating())+" ★");
        }

    }

}

