package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.AccountListBean;

import java.util.List;

public class BydayLVAdapter extends BaseAdapter {
    List<AccountBean> mData;

    public BydayLVAdapter(List<AccountBean> mData) {
        this.mData = mData;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mainlvlv, viewGroup, false);
        }

        TextView tv1 = view.findViewById(R.id.tv_right);
        TextView tv2 = view.findViewById(R.id.tv_left);

        //定义显示样式
        String money = "";
        int kind = mData.get(i).getKind();
        if (kind==1){
            money= "+￥"+mData.get(i).getMoney();
            tv2.setTextColor(ContextCompat.getColor(view.getContext(), R.color.green_4CAF50));
        } else if (kind==0) {
            money = "-￥"+mData.get(i).getMoney();
            tv2.setTextColor(ContextCompat.getColor(view.getContext(), R.color.red_BE262F));
        }
        //给组件提供数据
        tv1.setText(mData.get(i).getTypename());
        tv2.setText(money);

        return view;
    }
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }
}
