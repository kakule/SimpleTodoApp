package com.codepath.simpletodoapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codepath.simpletodoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.widget.AdapterView.OnItemSelectedListener;


public class EditEntryDialogFragment extends DialogFragment {
    Button btnFragSave;
    Button btnFragDate;
    Button btnFragTime;
    TextView tvFragHead;
    EditText etFragEditText;
    Spinner spFragPriority;
    ArrayAdapter<CharSequence> spAdapter;
    Calendar c = null;


    public interface EditEntryFragListener {
        void onSaveEditEntry(String resultText, Calendar resultDate, String priority, int index);
    }

    public EditEntryDialogFragment () {

    }

    public static EditEntryDialogFragment newInstance(String itemText, Calendar itemDate,
                                                      String itemPriority, int index) {
        EditEntryDialogFragment itemFrag = new EditEntryDialogFragment();
        Bundle args = new Bundle();
        args.putString("itemText", itemText);
        args.putSerializable("itemDate", itemDate);
        args.putString("itemPriority", itemPriority);
        args.putInt("index", index);
        itemFrag.setArguments(args);

        return itemFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View dialogView = inflater.inflate(R.layout.fragment_edit_entries, container, false);
        btnFragSave = (Button) dialogView.findViewById(R.id.btnFragSave);
        btnFragDate = (Button) dialogView.findViewById(R.id.btnFragDate);
        btnFragTime = (Button) dialogView.findViewById(R.id.btnFragTime);
        tvFragHead  = (TextView) dialogView.findViewById(R.id.txtFragMsg);
        spFragPriority = (Spinner) dialogView.findViewById(R.id.spFragPriority);
        etFragEditText = (EditText) dialogView.findViewById(R.id.etFragEditItem);
        c = (Calendar) getArguments().getSerializable("itemDate");
        String initText = getArguments().getString("itemText", "");
        String spText = getArguments().getString("itemPriority", "");
        if (initText.isEmpty()) {
            tvFragHead.setText(R.string.add_item);
            etFragEditText.setHint(R.string.new_hint);
            btnFragDate.setText(R.string.add_date);
            btnFragTime.setText(R.string.add_time);
            btnFragTime.setEnabled(false);
        } else {
            tvFragHead.setText(R.string.edit_item);
            etFragEditText.setText(initText);
            btnFragDate.setText(R.string.edit_date);
            btnFragTime.setText(R.string.edit_time);
        }
        etFragEditText.setSelection(initText.length());
        etFragEditText.setOnFocusChangeListener(focusListener);
        btnFragDate.setOnClickListener(dateOnclickListener);
        btnFragTime.setOnClickListener(timeOnclickListener);
        btnFragSave.setOnClickListener(saveOnclickListener);
        spAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.priorities, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFragPriority.setAdapter(spAdapter);
        if (!spText.isEmpty()) {
            int position = spAdapter.getPosition(spText);
            spFragPriority.setSelection(position);
        }
        setPriorityListener();
        return dialogView;
    }

    private Button.OnClickListener saveOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String itemText = etFragEditText.getText().toString().trim();
            if (!itemText.isEmpty()) {
                String spSelect = (String) spFragPriority.getSelectedItem();
                EditEntryFragListener listener = (EditEntryFragListener) getActivity();
                listener.onSaveEditEntry(itemText, c, spSelect, getArguments().getInt("index"));
                dismiss();
            }
        }
    };

    private Button.OnClickListener dateOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateEntryFragment newFragment = DateEntryFragment.newInstance((Calendar) getArguments().getSerializable("itemDate"));
            newFragment.setDateFragCallBack(newdate);
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

        }
    };

     DatePickerDialog.OnDateSetListener newdate = new DatePickerDialog.OnDateSetListener() {
         @Override
         public void onDateSet (DatePicker view, int year, int month, int dayOfMonth) {
             // store the values selected into a Calendar instance
             c = Calendar.getInstance();
             c.set(Calendar.YEAR, year);
             c.set(Calendar.MONTH, month);
             c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
             SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
             btnFragDate.setText(dateFormat.format(c.getTime()));
             btnFragTime.setEnabled(true);
         }
    };

    private Button.OnClickListener timeOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimeEntryFragment newFragment =
                    TimeEntryFragment.newInstance((Calendar)getArguments().getSerializable("itemDate"));
            newFragment.setTimeFragCallBack(newtime);
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");

        }
    };

    TimePickerDialog.OnTimeSetListener newtime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet (TimePicker view, int hour, int minute) {
            // store the values selected into a Calendar instance
            c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            btnFragTime.setText(dateFormat.format(c.getTime()));
        }
    };

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();

    }

    private void setPriorityListener() {

        spFragPriority.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(
                                getColor((String) parent.getItemAtPosition(position)));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });

    }

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d("simpletodo", "onFocusChange called");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus) {
                Log.d("simpletodo", "hasFocus entered");
                imm.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);
            } else {
                Log.d("simpletodo", "does not has Focus entered");
                imm.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);
            }
        }
        };

    private int getColor(String priority) {
        int spColor = Color.YELLOW;

        if (priority.equals("LOW")) {
            spColor = Color.GREEN;
        } else if (priority.equals("HIGH")){
            spColor = Color.RED;
        }
        return spColor;
    }

    
}
