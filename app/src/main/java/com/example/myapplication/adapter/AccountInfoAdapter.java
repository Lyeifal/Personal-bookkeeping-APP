package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.db.AccountBean;

import java.util.List;

public class AccountInfoAdapter extends BaseAdapter {
    List<String[]> mData;

    public AccountInfoAdapter(List<String[]> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mainlvlv, viewGroup, false);
        }
        TextView tv1 = view.findViewById(R.id.tv_right);
        TextView tv2 = view.findViewById(R.id.tv_left);


        tv1.setText(mData.get(i)[0]);
        tv2.setText(mData.get(i)[1]);

        return view;
    }
}
