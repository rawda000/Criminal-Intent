package com.example.criminal_intent.CriminalIntent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID ID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiresPolice;
    private String mSuspect;
    public Crime() {
        ID = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(UUID ID) {
        this.ID = ID;
        mDate = new Date();
    }

    public UUID getID() {
        return ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getSuspect() {
        return mSuspect;
    }
}
