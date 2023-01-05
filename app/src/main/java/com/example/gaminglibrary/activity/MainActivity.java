package com.example.gaminglibrary.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;

import com.example.gaminglibrary.adapter.MyAdapter;
import com.example.gaminglibrary.database.ListDatabase;
import com.example.gaminglibrary.R;
import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<ListModel> allLists = new ArrayList<>();
    ListView listView;
    private int STORAGE_PERMISSION_CODE = 1;
    private MyAdapter myAdapter;
    private int indexContextMenuItem;

    static ListModel currentList;
    static ListDatabase listDatabase;
    AlertDialog dialog;
    public SubMenu subMenu;
    ActivityResultLauncher<Intent> someActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Rechte um Bild aus der Bib zu holen
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        listView = (ListView) findViewById(R.id.MY_LISTVIEW);
        registerForContextMenu(listView);
        initDB();
        buildAlertBox(); // HIER ALA
        this.setTitle("KEINE LISTE AUSGEWAEHLT");

        // Wird benötigt, damit das Programm erst weiter läuft, wenn aus der Activity X etwas returned wird
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        // 2 = gameInsert; 3 = gameUpdate; 4 = editListActivity; 5 = deleted any games or updated name --> Update everything from currentlist; 6 = deleted every game
                        if (result.getResultCode() == 2 || result.getResultCode() == 3) {
                            currentList = (ListModel) data.getSerializableExtra("CURRENTLIST");
                            if (result.getResultCode() == 3) {
                                refreshList();
                            } else {
                                addGameIntoAllLists();
                            }
                        } else if (result.getResultCode() == 4) {
                            refreshAllLists();
                            if (allLists.size() > 0) {
                                currentList = allLists.get(0);
                                MainActivity.this.setTitle(currentList.getName());
                                loadGamesIntoAdapterView(currentList.getGames());
                            } else {
                                MainActivity.this.setTitle("KEINE LISTE AUSGEWAEHLT");
                                loadGamesIntoAdapterView(new ArrayList<GameModel>());
                                dialog.show();
                            }
                            updateSubMenu();
                        } else if (result.getResultCode() == 5 || result.getResultCode() == 6) {
                            if (result.getResultCode() == 5) {
                                refreshList();
                                MainActivity.this.setTitle(currentList.getName());
                                updateSubMenu();
                            } else {
                                allLists.get(currentList.getId() - 1).getGames().clear();
                                currentList.getGames().clear();
                                loadGamesIntoAdapterView(currentList.getGames());
                            }
                        }
                    }
                });


        if (allLists.isEmpty()) {
            showToast("Keine Liste gefunden!");
            dialog.show();
        } else {
            currentList = allLists.get(0);
            this.setTitle(allLists.get(0).getName());
            loadGamesIntoAdapterView(currentList.getGames());
        }
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.start_activity_contextmenu, menu);

        indexContextMenuItem = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        MenuItem edit = menu.findItem(R.id.CONTEXT_EDIT);
        MenuItem delete = menu.findItem(R.id.CONTEXT_DELETE);

        edit.setTitle(allLists.get(currentList.getId() - 1).getGames().get(indexContextMenuItem).getName() + " editieren");
        delete.setTitle(allLists.get(currentList.getId() - 1).getGames().get(indexContextMenuItem).getName() + " löschen");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CONTEXT_EDIT:
                Intent i1 = new Intent(this, InsertGameActivity.class);
                i1.putExtra("CURRENTLIST", (Parcelable) currentList);
                i1.putExtra("INDEX", indexContextMenuItem);
                setResult(Activity.RESULT_OK, i1);
                someActivityResultLauncher.launch(i1);
                return true;
            case R.id.CONTEXT_DELETE:
                listDatabase.deleteGame(currentList.getGames().get(indexContextMenuItem));

                //  > 1 da bei einem Spiel die IDs nicht angepasst werden müssen
                if (allLists.get(currentList.getId() - 1).getGames().size() > 1) {
                    listDatabase.changeGameID(currentList.getGames().get(indexContextMenuItem).getId(), currentList.getId());
                }
                refreshList();
                currentList = allLists.get(currentList.getId() - 1);
                loadGamesIntoAdapterView(currentList.getGames());
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Lädt alle Spiele in den Adapter rein --> Liste alle Spiele aus der aktuellen Liste auf der Startliste
     */
    public void loadGamesIntoAdapterView(ArrayList<GameModel> arrayList) {
        myAdapter = new MyAdapter(this, arrayList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent i1 = new Intent(MainActivity.this, GameViewActivity.class);
                i1.putExtra("CURRENTLIST", (Parcelable) currentList);
                i1.putExtra("INDEX", i);
                setResult(Activity.RESULT_OK, i1);
                someActivityResultLauncher.launch(i1);
            }
        });
    }


    private void initDB() {
        listDatabase = new ListDatabase(this);
        refreshAllLists();
    }

    /**
     * Erstelle eine AlertBox, wenn der Benutzer eine neue Liste hinzufügen möchte oder die App zum ersten Mal startet
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
                loadGamesIntoAdapterView(currentList.getGames());
                MainActivity.this.setTitle(currentList.getName());
            }
        });
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
     * Füge alle Listen im Optionsmenu hinzu
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
                if (allLists.size() > 0 && currentList != null) {
                    Intent i2 = new Intent(this, EditListActivity.class);
                    i2.putExtra("CURRENTLIST", (Parcelable) currentList);
                    i2.putExtra("ALLLISTSIZE", allLists.size());
                    setResult(Activity.RESULT_OK, i2);
                    someActivityResultLauncher.launch(i2);
                } else {
                    showToast("Es existiert keine Liste");
                }
                return true;
            case R.id.SORT_LETTER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    currentList.getGames().sort(Comparator.comparing(GameModel::getName));
                    allLists.get(currentList.getId() - 1).getGames().sort(Comparator.comparing(GameModel::getName));
                    loadGamesIntoAdapterView(currentList.getGames());
                }
                return true;
            case R.id.SORT_NUMBER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    currentList.getGames().sort(Comparator.comparing(GameModel::getPrice));
                    allLists.get(currentList.getId() - 1).getGames().sort(Comparator.comparing(GameModel::getPrice));
                    loadGamesIntoAdapterView(currentList.getGames());
                }
                return true;
            case R.id.WEATHER:
                Intent i = new Intent(this, WeatherActivity.class);
                startActivity(i);
                return true;
            case R.id.INSERT_GAME:
                if (allLists.size() > 0) {
                    Intent i1 = new Intent(this, InsertGameActivity.class);
                    i1.putExtra("CURRENTLIST", (Parcelable) currentList);
                    setResult(Activity.RESULT_OK, i1);
                    someActivityResultLauncher.launch(i1);
                } else {
                    showToast("Es existiert keine Liste!");
                }
                return true;
            default:
                // Dynamische Liste unserer Listen wird durchlaufen und verglichen, ob Itemid == listenid vorhanden ist  --> Ja: currentList update
                // Default, da switch-Case feste Parameter benötigt und die Liste dynamisch ist
                for (ListModel listModel : allLists) {
                    if (listModel.getId() == item.getItemId()) {
                        currentList = listModel;
                        this.setTitle(listModel.getName());
                        loadGamesIntoAdapterView(currentList.getGames());
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

    /**
     * Füge ein neues Spiel in die Liste allList hinzu und setze currentList neu
     */
    private void addGameIntoAllLists() {
        try (Cursor cursor = listDatabase.selectAllGamesFromList(currentList.getId())) {
            if (cursor.getCount() > 0) {
                do {
                    if (cursor.getInt(cursor.getColumnIndexOrThrow("_id")) == currentList.getGames().size()) {
                        int gameID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                        String gameName = cursor.getString(cursor.getColumnIndexOrThrow("spielname"));
                        float price = cursor.getFloat(cursor.getColumnIndexOrThrow("preis"));
                        int rating = cursor.getInt(cursor.getColumnIndexOrThrow("bewertung"));
                        int listID = cursor.getInt(cursor.getColumnIndexOrThrow("listeid"));
                        if (cursor.getString(cursor.getColumnIndexOrThrow("imageUri")) != null) {
                            String imageFromPath = cursor.getString(cursor.getColumnIndexOrThrow("imageUri"));
                            allLists.get(currentList.getId() - 1).getGames().add(new GameModel(gameID, gameName, price, rating, listID, imageFromPath));
                        } else {
                            allLists.get(currentList.getId() - 1).getGames().add(new GameModel(gameID, gameName, price, rating, listID, null));
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
        loadGamesIntoAdapterView(currentList.getGames());
        currentList = allLists.get(currentList.getId() - 1);
    }

    /**
     * Aktualisiere allGames in der aktuellen allLists und setzte currentList neu
     */
    private void refreshList() {
        allLists.get(currentList.getId() - 1).getGames().clear();
        Cursor cursor1 = listDatabase.selectAllGamesFromList(currentList.getId());

        Cursor cursor2 = listDatabase.selectListFromID(currentList.getId());
        if (cursor2.getCount() > 0) {
            allLists.get(currentList.getId() - 1).setName(cursor2.getString(cursor2.getColumnIndexOrThrow("titel")));
        }

        ArrayList<GameModel> gameList = new ArrayList<>();
        if (cursor1.getCount() > 0) {
            do {
                int gameID = cursor1.getInt(cursor1.getColumnIndexOrThrow("_id"));
                String gameName = cursor1.getString(cursor1.getColumnIndexOrThrow("spielname"));
                float price = cursor1.getFloat(cursor1.getColumnIndexOrThrow("preis"));
                int rating = cursor1.getInt(cursor1.getColumnIndexOrThrow("bewertung"));
                int listID = cursor1.getInt(cursor1.getColumnIndexOrThrow("listeid"));
                if (cursor1.getString(cursor1.getColumnIndexOrThrow("imageUri")) != null) {
                    String imageFromPath = cursor1.getString(cursor1.getColumnIndexOrThrow("imageUri"));
                    gameList.add(new GameModel(gameID, gameName, price, rating, listID, imageFromPath));
                } else {
                    gameList.add(new GameModel(gameID, gameName, price, rating, listID, null));
                }


            } while (cursor1.moveToNext());
            allLists.get(currentList.getId() - 1).setGames(gameList);
        }
        currentList = allLists.get(currentList.getId() - 1);
        loadGamesIntoAdapterView(currentList.getGames());
    }

    /**
     * Lädt jede Liste aus der Datenbank mit allen Spielen
     */
    @SuppressLint("Range")
    public void refreshAllLists() {
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
                            if (cursor1.getString(cursor1.getColumnIndexOrThrow("imageUri")) != null) {
                                String imageFromPath = cursor1.getString(cursor1.getColumnIndexOrThrow("imageUri"));
                                gameList.add(new GameModel(gameID, gameName, price, rating, listID, imageFromPath));
                            } else {
                                gameList.add(new GameModel(gameID, gameName, price, rating, listID, null));
                            }
                        } while (cursor1.moveToNext());
                    }
                    allLists.add(new ListModel(listeid, titel, gameList));
                } while (cursor.moveToNext());
            }
        }
    }
}