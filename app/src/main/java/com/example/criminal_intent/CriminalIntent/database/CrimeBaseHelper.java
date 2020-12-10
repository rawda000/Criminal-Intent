package com.example.criminal_intent.CriminalIntent.database;

import com.example.criminal_intent.CriminalIntent.database.CrimeDbSchema.CrimeTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CrimeTable.NAME
                + "( _id integer primary key autoincrement, "
                + CrimeTable.cols.UUID
                + ", " + CrimeTable.cols.TITLE
                + ", " + CrimeTable.cols.DATE
                + ", " + CrimeTable.cols.SOLVED
                + ", " + CrimeTable.cols.SUSPECT
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
