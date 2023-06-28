package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

public class Timedialog extends Dialog implements View.OnClickListener {
    TimePicker timePicker;
    public Timedialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View view) {

    }
}
