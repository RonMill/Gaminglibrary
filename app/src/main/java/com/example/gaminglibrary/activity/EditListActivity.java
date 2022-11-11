package com.example.gaminglibrary.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        if(view.getId()== delete.getId()){
            buildDeleteDialog();
            alertDialog.show();
        }
    }

    private void buildDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Liste löschen");
        builder.setMessage("Sind Sie sicher das Sie "+MainActivity.currentList.getName()+" löschen wollen?");
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: Wenn Benny hier das Currentlistproblem gefixt hat dann brauchen wir hier kein if
            }
        });
        builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: Wenn Benny hier das Currentlistproblem gefixt hat dann brauchen wir hier kein if
                Log.d("HS_KL",MainActivity.currentList.toString());
                listDatabase.deleteList(MainActivity.currentList);
                Toast.makeText(EditListActivity.this, "Liste wurde erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
                EditListActivity.this.finish();
            }
        });
        alertDialog = builder.create();
    }
}
