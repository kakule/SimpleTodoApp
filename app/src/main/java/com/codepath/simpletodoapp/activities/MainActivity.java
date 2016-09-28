package com.codepath.simpletodoapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.codepath.simpletodoapp.R;
import com.codepath.simpletodoapp.adapters.ItemsAdapter;
import com.codepath.simpletodoapp.models.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        EditEntryDialogFragment.EditEntryFragListener{
    ArrayList<Item> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    Spinner spSpinnerItem;
    private final int REQUEST_CODE = 101;
    private final int DIAG_ID = 102;
    private int NEW_WRITE = -1;
    private Calendar entryDate = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        ActiveAndroid.initialize(this);
        readItems();
        itemsAdapter = new ItemsAdapter (this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        spSpinnerItem = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpinnerItem.setAdapter(spAdapter);
    }

    @Override
    public void onSaveEditEntry(String itemText, int listIndex) {
        if (listIndex >= 0) {
            items.get(listIndex).itemName = itemText;
            writeItem(listIndex, itemText, null);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Button dateButton = (Button) findViewById(R.id.btnDate);
        String spinnerSelection = (String) spSpinnerItem.getSelectedItem();
        if (!itemText.trim().isEmpty()) {
            writeItem(NEW_WRITE, itemText.trim(), spinnerSelection);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
            dateButton.setText("Set Due Date");

        }
    }

    public void onSetDate(View v) {
        showDialog(DIAG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIAG_ID) {
            return new DatePickerDialog(this, datepickerListener, entryDate.get(Calendar.YEAR),
                    entryDate.get(Calendar.MONTH), entryDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datepickerListener =
            new DatePickerDialog.OnDateSetListener() {
        @Override
            public void onDateSet (DatePicker view, int year, int month, int day) {
                Button dateButton = (Button) findViewById(R.id.btnDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                entryDate.set(Calendar.YEAR, year);
                entryDate.set(Calendar.MONTH, month + 1);
                entryDate.set(Calendar.DAY_OF_MONTH, day);
                //Toast.makeText(MainActivity.this, entryDate.get(Calendar.YEAR) +
                // "/" +  entryDate.get(Calendar.MONTH) + "/" +
                // entryDate.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
                dateButton.setText(dateFormat.format(entryDate.getTime()));
        }
    };

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        removeItem(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        EditEntryDialogFragment editEntryFrag;
                        if (items.size() > 0) {
                            //i.putExtra("position", pos);
                            editEntryFrag = EditEntryDialogFragment.
                                    newInstance(items.get(pos).itemName, pos);
                            editEntryFrag.show(getSupportFragmentManager(),"fragment_edit_entries");
                        }
                    }
                });
    }

    private void readItems() {
        items = new ArrayList<>(Item.getAll());
    }

    private void writeItem(int index, String itemText, String spinnerText) {
        Item dbItem;
        if (index == NEW_WRITE) {
            dbItem = new Item();
            dbItem.itemDueDate = entryDate;
        } else {
            dbItem = items.get(index);
        }
        dbItem.itemName = itemText;
        if (spinnerText != null) {
            dbItem.itemPriority = spinnerText;
        }
        dbItem.save();
        reloadItems();
    }

    private void removeItem(int index) {
        Item dbItem = Item.load(Item.class, items.get(index).getId());
        dbItem.delete();
        reloadItems();
    }

    private void reloadItems() {
        items.clear();
        items.addAll(Item.getAll());
    }
}
