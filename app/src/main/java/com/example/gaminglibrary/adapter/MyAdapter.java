package com.example.gaminglibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

