package com.example.myapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.AccountListBean;

import java.util.ArrayList;
import java.util.List;

public class OutterAccountAdapter extends BaseAdapter {
    private List<AccountListBean> mData;

    public OutterAccountAdapter(List<AccountListBean> mData) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainlv, parent, false);
        }

        // 设置内层 ListView 的数据
        List<AccountBean> innerData =mData.get(position).getList();
        // 声明适配器
        InnerAccountAdapter innerAdapter = new InnerAccountAdapter(innerData);
        //查找视图
        ListView innerListView = convertView.findViewById(R.id.item_mainlv_lv);

        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0,len = innerAdapter.getCount(); i < len; i++) {
            View listItem = innerAdapter.getView(i, null, innerListView);
            //用于测量视图的尺寸，包括宽度和高度的方法。
            //measure() 方法的输出结果将用于计算视图的布局。在测量完成后，measure() 方法将在视图中存储测量结果，以便在计算视图的布局时使用。
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = innerListView.getLayoutParams();

        params.height = totalHeight + (innerListView.getDividerHeight() *  (innerAdapter .getCount() - 1));

        innerListView.setLayoutParams(params);
        innerAdapter.notifyDataSetChanged();


        innerListView.setAdapter(innerAdapter);
        TextView recorded_time = convertView.findViewById(R.id.recorded_time);  //记录时间
        TextView total_money = convertView.findViewById(R.id.total_money);     //总支出收入
        //获取数据
        float count1 = 0,count2 =0;
        for (int i = 0;i<innerData.size();i++){
            if (innerData.get(i).getKind()==1){
                count1 += innerData.get(i).getMoney();
            }else if (innerData.get(i).getKind()==0){
                count2 += innerData.get(i).getMoney();
            }
        }
        String moneyOutIn = "";
        if (count1>0){
            moneyOutIn +="收入：+￥"+count1+"  ";
        }
        if (count2>0){
            moneyOutIn +="支出：-￥"+count2;
        }
        int month = innerData.get(0).getMonth();
        int day = innerData.get(0).getDay();
        int dayForWeek = innerData.get(0).getDayForWeek();

        String date = month+"."+day;
        //绑定数据

        recorded_time.setText(date);
        total_money.setText(moneyOutIn);

        return convertView;
    }

    //为listview动态设置高度（有多少条目就显示多少条目）
    public void setListViewHeight(ListView listView) {
        //获取listView的adapter


    }
}
