package com.example.myapplication.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.myapplication.R;



public class CalendarDialog extends Dialog implements View.OnClickListener {
    DatePicker datePicker;
    TextView yes,no,time;
    OnEnsureListener onEnsureListener;
    int hour,min;

    public interface OnEnsureListener{
        public void onEnsure(String time,int year,int month,int day ,int dayForWeek);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public CalendarDialog(@NonNull Context context,int hour,int min) {
        super(context);
        this.hour=hour;
        this.min =min;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定布局
        setContentView(R.layout.dialog_calendar);
        //进行查找
        datePicker = findViewById(R.id.dialog_time_dp);
        yes = findViewById(R.id.dialog_calendar_btn_yes);
        no = findViewById(R.id.dialog_calendar_btn_no);
        time = findViewById(R.id.dialog_time);
        time.setText(hour+":"+min);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        time.setOnClickListener(this);
        hideDatePickerHeader();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_calendar_btn_no:
                cancel();
                break;
            case R.id.dialog_calendar_btn_yes:
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                int dayOfWeek = datePicker.getFirstDayOfWeek();
                String time1 = time.getText().toString().trim();
                if (onEnsureListener!=null){
                    onEnsureListener.onEnsure(time1,year,month,day,dayOfWeek);
                }
                cancel();
                break;
            case R.id.dialog_time:
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                // 处理用户选择的时间
                                time.setText(hour+":"+minute);
                            }
                        },
                        hour, // 设置默认小时
                        min, // 设置默认分钟
                        true // 设置是否使用24小时制
                );
                timePickerDialog.show();

        }
    }

    //隐藏datePicker头布局
    private void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView==null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView==null){
            return;
        }
        //5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {

            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }
        //6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }
}
