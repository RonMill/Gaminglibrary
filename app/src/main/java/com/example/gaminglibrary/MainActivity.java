package com.example.gaminglibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ListModel> allLists = new ArrayList<>();

    //TODO: Später bei verlassen der App currentList in sharedPref saven
    ListModel currentList;
    ListenDatenbank listenDatenbank;
    AlertDialog dialog;
    public SubMenu subMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenDatenbank = new ListenDatenbank(this);
        //addSomeFakeData();
        refreshAllLists();
        //listenDatenbank.deleteList(allLists);
        //listenDatenbank.deleteList(searchListModelById(2),allLists);
        Log.d("HS_KL", allLists.toString());
        //deleteListByID(1);
        Log.d("HS_KL", allLists.toString());
        addingAlertBox();


        if (allLists.isEmpty()) {
            //TODO: DIALOG NOCH ERSTELLEN UND HIER ÖFFNEN
            Log.d("HS_KL", "Test");
            dialog.show();
            showToast("Keine Liste gefunden!");
        } else {
            currentList = allLists.get(0);
            Log.d("HS_KL", currentList.toString());
        }


    }

    /**
     * Creating alert box, if the user wanna create a new list or the app starts the first time
     */
    private void addingAlertBox() {
        // Set up the alert box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Liste hinzufügen");
        builder.setMessage("Was ist der Listennamen?");

        // define edit field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);

        // define negative and positive button
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                input.setText("");
            }
        });
        builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listenDatenbank.insertListe(allLists.size() + 1, String.valueOf(input.getText())); // save the listname in the db
                allLists.add(new ListModel(allLists.size() + 1, String.valueOf(input.getText()), new ArrayList<>()));
                showToast("Liste hinzugefügt!");
                input.setText("");
                updateSubMenu();
            }
        });
        dialog = builder.create(); // create current dialog

        //TODO: Menu updaten
    }

    /**
     * Show Toast
     *
     * @param s Text inside Toast
     */
    private void showToast(String s) {
        Toast myToast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        myToast.show(); // show info toast for the user
    }

    /**
     * Delete a list
     *
     * @param id ID from list
     */
    private void deleteListByID(int id) {
        boolean found = false;
        ListModel foundListModel = null;
        for (ListModel listModel : allLists) {
            if (listModel.getId() == id) {
                listenDatenbank.deleteList(listModel, allLists);
                foundListModel = listModel;
                found = true;
            }
            if (found) {
                listModel.setId(listModel.getId() - 1);
            }
        }
        allLists.remove(foundListModel);
        listenDatenbank.changeIDs(allLists, id);

    }

    private void addSomeFakeData() {
        listenDatenbank.insertListe(1, "testliste");
        listenDatenbank.insertListe(2, "zweiteListe");
        listenDatenbank.insertListe(3, "DÖNERR");
        listenDatenbank.insertListe(4, "DÖNERR1");
        listenDatenbank.insertListe(5, "DÖNERR2");
        listenDatenbank.insertListe(6, "DÖNERR3");
        listenDatenbank.insertSpiel(1, "League1", 1.33F, 3, 1);
        listenDatenbank.insertSpiel(2, "League2", 1.33F, 3, 1);
        listenDatenbank.insertSpiel(3, "League3", 1.33F, 3, 1);
        listenDatenbank.insertSpiel(4, "League4", 1.33F, 3, 1);
        listenDatenbank.insertSpiel(5, "League5", 1.33F, 3, 1);
        listenDatenbank.insertKategorie(1, 1, "MMOGA");
        listenDatenbank.insertTag(1, 1, "Killergame");
    }

    /**
     * Add interaction menu at the top
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.start_activity_menu, menu);

        subMenu = menu.findItem(R.id.MY_LISTS).getSubMenu();
        subMenu.clear();
        for (ListModel s : allLists) {
            subMenu.add(0, s.getId(), Menu.NONE, s.getName());
        }
        subMenu.add(0, allLists.size() + 1, Menu.NONE, "Liste hinzufügen");
        subMenu.getItem(allLists.size()).setIcon(R.drawable.ic_add_black_48dp);
        return true;
    }

    private void updateSubMenu(){
        subMenu.clear();
        for (ListModel s : allLists) {
            subMenu.add(0, s.getId(), Menu.NONE, s.getName());
        }
        subMenu.add(0, allLists.size() + 1, Menu.NONE, "Liste hinzufügen");
        subMenu.getItem(allLists.size()).setIcon(R.drawable.ic_add_black_48dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.EDIT:
                Log.d("HS_KL", "EDIT");
                return true;
            case R.id.TAGS:
                Log.d("HS_KL", "TAGS");
                return true;
            case R.id.STATISTIC:
                Log.d("HS_KL", "STATISTIC");
                return true;
            case R.id.SETTINGS:
                Log.d("HS_KL", "SETTINGS");
                return true;
            case R.id.IMPORT:
                Log.d("HS_KL", "IMPORT");
                return true;
            case R.id.EXPORT:
                Log.d("HS_KL", "EXPORT");
                return true;
            default:
                for (ListModel listModel : allLists) {
                    if (listModel.getId() == item.getItemId()) {
                        Log.d("HS_KL", listModel.getName());
                        currentList = listModel;
                        this.setTitle(listModel.getName());
                        return true;
                    }
                }
                if (allLists.size() + 1 == item.getItemId()) { // if the user choose Liste hinzufügen
                    Log.d("HS_KL", "Liste Hinzufügen");
                    dialog.show(); // show dialog to insert a list
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("Range")
    private void refreshAllLists() {
        try (Cursor cursor = listenDatenbank.selectAllLists()) {
            if (cursor.getCount() > 0) {
                do {
                    ArrayList<GameModel> spieleListe = new ArrayList<>();
                    int listeid = cursor.getInt(cursor.getColumnIndexOrThrow("listeid"));
                    String titel = cursor.getString(cursor.getColumnIndexOrThrow("titel"));
                    Cursor cursor1 = listenDatenbank.selectAllSpieleFromListe(listeid);

                    if (cursor1.getCount() > 0) {
                        do {
                            int spielID = cursor1.getInt(cursor1.getColumnIndexOrThrow("spielid"));
                            String spielname = cursor1.getString(cursor1.getColumnIndexOrThrow("spielname"));
                            float preis = cursor1.getFloat(cursor1.getColumnIndexOrThrow("preis"));
                            int bewertung = cursor1.getInt(cursor1.getColumnIndexOrThrow("bewertung"));
                            int listID = cursor1.getInt(cursor1.getColumnIndexOrThrow("listeid"));

                            spieleListe.add(new GameModel(spielID, spielname, preis, bewertung, listID));

                        } while (cursor1.moveToNext());
                    }
                    allLists.add(new ListModel(listeid, titel, spieleListe));
                } while (cursor.moveToNext());
            }
        }
    }
}