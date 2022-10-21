package com.example.gaminglibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;

import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ListModel> allLists = new ArrayList<>();

    //TODO: Später bei verlassen der App currentList in sharedPref saven
    ListModel currentList;
    ListenDatenbank listenDatenbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenDatenbank = new ListenDatenbank(this);
        addSomeFakeData();
        refreshAllLists();
        //listenDatenbank.deleteList(allLists);
        //listenDatenbank.deleteList(searchListModelById(2),allLists);
        Log.d("HS_KL",allLists.toString());
        deleteListByID(2);
        Log.d("HS_KL",allLists.toString());


        if (allLists.isEmpty()) {
            //TODO: DIALOG NOCH ERSTELLEN UND HIER ÖFFNEN
        } else {
            currentList = allLists.get(0);
        }


        Log.d("HS_KL", currentList.toString());
    }

    private void deleteListByID(int id) {
        boolean found = false;
        ListModel foundListModel = null;
        for(ListModel listModel : allLists){
            if(listModel.getId()==id){
                listenDatenbank.deleteList(listModel,allLists);
                foundListModel = listModel;
                found = true;
            }
            if(found){
                listModel.setId(listModel.getId()-1);
            }
        }
        allLists.remove(foundListModel);
        listenDatenbank.changeIDs(allLists,id);
    }

    private void addSomeFakeData(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.start_activity_menu, menu);

        SubMenu subMenu = menu.findItem(R.id.MY_LISTS).getSubMenu();
        subMenu.clear();
        for (ListModel s : allLists) {
            subMenu.add(0, s.getId(), Menu.NONE, s.getName());
        }
        subMenu.add(0, allLists.size() + 1, Menu.NONE, "Liste hinzufügen");
        subMenu.getItem(allLists.size()).setIcon(R.drawable.ic_add_black_48dp);
        return true;
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
                if (allLists.size() + 1 == item.getItemId()) {
                    Log.d("HS_KL", "Liste Hinzufügen");
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