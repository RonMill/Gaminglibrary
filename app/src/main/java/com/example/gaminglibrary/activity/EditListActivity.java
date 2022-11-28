package com.example.gaminglibrary.activity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.R;
import com.example.gaminglibrary.database.ListDatabase;
import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class EditListActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    TextView textView;
    LinearLayout linearLayout;
    Button save, delete;
    AlertDialog alertDialog;
    ListDatabase listDatabase;
    ListModel currentList;
    private int allListSize;
    private ArrayList<Integer> allBoxIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        listDatabase = new ListDatabase(this);
        editText = (EditText) findViewById(R.id.UPDATED_LIST_NAME);
        textView = (TextView) findViewById(R.id.MARK_FOR_DELETE);
        linearLayout = (LinearLayout) findViewById(R.id.table_for_checkbox);
        save = (Button) findViewById(R.id.BUTTON_SAVE);
        delete = (Button) findViewById(R.id.BUTTON_DELETE);
        currentList = (ListModel) getIntent().getSerializableExtra("CURRENTLIST");
        allListSize = getIntent().getIntExtra("ALLLISTSIZE", -1);


        editText.setText(currentList.getName());

        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        if (currentList.getGames().isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            loadCheckboxGames();
        }
    }

    private void loadCheckboxGames() {
        for (GameModel gameModel : currentList.getGames()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(gameModel.getName());
            checkBox.setId(gameModel.getId());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //is chkIos checked?
                    if (((CheckBox) v).isChecked()) {
                        if (!allBoxIDs.contains(v.getId())) {
                            allBoxIDs.add(v.getId());
                        }
                    } else {
                        allBoxIDs.remove(allBoxIDs.indexOf(v.getId()));
                    }
                    Log.d("HS_KL", allBoxIDs.toString());
                }
            });
            linearLayout.addView(checkBox);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == delete.getId()) {
            buildDeleteDialog();
            alertDialog.show();
        } else if (view.getId() == save.getId()) {
            String newListName = editText.getText().toString();
            listDatabase.deleteSelectedGames(allBoxIDs, currentList.getId());
            boolean insideIf = false;
            Intent resultIntent = new Intent();
            if (!currentList.getName().equals(newListName)) {
                listDatabase.updateListName(currentList.getId(), newListName);
                insideIf = true;
            }
            if (currentList.getGames().size() > allBoxIDs.size()) {
                for (int i : allBoxIDs) {
                    listDatabase.changeGameID(i, currentList.getId());
                }
                insideIf = true;
            }
            if(insideIf){
                setResult(5, resultIntent);
            } else{
                setResult(6, resultIntent);
            }
            EditListActivity.this.finish();
            return;
        }
    }

    private void buildDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Liste löschen");
        builder.setMessage("Sind Sie sicher das Sie " + currentList.getName() + " löschen wollen?");

        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("HS_KL", currentList.toString());

                listDatabase.deleteSelectedList(currentList);
                listDatabase.deleteAllGamesFromCurrentList(currentList);
                allListSize = allListSize - 1;
                if (allListSize > 0) {
                    listDatabase.changeListIDs(currentList.getId());
                }
                Toast.makeText(EditListActivity.this, "Liste wurde erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(4, resultIntent);
                EditListActivity.this.finish();
                return;
            }
        });
        alertDialog = builder.create();
    }
}
