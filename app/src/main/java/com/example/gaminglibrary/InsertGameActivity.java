package com.example.gaminglibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.io.IOException;
import java.util.ArrayList;

public class InsertGameActivity extends AppCompatActivity implements View.OnClickListener {
    ListenDatenbank db;
    Button addGame, loadPicture;
    EditText gameName, gamePrice, gameReview;
    ImageView currentGameImage;
    //ListModel currentList;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_game);
        db = new ListenDatenbank(this);
        Intent i1 = getIntent();

        //currentList = (ListModel) i1.getSerializableExtra("CURRENTLIST");

        addGame = (Button) findViewById(R.id.ADD_GAME);
        loadPicture = (Button) findViewById(R.id.ADD_GALARY);
        gameName = (EditText) findViewById(R.id.GAME_NAME);
        gamePrice = (EditText) findViewById(R.id.GAME_PRICE);
        gameReview = (EditText) findViewById(R.id.GAME_REVIEW);
        currentGameImage = (ImageView) findViewById(R.id.GAME_CREATE_IMAGE);

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            try {
                                // There are no request codes
                                Intent data = result.getData();
                                imageFilePath = data.getData();
                                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                                currentGameImage.setImageBitmap(imageToStore);
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });
        addGame.setOnClickListener(this);
        loadPicture.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addGame.getId()) {
            Float price = Float.parseFloat(String.valueOf(gamePrice.getText()));
            int review = Integer.parseInt(String.valueOf(gameReview.getText()));
            if (!imageFilePath.equals(null)) { // if user select a picture
                db.insertSpiel(MainActivity.currentList.getGames().size() + 1, gameName.getText().toString(), price, review, MainActivity.currentList.getId(), String.valueOf(imageFilePath));
                MainActivity.currentList.getGames().add(new GameModel(MainActivity.currentList.getGames().size() + 1, gameName.getText().toString(), price, review, MainActivity.currentList.getId(), imageFilePath));
            } else {
                db.insertSpiel(MainActivity.currentList.getGames().size() + 1, gameName.getText().toString(), price, review, MainActivity.currentList.getId());
                MainActivity.currentList.getGames().add(new GameModel(MainActivity.currentList.getGames().size() + 1, gameName.getText().toString(), price, review, MainActivity.currentList.getId(), null));
            }

            Toast t = Toast.makeText(this, "Spiel hinzugefügt", Toast.LENGTH_SHORT);
            t.show();

            this.finish();
        } else if (view.getId() == loadPicture.getId()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            someActivityResultLauncher.launch(intent);
            currentGameImage.setImageBitmap(imageToStore);
        }
    }

}

