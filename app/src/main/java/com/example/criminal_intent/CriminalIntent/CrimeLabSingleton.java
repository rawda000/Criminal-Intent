package com.example.criminal_intent.CriminalIntent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminal_intent.CriminalIntent.database.CrimeBaseHelper;
import com.example.criminal_intent.CriminalIntent.database.CrimeCursorWrapper;
import com.example.criminal_intent.CriminalIntent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLabSingleton {
    private static CrimeLabSingleton sCrimeLabSingleton; //s before variable name for static variable
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLabSingleton(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLabSingleton getInstance(Context context) {
        if (sCrimeLabSingleton == null) {
            sCrimeLabSingleton = new CrimeLabSingleton(context);
        }
        return sCrimeLabSingleton;
    }

    private static ContentValues getContentValuesFromCrime(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.cols.UUID, crime.getID().toString());
        contentValues.put(CrimeTable.cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.cols.DATE, crime.getDate().toString());
        contentValues.put(CrimeTable.cols.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(CrimeTable.cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    public List<Crime> getCrimeList() {
        List<Crime> crimeList = new ArrayList<>();
        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null, null);
        try {
            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()) {
                crimeList.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimeList;
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValuesFromCrime(crime);
        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuid = crime.getID().toString();
        ContentValues contentValues = getContentValuesFromCrime(crime);
        mDatabase.update(CrimeTable.NAME, contentValues,
                CrimeTable.cols.UUID + "= ?",
                new String[]{uuid});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null,
                whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    public void deleteCrime(UUID id) {
        mDatabase.delete(CrimeTable.NAME, CrimeTable.cols.UUID + " =?", new String[]{id.toString()});
    }

    public Crime getCrime(UUID ID) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.cols.UUID + " =?", new String[]{ID.toString()});
        if (cursorWrapper.getCount() == 0) return null;
        try {
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }
}
