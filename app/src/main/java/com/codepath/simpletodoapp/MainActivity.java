package com.codepath.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    List<Item> todolist;
    ListView lvItems;
    private final int REQUEST_CODE = 101;
    private int NEW_WRITE = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        ActiveAndroid.initialize(this);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemText = data.getExtras().getString("field_text");
            int pos = data.getExtras().getInt("position", -1);
            if (pos >= 0) {
                items.set(pos, itemText);
                itemsAdapter.notifyDataSetChanged();
                writeItem(pos, itemText);
            }

        }
    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (!itemText.trim().isEmpty()) {
            itemsAdapter.add(itemText.trim());
            etNewItem.setText("");
            writeItem(NEW_WRITE, itemText.trim());
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        removeItem(pos);
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
                            i.putExtra("field_text", items.get(pos));
                            startActivityForResult(i, REQUEST_CODE);
                        }
                    }
                });
    }

    private void readItems() {
        todolist = Item.getAll();
        for (int i = 0; i < todolist.size(); i++) {
            Item entry = todolist.get(i);
            items.add(entry.itemName);
        }
    }

    private void writeItem(int index, String itemText) {
        Item dbItem;
        if (index == NEW_WRITE) {
            dbItem = new Item();
        } else {
            dbItem = todolist.get(index);
        }
        dbItem.itemName = itemText;
        dbItem.save();
        todolist = Item.getAll();
    }

    private void removeItem(int index) {
        Item dbItem = Item.load(Item.class, todolist.get(index).getId());
        dbItem.delete();
        todolist = Item.getAll();
    }

}
