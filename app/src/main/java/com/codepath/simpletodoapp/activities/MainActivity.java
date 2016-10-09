package com.codepath.simpletodoapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.codepath.simpletodoapp.R;
import com.codepath.simpletodoapp.adapters.ItemsAdapter;
import com.codepath.simpletodoapp.models.Item;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        EditEntryDialogFragment.EditEntryFragListener {
    ArrayList<Item> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;
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

    }

    @Override
    public void onSaveEditEntry(String itemText, Calendar itemDate,
                                String itemPriority, int listIndex) {
            //Toast.makeText(MainActivity.this, itemDate.get(Calendar.YEAR) +
            //         "/" +  itemDate.get(Calendar.MONTH) + "/" +
            //         itemDate.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
            writeItem(listIndex, itemText, itemDate, itemPriority);
            itemsAdapter.notifyDataSetChanged();
    }

    public void onAddItem(View v) {
        EditEntryDialogFragment newEntryFrag = EditEntryDialogFragment.
                 newInstance(null, Calendar.getInstance(), "MEDIUM", NEW_WRITE);
        newEntryFrag.show(getSupportFragmentManager(),"fragment_edit_entries");
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
                        EditEntryDialogFragment editEntryFrag;
                        if (items.size() > 0) {
                            editEntryFrag = EditEntryDialogFragment.
                                    newInstance(items.get(pos).itemName, items.get(pos).itemDueDate,
                                            items.get(pos).itemPriority, pos);
                            editEntryFrag.show(getSupportFragmentManager(),"fragment_edit_entries");
                        }
                    }
                });
    }

    private void readItems() {
        items = new ArrayList<>(Item.getAll());
    }

    private void writeItem(int index, String itemText, Calendar itemDate, String spinnerText) {
        Item dbItem;
        if (index == NEW_WRITE) {
            dbItem = new Item();
        } else {
            dbItem = items.get(index);
        }
        dbItem.itemName = itemText;
        if (itemDate != null) {
            dbItem.itemDueDate = itemDate;
        }
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
