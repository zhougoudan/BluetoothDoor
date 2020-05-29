package com.zhiwailife.www.myapplication;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter extends ArrayAdapter<String> implements Serializable {
    public Adapter(@NonNull Context context, int resource, ArrayList<String> data) {
        super(context, resource);
    }
}
