package com.example.gaminglibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.database.ListDatabase;
import com.example.gaminglibrary.R;
import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.io.File;
import java.io.IOException;

public class InsertGameActivity extends AppCompatActivity implements View.OnClickListener {
    ListDatabase db;
    Button addGame, loadPicture;
    EditText gameName, gamePrice, gameReview;
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
        setContentView(R.layout.activity_insert_game);
        db = new ListDatabase(this);
        Intent i1 = getIntent();

        currentList = (ListModel) i1.getSerializableExtra("CURRENTLIST");
        this.setTitle(currentList.getName());


        addGame = (Button) findViewById(R.id.ADD_GAME);
        loadPicture = (Button) findViewById(R.id.ADD_GALARY);
        gameName = (EditText) findViewById(R.id.GAME_NAME);
        gamePrice = (EditText) findViewById(R.id.GAME_PRICE);
        gameReview = (EditText) findViewById(R.id.GAME_REVIEW);
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
            gamePrice.setText(String.valueOf(gameModel.getPrice()));
            gameReview.setText(String.valueOf(gameModel.getRating()));
            addGame.setText("Speichern");
        }

        // TODO: Bewertung komma zahlen wegschneiden
        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            try {
                                // There are no request codes
                                Intent data = result.getData();
                                imageFilePath = data.getData();
                                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                                grantUriPermission(
                                        getPackageName(), imageFilePath,
                                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                getContentResolver().takePersistableUriPermission(
                                        imageFilePath, Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                                String wholeID = DocumentsContract.getDocumentId(data.getData());
                                String id = wholeID.split(":")[1];
                                String[] column = {MediaStore.Images.Media.DATA};
                                String sel = MediaStore.Images.Media._ID + "=?";
                                Cursor cursor = getContentResolver().
                                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                column, sel, new String[]{id}, null);

                                int columnIndex = cursor.getColumnIndex(column[0]);

                                if (cursor.moveToFirst()) {
                                    filePath = cursor.getString(columnIndex);
                                }
                                File imgFile = new File(filePath);
                                Log.d("HS_KL", filePath);
                                Log.d("HS_KL", imgFile.getAbsolutePath());

                                if (imgFile.exists()) {
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    currentGameImage.setImageBitmap(myBitmap);
                                }
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        // on Click listener for each button
        addGame.setOnClickListener(this);
        loadPicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addGame.getId() && index == -1) { // check which button got pressed
            //int review = (int) Integer.parseInt(String.valueOf(gameReview.getText()));
            String tmp = String.valueOf(gameReview.getText());
            int indexPoint = tmp.indexOf(".");
            if(indexPoint != -1){
            tmp = tmp.substring(0, indexPoint);
            }
            int review = Integer.parseInt(tmp);
            Log.d("HS_KL", String.valueOf(review));
            if (review <= 5 && review >= 1) {
                Float price = Float.parseFloat(String.valueOf(gamePrice.getText()));
                if (imageFilePath != null) { // if user select a picture
                    currentList.getGames().add(new GameModel(currentList.getGames().size() == 0 ? 1 : currentList.getGames().size(), gameName.getText().toString(), price, review, currentList.getId(), filePath));
                    db.insertGame(currentList.getGames().size() == 0 ? 1 : currentList.getGames().size(), gameName.getText().toString(), price, review, currentList.getId(), filePath);
                } else {
                    currentList.getGames().add(new GameModel(currentList.getGames().size() == 0 ? 1 : currentList.getGames().size(), gameName.getText().toString(), price, review, currentList.getId(), null));
                    db.insertGame(currentList.getGames().size() == 0 ? 1 : currentList.getGames().size(), gameName.getText().toString(), price, review, currentList.getId());
                }
                Toast.makeText(this, "Spiel hinzugefügt", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("CURRENTLIST", (Parcelable) currentList);
                setResult(2, resultIntent);
                this.finish();
                return;
            } else {
                Toast.makeText(this, "Deine Bewertung ist zu hoch! (Range von 1-5)", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == addGame.getId() && index != -1) {
            //int review = (int) Integer.parseInt(String.valueOf(gameReview.getText()));
            String tmp = String.valueOf(gameReview.getText());
            int indexPoint = tmp.indexOf(".");
            tmp = tmp.substring(0, indexPoint);
            int review = Integer.parseInt(tmp);
            Log.d("HS_KL", String.valueOf(review));

            if (review <= 5 && review >= 1) {
                Float price = Float.parseFloat(String.valueOf(gamePrice.getText()));
                currentList.getGames().get(index).setName(gameName.getText().toString());
                currentList.getGames().get(index).setRating(review);
                currentList.getGames().get(index).setPrice(Float.parseFloat(gamePrice.getText().toString()));
                if (imageFilePath != null) { // if user select a picture
                    currentList.getGames().get(index).setImageFromPath(filePath);
                    db.updateGame(currentList.getGames().get(index).getId(), gameName.getText().toString(), price, review, filePath);
                } else {
                    db.updateGame(currentList.getGames().get(index).getId(), gameName.getText().toString(), price, review);
                }
                Toast.makeText(this, "Spiel gespeichert", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("CURRENTLIST", (Parcelable) currentList);
                setResult(3, resultIntent);
                this.finish();
                return;
            } else {
                Toast.makeText(this, "Deine Bewertung ist zu hoch! (Range von 1-5)", Toast.LENGTH_SHORT).show();
            }

        } else if (view.getId() == loadPicture.getId()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            someActivityResultLauncher.launch(intent);
            currentGameImage.setImageBitmap(imageToStore);
        }
    }

}

