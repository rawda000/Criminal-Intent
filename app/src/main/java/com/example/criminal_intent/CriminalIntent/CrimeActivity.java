package com.example.criminal_intent.CriminalIntent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity{
    private static final String EXTRA_CRIME_ID = "com.example.the_big_nerd.CriminalIntent.crime_ID";
    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(uuid);
    }
}