package com.codepath.simpletodoapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

@Table(name = "Items")
public class Item extends Model {
    @Column(name = "item_name")
    public String itemName;
    @Column(name = "item_priority")
    public String itemPriority;
    @Column(name = "item_due_date")
    public Calendar itemDueDate;

    public Item() {
        super();
    }

    public Item(String name) {
        super();
        this.itemName = name;
        this.itemPriority = "MEDIUM";
        this.itemDueDate = Calendar.getInstance();
    }

    public Item(String name, String priority, Calendar dueDate) {
        super();
        this.itemName = name;
        this.itemPriority = priority;
        this.itemDueDate = dueDate;
    }

    public static List<Item> getAll() {
        return new Select().from(Item.class).execute();
    }
}
