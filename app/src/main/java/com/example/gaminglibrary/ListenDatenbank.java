package com.example.gaminglibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ListenDatenbank extends SQLiteOpenHelper {

    public static final int DATENBANK_VERSION = 1;
    public static final String DATENBANK_NAMEN = "Gaminglibrary.db";


    public static final String TABELLE_LISTE = "liste";
    public static final String SPALTE_LISTE_ID = "listeid";
    public static final String SPALTE_NAME = "titel";


    public static final String TABELLE_SPIEL = "spiel";
    public static final String SPALTE_SPIEL_ID = "spielid";
    public static final String SPALTE_SPIEL_NAME = "spielname";
    public static final String SPALTE_PREIS = "preis";
    public static final String SPALTE_BEWERTUNG = "bewertung";
    public static final String SPALTE_LISTE = "listeid";

    public static final String TABELLE_KATEGORIE = "kategorie";
    public static final String SPALTE_KATEGORIE_ID = "listeid";
    public static final String SPALTE_SPIEL = "spielid";
    public static final String SPALTE_KATEGORIE_NAME = "name";

    public static final String TABELLE_TAG = "tag";
    public static final String SPALTE_TAG_ID = "tagid";
    public static final String SPALTE_SPIELE = "spielid";
    public static final String SPALTE_TAG_NAME = "name";


    public ListenDatenbank(Context cxt) {
        super(cxt, DATENBANK_NAMEN, null, DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("HS_KL", "DB_CREATE1");
        db.execSQL(
                "CREATE TABLE " + TABELLE_LISTE + " (" +
                        SPALTE_LISTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_NAME + " TEXT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABELLE_SPIEL + " (" +
                        SPALTE_SPIEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_SPIEL_NAME + " TEXT," +
                        SPALTE_PREIS + " REAL," +
                        SPALTE_BEWERTUNG + " INTEGER check(SPALTE_BEWERTUNG >= 0 AND SPALTE_BEWERTUNG <= 5)," +
                        SPALTE_LISTE + " INTEGER" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABELLE_KATEGORIE + " (" +
                        SPALTE_KATEGORIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_SPIEL + " INTEGER," +
                        SPALTE_KATEGORIE_NAME + " TEXT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABELLE_TAG + " (" +
                        SPALTE_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_SPIELE + " INTEGER," +
                        SPALTE_TAG_NAME + " TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("HS_KL", "DB_UPGRADE");
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_LISTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_SPIEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_KATEGORIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_TAG);

        onCreate(db);
    }

    public void insertListe(int listID, String titel) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_LISTE_ID, listID);
        neueZeile.put(SPALTE_NAME, titel);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABELLE_LISTE, null, neueZeile);
    }

    public void insertSpiel(int spieleID, String spieleName, Float preis, int bewertung, int listID) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_SPIEL_ID, spieleID);
        neueZeile.put(SPALTE_SPIEL_NAME, spieleName);
        neueZeile.put(SPALTE_PREIS, preis);
        neueZeile.put(SPALTE_BEWERTUNG, bewertung);
        neueZeile.put(SPALTE_LISTE, listID);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABELLE_LISTE, null, neueZeile);
    }

    public void insertKategorie(int kategorieID, int spieleId, String kategorieName) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_KATEGORIE_ID, kategorieID);
        neueZeile.put(SPALTE_SPIEL, spieleId);
        neueZeile.put(SPALTE_KATEGORIE_NAME, kategorieName);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABELLE_LISTE, null, neueZeile);
    }

    public void insertTag(int tagID, int spieleId, String tagName) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_TAG_ID, tagID);
        neueZeile.put(SPALTE_SPIEL, spieleId);
        neueZeile.put(SPALTE_TAG_NAME, tagName);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABELLE_LISTE, null, neueZeile);
    }
}