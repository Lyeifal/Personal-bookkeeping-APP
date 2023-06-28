package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.db.MonthListBean;
import com.example.myapplication.db.TypeBean;

import java.util.List;

public class MonthPickerAdapter extends BaseAdapter {
    List<MonthListBean> mDatas;
    int selectPos = 0;//本次点击的位置

    public MonthPickerAdapter( List<MonthListBean> mDatas) {

        this.mDatas = mDatas;
    }

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return  mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_month,viewGroup,false);
        //查找布局中的控件
        TextView tv = view.findViewById(R.id.month_1);
        //获取指定位置的数据源
        String str = mDatas.get(i).getMonth();
        tv.setText(str);
        //判断当前位置是否为选中位置，如果时选中位置，就设置为带颜色图片
        if (selectPos == i) {
            tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.blue_03A9F4));
        }else{
            tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.grey_707070));
        }
        return view;
    }


}
