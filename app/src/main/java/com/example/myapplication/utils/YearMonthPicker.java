package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MonthPickerAdapter;
import com.example.myapplication.adapter.YearPickerAdapter;
import com.example.myapplication.db.MonthListBean;
import com.example.myapplication.db.TypeBean;
import com.example.myapplication.frag_record.TypeBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearMonthPicker extends Dialog implements View.OnClickListener{
    RecyclerView mRecyclerView;
    GridView mGridView;
    TextView mTV,yTV;
    Context context;
    int y,m;
    //适配器
    MonthPickerAdapter mAdapter;
    YearPickerAdapter yAdapter;
    //源数据
    List<MonthListBean> month_list = new ArrayList<>();
    List<String> year_list = new ArrayList<>();


    public YearMonthPicker(@NonNull Context context,int y,int m) {
        super(context);
        this.context=context;
        this.y=y;
        this.m=m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //设置年份
        setItemYear();
        //设置月份
        setItemMonth();
        //设置月份点击监听器
        setMonthListener();
        //设置年份点击监听器
        setYearListener();

    }

    /**
     * initView
     */
    private void initView() {
        setContentView(R.layout.dialog_monthpicker);//设置对话框显示布局
        mRecyclerView = findViewById(R.id.monthpicker_year);  //绑定对应的元素
        mGridView = findViewById(R.id.monthpicker_month);
        mTV = findViewById(R.id.month_selected);
        yTV = findViewById(R.id.year_selected);
    }

    /**
     * 设置年份
     */
    public void setItemYear() {
        //设置为横向滚动
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        //设置一行同时显示多个
        mRecyclerView.setPadding(310, 0, 310, 0);
        //false 将 View 的 padding 区域作为内容区域的一部分进行绘制
        mRecyclerView.setClipToPadding(false);
        // 创建一个ItemDecoration对象，并设置item之间的间隔大小
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int spacing = 0; // 设置item之间的间隔大小
                outRect.set(spacing, 20, spacing, 20);
            }
        };
        mRecyclerView.addItemDecoration(decoration);
        //制造数据
        year_list = new ArrayList<>();
        int i = -1;
        for (int year = 1900;year<=2099;year++){
            year_list.add(year+"");
            if (year==y){
                i=year_list.size()-1;
            }
        }
        //自定义的适配器
        yAdapter = new YearPickerAdapter(year_list);
        //设置默认焦点
        yAdapter.setSelectedPosition(i);
        //设置适配器
        mRecyclerView.setAdapter(yAdapter);
        //延迟执行
        mRecyclerView.post(() -> {
            //获取默认焦点位置
            int selectedPosition = yAdapter.getSelectedPosition();
            //设置显示在最中间的item
            layoutManager.scrollToPosition(selectedPosition);

        });
        yTV.setText(y+"");

    }

    /**
     * 设置月份数据
     */
    public void setItemMonth() {
        //获取数据
        month_list.add(new MonthListBean(0,"一月"));month_list.add(new MonthListBean(1,"二月"));month_list.add(new MonthListBean(2,"三月"));
        month_list.add(new MonthListBean(3,"四月")); month_list.add(new MonthListBean(4,"五月"));month_list.add(new MonthListBean(5,"六月"));
        month_list.add(new MonthListBean(6,"七月"));month_list.add(new MonthListBean(7,"八月"));month_list.add(new MonthListBean(8,"九月"));
        month_list.add(new MonthListBean(9,"十月"));month_list.add(new MonthListBean(10,"十一月"));month_list.add(new MonthListBean(11,"十二月"));

        mAdapter = new MonthPickerAdapter(month_list);
        //设置选中月份
        mAdapter.setSelectPos(m-1);
        mGridView.setAdapter(mAdapter);
        mTV.setText(m+"");
    }

    /**
     * 设置年份监听器
     */
    private void setYearListener() {
        yAdapter.setOnItemClickListener(new YearPickerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                yAdapter.setSelectedPosition(position);
                yTV.setText(year_list.get(position));
            }
        });
    }

    /**
     * 设置月份监听器
     */
    private void setMonthListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.setSelectPos(i);
                mAdapter.notifyDataSetChanged();//提示绘制发生变化
                mTV.setText(month_list.get(i).getI()+1+"");
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    public TextView getmTV() {
        return mTV;
    }

    public TextView getyTV() {
        return yTV;
    }
}
