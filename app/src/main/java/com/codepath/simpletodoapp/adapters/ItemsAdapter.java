package com.codepath.simpletodoapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.simpletodoapp.R;
import com.codepath.simpletodoapp.models.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemsAdapter extends ArrayAdapter<Item> {
    public ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvListItemName);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        ImageView ivPriorityColor = (ImageView) convertView.findViewById(R.id.ivPriorityColor);
        // Populate the data into the template view using the data object
        Date itemDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        tvName.setText(item.itemName);
        tvDueDate.setText("Due: " + dateFormat.format(item.itemDueDate.getTime()));
        ivPriorityColor.setBackgroundColor(setColor(item.itemPriority));
        // Return the completed view to render on screen
        return convertView;
    }

    private int setColor(String priority) {
        int bgColor;
        if (priority.equals("LOW")){
            bgColor = Color.GREEN;
        }
        else if (priority.equals("HIGH")){
            bgColor = Color.RED;
        }
        else {
            bgColor = Color.YELLOW;
        }
        return bgColor;
    }
}
