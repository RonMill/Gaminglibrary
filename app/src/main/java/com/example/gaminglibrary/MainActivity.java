package com.example.gaminglibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ListModel> allLists = new ArrayList<>();

    ListenDatenbank listenDatenbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenDatenbank = new ListenDatenbank(this);
        //db = new BuchDatenbank(this);
        listenDatenbank.insertListe(1, "testliste");
        listenDatenbank.insertSpiel(1, "League", 1.33F, 3, 1);
        Log.d("HS_KL", "DB_INSERT_GAME");
        listenDatenbank.insertKategorie(1, 1, "MMOGA");
        listenDatenbank.insertTag(1, 1, "Killergame");
        refreshAllLists();

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        updateSpieleListe(item.getItemId());

        Array alleIDDB = getIdDBListe;



        for --> i = 0 bis i = alleIDDb.lenght
        if(item.getId() == alleIDDB[i]
            mach was


        //TODO: Switch-Case mit den Optionsmenü-Items suchen, hinzufügen, löschen, einstellung und cast implementieren
        switch (item.getItemId()) {
            /*case TEST:
                Log.d("HSKL", "SUCHEN");
                return true;

        }
        */
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    private void refreshAllLists() {
        //TODO: Cursor richtig auslesen und die Listen, welche returned werden, in eine ArrayList eintragen

        Log.d("HS_KL", "spacken");
        Cursor c1 = listenDatenbank.selectAllLists();
        Log.d("HS_KL", DatabaseUtils.dumpCursorToString(c1));
        int x = c1.getInt(c1.getColumnIndexOrThrow("listeid"));
        Log.d("HS_KL", String.valueOf(x));

        /*try (Cursor cursor = listenDatenbank.selectAllLists()) {
            while (cursor.moveToNext()) {
                Log.d("HS_KL", String.valueOf(cursor.getInt(cursor.getColumnIndex("listid"))));
            }
        }*/
        allLists.add(new ListModel(allLists.size() + 1, "Liste hinzufügen", null));
    }
}