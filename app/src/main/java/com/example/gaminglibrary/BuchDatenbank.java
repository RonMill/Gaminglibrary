package com.example.gaminglibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuchDatenbank extends SQLiteOpenHelper {

    public static final int DATENBANK_VERSION = 1;
    public static final String DATENBANK_NAMEN = "Buch.db";

    public static final String TABELLE_BUECHER = "buecher";
    public static final String SPALTE_BUCH_ID = "_id";
    public static final String SPALTE_BUCH_TITEL = "titel";
    public static final String SPALTE_BUCH_AUTOR = "autor";
    public static final String SPALTE_BUCH_ERSCHEINUNGSJAHR = "erscheinungsjahr";


    public BuchDatenbank(Context cxt) {
        super(cxt, DATENBANK_NAMEN, null, DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABELLE_BUECHER + " (" +
                        SPALTE_BUCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SPALTE_BUCH_TITEL + " TEXT," +
                        SPALTE_BUCH_AUTOR + " TEXT," +
                        SPALTE_BUCH_ERSCHEINUNGSJAHR + " INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_BUECHER);
        onCreate(db);
    }


    public Cursor selectAlleBuecher() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+TABELLE_BUECHER, null);
        meinZeiger.moveToFirst();
        return meinZeiger;
    }


    public void insertBuch(String titel, String autor, int jahr) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(SPALTE_BUCH_TITEL, titel);
        neueZeile.put(SPALTE_BUCH_AUTOR, autor);
        neueZeile.put(SPALTE_BUCH_ERSCHEINUNGSJAHR, jahr);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABELLE_BUECHER, null, neueZeile);
    }


    public String getBuchName(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor meinZeiger = db.rawQuery("SELECT * FROM "+TABELLE_BUECHER+" WHERE "+SPALTE_BUCH_ID+"="+id,null);
        meinZeiger.moveToFirst();
        return meinZeiger.toString();
    }


    public void deleteBuch(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String where = SPALTE_BUCH_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(id)};

        db.delete(TABELLE_BUECHER, where, whereArg);
    }
    
}
