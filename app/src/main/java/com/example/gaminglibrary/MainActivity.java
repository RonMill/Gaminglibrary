package com.example.gaminglibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> liste = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        liste.add("Woof");
        liste.add("Miau");
        liste.add("Hund");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.start_activity_menu, menu);

        SubMenu subMenu = menu.findItem(R.id.MY_LISTS).getSubMenu();
        subMenu.clear();
        for (String s : liste) {
            subMenu.add(0, R.id.EDIT, Menu.NONE, s);
        }


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
}