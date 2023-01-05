package com.example.gaminglibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gaminglibrary.model.GameModel;
import com.example.gaminglibrary.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class ListDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAMES = "Gaminglibrary.db";


    public static final String TABLE_LIST = "liste";
    public static final String COLUMN_LIST_ID = "listeid";
    public static final String COLUMN_NAME = "titel";


    public static final String TABLE_GAME = "spiel";
    public static final String TABLE_GAME_ID = "_id";
    public static final String TABLE_GAME_NAME = "spielname";
    public static final String COLUMN_PRICE = "preis";
    public static final String COLUMN_RATING = "bewertung";
    public static final String COLUMN_LIST = "listeid";
    public static final String COLUMN_IMAGE_URI = "imageUri";

    public ListDatabase(Context cxt) {
        super(cxt, DATABASE_NAMES, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_LIST + " (" +
                        COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT NOT NULL" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_GAME + " (" +
                        TABLE_GAME_ID + " INTEGER NOT NULL," +
                        TABLE_GAME_NAME + " TEXT," +
                        COLUMN_PRICE + " REAL," +
                        COLUMN_RATING + " INTEGER," +
                        COLUMN_LIST + " INTEGER NOT NULL," +
                        COLUMN_IMAGE_URI + " TEXT," +
                        "PRIMARY KEY (" + TABLE_GAME_ID + ", " + COLUMN_LIST + ")" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        onCreate(db);
    }

    public void insertList(int listID, String titel) {
        ContentValues newline = new ContentValues();
        newline.put(COLUMN_LIST_ID, listID);
        newline.put(COLUMN_NAME, titel);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_LIST, null, newline);
    }

    public void insertGame(int gameID, String gameName, Float price, int rating, int listID, String uri) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(TABLE_GAME_ID, gameID);
        neueZeile.put(TABLE_GAME_NAME, gameName);
        neueZeile.put(COLUMN_PRICE, price);
        neueZeile.put(COLUMN_RATING, rating);
        neueZeile.put(COLUMN_LIST, listID);
        neueZeile.put(COLUMN_IMAGE_URI, uri);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_GAME, null, neueZeile);
    }

    public void insertGame(int gameID, String gameName, Float price, int rating, int listID) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(TABLE_GAME_ID, gameID);
        neueZeile.put(TABLE_GAME_NAME, gameName);
        neueZeile.put(COLUMN_PRICE, price);
        neueZeile.put(COLUMN_RATING, rating);
        neueZeile.put(COLUMN_LIST, listID);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_GAME, null, neueZeile);
    }


    public Cursor selectAllGamesFromList(int listID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAME + " WHERE " + COLUMN_LIST_ID + " = " + listID, null);
        cursor.moveToFirst();
        return cursor;
    }


    public Cursor selectAllLists() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LIST, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor selectListFromID(int listid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LIST + " WHERE " + COLUMN_LIST_ID + " = " + listid , null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteSelectedList(ListModel listModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_LIST_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(listModel.getId())};
        db.delete(TABLE_LIST, where, whereArg);
        //allLists.remove(listModel);
    }

    public void deleteAllGamesFromCurrentList(ListModel currentlist) {
        for (GameModel gameModel : currentlist.getGames()) {
            SQLiteDatabase db = this.getWritableDatabase();
            String where = COLUMN_LIST_ID + "=?";
            String[] whereArg = new String[]{Integer.toString(currentlist.getId())};
            db.delete(TABLE_GAME, where, whereArg);

        }
    }

    public void deleteGame(GameModel game) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = TABLE_GAME_ID + "=? AND " + COLUMN_LIST_ID + "=?";
        String[] whereArg = new String[]{String.valueOf(game.getId()), String.valueOf(game.getListID())};
        db.delete(TABLE_GAME, where, whereArg);
    }

    public void deleteGame(int gameID, int listID) {
        Log.d("HS_KL", "Currentlist inside delete method: " + listID);
        SQLiteDatabase db = this.getWritableDatabase();
        String where = TABLE_GAME_ID + "=? AND " + COLUMN_LIST_ID + "=?";
        String[] whereArg = new String[]{String.valueOf(gameID), String.valueOf(listID)};
        db.delete(TABLE_GAME, where, whereArg);
    }

    public void changeListIDs(int deletedID) {
        Cursor cursor = selectAllLists();
        SQLiteDatabase db = this.getWritableDatabase();

        do {
            int cursorid = cursor.getInt(cursor.getColumnIndexOrThrow("listeid"));

            if (cursorid > deletedID) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_LIST_ID, cursorid - 1);
                String where = COLUMN_LIST_ID + "=?";
                String[] whereArg = new String[]{Integer.toString(cursorid)};
                db.update(TABLE_LIST, values, where, whereArg);
                changeListIDInsideGamesTable(cursorid - 1);
            }
        }
        while (cursor.moveToNext());
    }

    public void changeListIDInsideGamesTable(int listID) {
        Cursor cursor = selectAllGamesFromList(listID + 1);
        SQLiteDatabase db = this.getWritableDatabase();

        do {
            ContentValues values = new ContentValues();
            values.put(COLUMN_LIST_ID, listID);
            String where = COLUMN_LIST_ID + "=?"; // Attribut game id unnötig, da alle Spiele der listid ausgewählt wurden und geupdated werden
            String[] whereArg = new String[]{Integer.toString(listID + 1)};
            db.update(TABLE_GAME, values, where, whereArg);

        }
        while (cursor.moveToNext());
    }

    public void changeGameID(int deletedGameID, int listID) {
        Cursor cursor = selectAllGamesFromList(listID);
        SQLiteDatabase db = this.getWritableDatabase();

        do {
            int cursorid = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_GAME_ID));
            if (cursorid > deletedGameID) {
                ContentValues values = new ContentValues();
                values.put(TABLE_GAME_ID, cursorid - 1);
                //String where = TABLE_GAME_ID + "=?";
                String where = TABLE_GAME_ID + "=? AND " + COLUMN_LIST_ID + "=?";
                String[] whereArg = new String[]{Integer.toString(cursorid), String.valueOf(listID)};
                db.update(TABLE_GAME, values, where, whereArg);
            }
        }
        while (cursor.moveToNext());
    }

    public void updateGame(int gameID, String gameName, Float price, int rating, String uri, int listid) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(TABLE_GAME_NAME, gameName);
        neueZeile.put(COLUMN_PRICE, price);
        neueZeile.put(COLUMN_RATING, rating);
        neueZeile.put(COLUMN_IMAGE_URI, uri);
        SQLiteDatabase db = this.getWritableDatabase();
        String where = TABLE_GAME_ID + "=? AND " + COLUMN_LIST_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(gameID), Integer.toString(listid)};
        db.update(TABLE_GAME, neueZeile, where, whereArg);
    }

    public void updateGame(int gameID, String gameName, Float price, int rating, int listid) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(TABLE_GAME_NAME, gameName);
        neueZeile.put(COLUMN_PRICE, price);
        neueZeile.put(COLUMN_RATING, rating);
        SQLiteDatabase db = this.getWritableDatabase();
        String where = TABLE_GAME_ID + "=? AND " + COLUMN_LIST_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(gameID), Integer.toString(listid)};
        db.update(TABLE_GAME, neueZeile, where, whereArg);
    }

    public void updateListName(int listID, String newListName) {
        ContentValues neueZeile = new ContentValues();
        neueZeile.put(COLUMN_NAME, newListName);
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_LIST_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(listID)};
        db.update(TABLE_LIST, neueZeile, where, whereArg);
    }
}