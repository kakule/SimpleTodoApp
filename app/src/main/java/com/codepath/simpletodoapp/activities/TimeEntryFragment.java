package com.codepath.simpletodoapp.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;


public class TimeEntryFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener onTimeSet;

    public TimeEntryFragment() {}

    public static TimeEntryFragment newInstance (Calendar initTime) {
        TimeEntryFragment timeFrag = new TimeEntryFragment();
        Bundle args = new Bundle();
        args.putSerializable("initTime", initTime);
        timeFrag.setArguments(args);

        return timeFrag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Calendar c = (Calendar)getArguments().getSerializable("initTime");
        if (c == null) {
            c = Calendar.getInstance();
        }
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), onTimeSet, hour, minute, false);

    }

    public void setTimeFragCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        onTimeSet = ontime;
    }
}
