package com.example.criminal_intent.CriminalIntent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.criminal_intent.R;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_Time = "time";
    private Date mDateParam;
    private TimePicker mTimePicker;
    public static final String EXTRA_TIME = "com.example.the_big_nerd.CriminalIntent.time";

    public TimePickerFragment() {

    }

    public static TimePickerFragment newInstance(Date date) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_Time, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDateParam = (Date) getArguments().getSerializable(ARG_Time);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_time_picker, null);
        mTimePicker = view.findViewById(R.id.time_picker);
        Date date = (Date) getArguments().getSerializable(ARG_Time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.crime_time)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int h = mTimePicker.getHour();
                        int min = mTimePicker.getMinute();
                        mDateParam.setHours(h);
                        mDateParam.setMinutes(min);
                        sendResult(Activity.RESULT_OK, mDateParam);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date time) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}