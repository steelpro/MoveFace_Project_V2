package com.example.issorrossi.accelerometer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by issoRRossi on 4/30/2017.
 */

public class MyDBHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "highscores.db";
    public static final String TABLE_NAME = "scores_table";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SCORE = "SCORE";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //CREATE DATABASE WITH ID, NAME, AND SCORE (ID IS AUTO)
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SCORE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addScore(HighScore highscore){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, highscore.getName()); //SEND VALUES FROM HIGHSCORE CLASS
        values.put(COLUMN_SCORE, highscore.getScore());

        long result = db.insert(TABLE_NAME, null, values);

        if (result == -1) //IF FAILS
            return false;
        else
            return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = getWritableDatabase(); //IMPORT RECORDS AND SORT BY SCORE THAN BY NAME
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY "
                + COLUMN_SCORE + ", " + COLUMN_NAME + " DESC", null);
        return result;
    }
}