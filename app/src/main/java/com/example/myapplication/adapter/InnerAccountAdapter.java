package com.example.myapplication.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.db.AccountBean;

import java.util.List;

public class InnerAccountAdapter extends BaseAdapter {

    List<AccountBean> mData;

    public InnerAccountAdapter(List<AccountBean> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainlvlv, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tv_right);
        TextView textView2 = convertView.findViewById(R.id.tv_left);
        // 设置内层 ListView 的数据
        String Typename = mData.get(position).getTypename();

        int kind = mData.get(position).getKind();
        String money ="";
        if (kind==1){
            money= "+￥"+mData.get(position).getMoney();
            textView2.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.green_4CAF50));
        } else if (kind==0) {
            money = "-￥"+mData.get(position).getMoney();
            textView2.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.red_BE262F));
        }

        textView.setText(Typename);
        textView2.setText(money);
        return convertView;
    }
}

