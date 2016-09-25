package com.codepath.simpletodoapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Items")
public class Item extends Model {
    @Column(name = "item_name")
    public String itemName;
    @Column(name = "item_priority")
    public String itemPriority;

    public Item() {
        super();
    }

    public Item(String name) {
        super();
        this.itemName = name;
        this.itemPriority = "MEDIUM";
    }

    public Item(String name, String priority) {
        super();
        this.itemName = name;
        this.itemPriority = priority;
    }

    public static List<Item> getAll() {
        return new Select().from(Item.class).execute();
    }
}
