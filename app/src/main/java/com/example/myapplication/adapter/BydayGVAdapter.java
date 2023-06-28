package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.db.AccountListBean;

import java.util.List;

public class BydayGVAdapter extends BaseAdapter{
    List<AccountListBean> mData;
//    private final View.OnClickListener listener;
    int selectPos = 0;//本次点击的位置

    public BydayGVAdapter( List<AccountListBean> mData) {
        this.mData = mData;
//        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_byday_gv, viewGroup, false);
        }
        TextView tv1 = view.findViewById(R.id.item_byday_tv1);
        TextView tv2 = view.findViewById(R.id.item_byday_tv2);
        TextView tv3 = view.findViewById(R.id.item_byday_tv3);

        tv1.setText(mData.get(i).getDay()+"");
        tv2.setText(mData.get(i).getPayCount()+"");
        tv3.setText(mData.get(i).getInComeCount()+"");

//        tv1.setOnClickListener(listener);

        return view;
    }
}
