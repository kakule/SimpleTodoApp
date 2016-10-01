package com.codepath.simpletodoapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DateEntryFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener onDateSet;

    public DateEntryFragment() {}

    public static DateEntryFragment newInstance (Calendar initDate) {
        DateEntryFragment dateFrag = new DateEntryFragment();
        Bundle args = new Bundle();
        args.putSerializable("initDate", initDate);
        dateFrag.setArguments(args);

        return dateFrag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = (Calendar)getArguments().getSerializable("initDate");
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);

    }

    public void setDateFragCallBack(DatePickerDialog.OnDateSetListener ondate) {
        onDateSet = ondate;
    }
}