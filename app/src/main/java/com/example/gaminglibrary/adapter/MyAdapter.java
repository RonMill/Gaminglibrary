package com.example.gaminglibrary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gaminglibrary.R;

import java.io.File;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gaminglibrary.R;
import com.example.gaminglibrary.model.GameModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<GameModel> {

    private Context mainContext;
    private ArrayList<GameModel> gameModelList;

    public MyAdapter(@NonNull Context context, ArrayList<GameModel> arrayList) {
        super(context, 0, arrayList);
        mainContext = context;
        gameModelList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) listItem = LayoutInflater.from(mainContext).inflate(R.layout.simple_game_layout,parent,false);
        GameModel gameModel = gameModelList.get(position);
        ImageView imageView = (ImageView) listItem.findViewById(R.id.MY_PERSON_PICTURE);

        if(gameModel.getImageFromPath() != null){
            Bitmap imageToStore = BitmapFactory.decodeFile(gameModel.getImageFromPath());
            imageView.setImageBitmap(imageToStore);
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        TextView name = (TextView) listItem.findViewById(R.id.MY_PERSON_NAME);
        name.setText(gameModel.getName());
        TextView price = (TextView) listItem.findViewById(R.id.MY_PERSON_ADRESS);
        price.setText(String.valueOf(gameModel.getPrice()));
        TextView rating = (TextView) listItem.findViewById(R.id.MY_RATING);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i< gameModel.getRating();i++)stringBuilder.append("★");
        rating.setText(stringBuilder);
        return listItem;
    }
}

/*
        StringBuilder stringBuilder = new StringBuilder();
        if (c.getString(c.getColumnIndexOrThrow(from[0])) == null) {



        } else {
            File imagePath = new File(c.getString(c.getColumnIndexOrThrow(from[0])));
            ImageView imageView = (ImageView) v.findViewById(to[0]);
            Bitmap imageToStore = BitmapFactory.decodeFile(imagePath.getAbsolutePath());
            imageView.setImageBitmap(imageToStore);
        }
        String text1 = c.getString(c.getColumnIndexOrThrow(from[1]));
        TextView textView1 = (TextView) v.findViewById(to[1]);
        textView1.setText(text1);
        String text2 = c.getString(c.getColumnIndexOrThrow(from[2]));
        TextView textView2 = (TextView) v.findViewById(to[2]);
        textView2.setText(text2 + "€");
        String text3 = c.getString(c.getColumnIndexOrThrow(from[3]));
        TextView textView3 = (TextView) v.findViewById(to[3]);
        for(int i = 0; i<Integer.parseInt(text3);i++)stringBuilder.append("★");
        textView3.setText(stringBuilder.toString());

 */
