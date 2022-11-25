package com.example.gaminglibrary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;

import com.example.gaminglibrary.adapter.MyAdapter;
import com.example.gaminglibrary.database.ListDatabase;
import com.example.gaminglibrary.R;
import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {

    static List<ListModel> allLists = new ArrayList<>();
    ListView listView;
    private int STORAGE_PERMISSION_CODE = 1;
    private boolean sorting = false;
    private MyAdapter myAdapter;


    //TODO: Später bei verlassen der App currentList in sharedPref saven
    static ListModel currentList;
    static ListDatabase listDatabase;
    AlertDialog dialog;
    public SubMenu subMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        listView = (ListView) findViewById(R.id.MY_LISTVIEW);
        initDB();
        buildAlertBox();

        if (allLists.isEmpty()) {
            showToast("Keine Liste gefunden!");
            dialog.show();
        } else {
            currentList = allLists.get(0);
            //addSomeFakeData();
            this.setTitle(allLists.get(0).getName());
            loadGames(currentList.getGames());
        }
    }

    /**
     * load all games into adapter --> Show all games from the current list on the start page
     */
    public void loadGames(ArrayList<GameModel> arrayList) {
        myAdapter = new MyAdapter(this,arrayList);
        listView.setAdapter(myAdapter);
    }

    private void initDB() {
        listDatabase = new ListDatabase(this);
        refreshAllLists();
        //listenDatenbank.deleteList(allLists);
        //listenDatenbank.deleteList(searchListModelById(2),allLists);
        //deleteListByID(1);
        listDatabase.deleteAllGames(allLists);
        listDatabase.insertGame(1,"Simon",60F,5,1, "null");
        listDatabase.insertGame(2,"Benni",59F,4,1,"null");
        listDatabase.insertGame(3,"Ronny",58F,3,1, "null");
        listDatabase.insertGame(4,"Niklas",60F,2,1, "null");
        //deleteListByID(2);
    }

    /**
     * Creating alert box, if the user wanna create a new list or the app starts the first time
     */
    private void buildAlertBox() {
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
                listDatabase.insertList(allLists.size() + 1, String.valueOf(input.getText())); // save the listname in the db
                allLists.add(new ListModel(allLists.size() + 1, String.valueOf(input.getText()), new ArrayList<>()));
                showToast("Liste hinzugefügt!");
                input.setText("");
                updateSubMenu();
                currentList = allLists.get(allLists.size() - 1);
            }
        });
        //this.setTitle(allLists.get(allLists.size() - 1).getName());
        dialog = builder.create(); // create current dialog
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
                listDatabase.deleteList(listModel, allLists);
                foundListModel = listModel;
                found = true;
            }
            if (found) {
                listModel.setId(listModel.getId() - 1);
                allLists.remove(foundListModel);
                listDatabase.changeIDs(allLists, id);
            }
        }
    }

    private void addSomeFakeData() {
        /*listenDatenbank.insertListe(1, "testliste");
        listenDatenbank.insertListe(2, "zweiteListe");
        listenDatenbank.insertListe(3, "DÖNERR");
        listenDatenbank.insertListe(4, "DÖNERR1");
        listenDatenbank.insertListe(5, "DÖNERR2");
        listenDatenbank.insertListe(6, "DÖNERR3");*/
        listDatabase.insertGame(1, "League1", 1.33F, 3, currentList.getId());
        /*listenDatenbank.insertSpiel(currentList.getGames().size() + 1, "League2", 1.33F, 3, currentList.getId());
        listenDatenbank.insertSpiel(currentList.getGames().size() + 1, "League3", 1.33F, 3, currentList.getId());
        listenDatenbank.insertSpiel(currentList.getGames().size() + 1, "League4", 1.33F, 3, currentList.getId());
        listenDatenbank.insertSpiel(currentList.getGames().size() + 1, "League5", 1.33F, 3, currentList.getId());*/
        listDatabase.insertCategory(1, 1, "MMOGA");
        listDatabase.insertTag(1, 1, "Killergame");
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

    private void updateSubMenu() {
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
                return true;
            case R.id.SORT_LETTER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ArrayList<GameModel> arrayList = new ArrayList<>(currentList.getGames());
                    arrayList.sort(Comparator.comparing(GameModel::getName));
                    loadGames(arrayList);
                }
                return true;
            case R.id.SORT_NUMBER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ArrayList<GameModel> arrayList = new ArrayList<>(currentList.getGames());
                    arrayList.sort(Comparator.comparing(GameModel::getPrice));
                    loadGames(arrayList);
                }
                return true;
            case R.id.TAGS:
                return true;
            case R.id.STATISTIC:
                return true;
            case R.id.SETTINGS:
                return true;
            case R.id.IMPORT:
                return true;
            case R.id.EXPORT:
                return true;
            case R.id.INSERT_GAME:
                if (allLists.size() > 0) {
                    Intent i1 = new Intent(this, InsertGameActivity.class);
                    //i1.putExtra("CURRENTLIST", (Parcelable) currentList);
                    startActivity(i1);
                } else {
                    showToast("Es existiert keine Liste!");
                }
                return true;
            default:
                for (ListModel listModel : allLists) {
                    if (listModel.getId() == item.getItemId()) {
                        Log.d("HS_KL", listModel.getName());
                        currentList = listModel;
                        this.setTitle(listModel.getName());
                        loadGames(currentList.getGames());
                        return true;
                    }
                }
                if (allLists.size() + 1 == item.getItemId()) {
                    dialog.show(); // show dialog to insert a list
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("Range")
    public static void refreshAllLists() {
        allLists.clear(); // clear list to avoid double entrys
        try (Cursor cursor = listDatabase.selectAllLists()) {
            if (cursor.getCount() > 0) {
                do {
                    ArrayList<GameModel> gameList = new ArrayList<>();
                    int listeid = cursor.getInt(cursor.getColumnIndexOrThrow("listeid"));
                    String titel = cursor.getString(cursor.getColumnIndexOrThrow("titel"));
                    Cursor cursor1 = listDatabase.selectAllGamesFromList(listeid);

                    if (cursor1.getCount() > 0) {
                        do {
                            int gameID = cursor1.getInt(cursor1.getColumnIndexOrThrow("_id"));
                            String gameName = cursor1.getString(cursor1.getColumnIndexOrThrow("spielname"));
                            float price = cursor1.getFloat(cursor1.getColumnIndexOrThrow("preis"));
                            int rating = cursor1.getInt(cursor1.getColumnIndexOrThrow("bewertung"));
                            int listID = cursor1.getInt(cursor1.getColumnIndexOrThrow("listeid"));

                                gameList.add(new GameModel(gameID, gameName, price, rating, listID, null));



                        } while (cursor1.moveToNext());
                    }
                    allLists.add(new ListModel(listeid, titel, gameList));
                } while (cursor.moveToNext());
            }
        }
    }
}