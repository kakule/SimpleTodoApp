package com.codepath.simpletodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.codepath.simpletodoapp.R;
import com.codepath.simpletodoapp.adapters.ItemsAdapter;
import com.codepath.simpletodoapp.models.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    Spinner spSpinnerItem;
    private final int REQUEST_CODE = 101;
    private int NEW_WRITE = -1;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemText = data.getExtras().getString("field_text");
            int pos = data.getExtras().getInt("position", -1);
            if (pos >= 0) {
                items.get(pos).itemName = itemText;
                writeItem(pos, itemText, null);
                itemsAdapter.notifyDataSetChanged();
            }

        }
    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        String spinnerSelection = (String) spSpinnerItem.getSelectedItem();
        if (!itemText.trim().isEmpty()) {
            writeItem(NEW_WRITE, itemText.trim(), spinnerSelection);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
        }
    }

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
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        if (items.size() > 0) {
                            i.putExtra("position", pos);
                            i.putExtra("field_text", items.get(pos).itemName);
                            startActivityForResult(i, REQUEST_CODE);
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
