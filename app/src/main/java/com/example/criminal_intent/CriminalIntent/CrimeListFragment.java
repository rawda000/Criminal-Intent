package com.example.criminal_intent.CriminalIntent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminal_intent.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private TextView mEmptyMessage;
    private CrimeAdapter mAdapter;
    private static final String EXTRA_CRIME_ID = "com.example.the_big_nerd.CriminalIntent.crime_ID";
    private int mIndex = -1;
    private boolean mSubtitleVisible;
    private Button mAddButton;
    private static final String SUBTITLE_VISIBLE = "subtitle_visible";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle_menu);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_menu) {
            addCrime();
            return true;
        } else if (id == R.id.show_subtitle_menu) {
            mSubtitleVisible = !mSubtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        return false;
    }

    private void updateSubtitle() {
        int size = CrimeLabSingleton.getInstance(getActivity()).getCrimeList().size();
        String subtitle = getResources().getQuantityString(R.plurals.plural_subtitle, size, size);
        if (!mSubtitleVisible) subtitle = null;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    private void addCrime() {
        Crime crime = new Crime();
        CrimeLabSingleton.getInstance(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID());
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE);
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mEmptyMessage = view.findViewById(R.id.empty_crime_message);
        mAddButton = view.findViewById(R.id.add_crime);
        mAddButton.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.fragment_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }


    private void updateUI() {
        CrimeLabSingleton crimeLabSingleton = CrimeLabSingleton.getInstance(getActivity());
        List<Crime> crimeList = crimeLabSingleton.getCrimeList();
        if (crimeList.size() == 0) {
            mEmptyMessage.setVisibility(View.VISIBLE);
            mAddButton.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            return;
        } else {
            mEmptyMessage.setVisibility(View.INVISIBLE);
            mAddButton.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimeList);
            mIndex = -1;
            mRecyclerView.setAdapter(mAdapter);
        } else {
        //    logCrimes(crimeList);
            mAdapter.setCrimes(crimeList);
            // mAdapter.notifyItemChanged(mIndex);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private void logCrimes(List<Crime> crimeList) {
        for (Crime crime : crimeList) {
            Log.i("Crimes", crime.getTitle());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        setHasOptionsMenu(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_crime) {
            if (v.getVisibility() == View.VISIBLE)
                addCrime();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;

        public CrimeHolder(@NonNull LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleTextView = itemView.findViewById(R.id.crime_title_item);
            mDateTextView = itemView.findViewById(R.id.crime_date_item);
            mSolvedImageView = itemView.findViewById(R.id.crimeSolved_imageView);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
            String formattedDate = formatter.format(crime.getDate());
            mDateTextView.setText(formattedDate);
            boolean crimeSolved = crime.isSolved();
            mSolvedImageView.setVisibility(crimeSolved ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            // Toast.makeText(getActivity(), "You clicked " + mTitleTextView.getText(), Toast.LENGTH_SHORT).show();
            //Intent intent = newIntent(getActivity(), mCrime.getID());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID());
            startActivity(intent);
            mIndex = getAdapterPosition();
        }
    }

    private class SeriousCrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mButton;
        private ImageView mSolvedImageView;
        private Crime mCrime;

        public SeriousCrimeHolder(@NonNull LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_serious_crime, parent, false));
            mTitleTextView = itemView.findViewById(R.id.crime_title_item);
            mDateTextView = itemView.findViewById(R.id.crime_date_item);
            mButton = itemView.findViewById(R.id.report_button);
            mSolvedImageView = itemView.findViewById(R.id.crimeSolved_imageView2);
            itemView.setOnClickListener(this);
            mButton.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mTitleTextView.setText(crime.getTitle());
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
            String formattedDate = formatter.format(crime.getDate());
            mDateTextView.setText(formattedDate);
            //mDateTextView.setText(crime.getDate().toString());
            boolean crimeSolved = crime.isSolved();
            mSolvedImageView.setVisibility(crimeSolved ? View.VISIBLE : View.GONE);
            mCrime = crime;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.serious_crime_rl) {
                /*Intent intent = newIntent(getActivity(), mCrime.getID());
                startActivity(intent);*/
                Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID());
                startActivity(intent);

            } else if (v.getId() == R.id.report_button) {
                Toast.makeText(getContext(), "You clicked Report Crime", Toast.LENGTH_SHORT).show();
            }
            mIndex = getAdapterPosition();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            switch (viewType) {
                case 0:
                    return new CrimeHolder(inflater, parent);
                case 1:
                    return new SeriousCrimeHolder(inflater, parent);
                default:
                    return null;
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (getItemViewType(position) == 0) {
                ((CrimeHolder) holder).bind(crime);
            } else if (getItemViewType(position) == 1) {
                ((SeriousCrimeHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimes.get(position).isRequiresPolice()) return 1;
            return 0;
        }
    }
}
