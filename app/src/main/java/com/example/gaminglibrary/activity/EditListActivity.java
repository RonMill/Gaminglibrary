package com.example.gaminglibrary.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.R;
import com.example.gaminglibrary.database.ListDatabase;
import com.example.gaminglibrary.model.GameModel;

public class EditListActivity  extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    TextView textView;
    LinearLayout linearLayout;
    Button save, delete;
    AlertDialog alertDialog;
    ListDatabase listDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        editText = (EditText) findViewById(R.id.UPDATED_LIST_NAME);
        textView = (TextView) findViewById(R.id.MARK_FOR_DELETE);
        linearLayout = (LinearLayout) findViewById(R.id.table_for_checkbox);
        save = (Button) findViewById(R.id.BUTTON_SAVE);
        delete = (Button) findViewById(R.id.BUTTON_DELETE);

        editText.setText(MainActivity.currentList.getName());

        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        if(MainActivity.currentList.getGames().isEmpty()){
            textView.setVisibility(View.GONE);
        }else{
            loadCheckboxGames();
        }
    }

    private void loadCheckboxGames() {
        for(GameModel gameModel:MainActivity.currentList.getGames()){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(gameModel.getName());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(checkBox);
        }
    }

    @Override
    public void onClick(View view) {

    }

    private void buildDeleteDialog() {

    }
}
