package com.zhiwailife.www.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;

public class LIstOfDeviceActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                LIstOfDeviceActivity.this,android.R.layout.simple_list_item_1,MainActivity.data);
        listView=findViewById(R.id.list_view_device);
        listView.setAdapter(adapter);

    }
}
