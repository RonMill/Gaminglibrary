package com.example.gaminglibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity {

    ListenDatenbank listenDatenbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenDatenbank = new ListenDatenbank(this);
        //db = new BuchDatenbank(this);
        listenDatenbank.insertListe(1,"testliste");
        listenDatenbank.insertSpiel(1,"League", 1.33F,3,1);
        listenDatenbank.insertKategorie(1,1,"MMOGA");
        listenDatenbank.insertTag(1,1,"Killergame");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.start_activity_menu, menu);
        return true;
    }
}