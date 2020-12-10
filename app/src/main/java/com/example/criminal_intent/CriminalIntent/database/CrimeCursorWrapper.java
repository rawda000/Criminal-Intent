package com.example.criminal_intent.CriminalIntent.database;

import com.example.criminal_intent.CriminalIntent.database.CrimeDbSchema.CrimeTable;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.criminal_intent.CriminalIntent.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuid = getString(getColumnIndex(CrimeTable.cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.cols.SUSPECT));
        Crime crime = new Crime(UUID.fromString(uuid));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);
        return crime;
    }
}
