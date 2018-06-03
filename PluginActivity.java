package com.example.plugindemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by shanghai on 2018/6/3.
 */

public class PluginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.text);
        text.setText("PluginActivity");
    }
}
