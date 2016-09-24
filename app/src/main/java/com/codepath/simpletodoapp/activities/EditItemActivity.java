package com.codepath.simpletodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codepath.simpletodoapp.R;

public class EditItemActivity extends AppCompatActivity {
    int changeIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        changeIndex = getIntent().getIntExtra("position", -1);
        String initText = getIntent().getStringExtra("field_text");
        int curPos = initText.length();

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(initText);
        etEditItem.setSelection(curPos);
    }

    public void onAddItem(View v) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemText = etEditItem.getText().toString();
        if (!itemText.trim().isEmpty() && changeIndex >= 0) {
            Intent d = new Intent();
            d.putExtra("field_text", itemText.trim());
            d.putExtra("position", changeIndex);
            setResult(RESULT_OK, d);
        }
        finish();
    }

}
