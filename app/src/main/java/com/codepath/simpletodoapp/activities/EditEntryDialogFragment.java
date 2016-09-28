package com.codepath.simpletodoapp.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.simpletodoapp.R;


public class EditEntryDialogFragment extends DialogFragment {
    Button btnFragSave;
    EditText etFragEditText;


    public interface EditEntryFragListener {
        void onSaveEditEntry(String result, int index);
    }

    public EditEntryDialogFragment () {

    }

    public static EditEntryDialogFragment newInstance(String itemText, int index) {
        EditEntryDialogFragment itemFrag = new EditEntryDialogFragment();
        Bundle args = new Bundle();
        args.putString("itemText", itemText);
        args.putInt("index", index);
        itemFrag.setArguments(args);

        return itemFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_edit_entries, container, false);
        btnFragSave = (Button) dialogView.findViewById(R.id.btnFragSave);
        etFragEditText = (EditText) dialogView.findViewById(R.id.etFragEditItem);
        String initText = getArguments().getString("itemText","Enter Text");
        etFragEditText.setText(initText);
        etFragEditText.setSelection(initText.length());
        btnFragSave.setOnClickListener(saveOnclickListener);
        return dialogView;
    }

    private Button.OnClickListener saveOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String itemText = etFragEditText.getText().toString().trim();
            if (!itemText.isEmpty()) {
                EditEntryFragListener listener = (EditEntryFragListener) getActivity();
                listener.onSaveEditEntry(itemText, getArguments().getInt("index"));
                dismiss();
            }
        }
    };

    
}
