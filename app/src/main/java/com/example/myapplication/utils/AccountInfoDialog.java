package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.BydayActivity;
import com.example.myapplication.R;
import com.example.myapplication.RecordActivity;
import com.example.myapplication.adapter.AccountInfoAdapter;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.DBManager;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoDialog extends Dialog implements View.OnClickListener{
    //listview
    ListView lv;
    //item
    View headView;
    TextView title,edit,del;
    AccountBean accountBean;
    List<String[]> mData;
    int id;
    OnEnsureListener onEnsureListener;

    public AccountInfoDialog(@NonNull Context context, AccountBean accountBean) {
        super(context);
        this.accountBean = accountBean;
        this.id = accountBean.getId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_account); //设置对话框显示布局
        lv = findViewById(R.id.dialog_account_lv);
        touming();
        addLVHeaderView();
        mData = new ArrayList<>();
        loadDBdata();

    }

    /**
     * 填充数据
     */
    private void loadDBdata(){
        mData.clear();
        mData.addAll(setList());
        AccountInfoAdapter adapter = new AccountInfoAdapter(mData);
        lv.setAdapter(adapter);
    }

    /**
     * 设置元数据
     */
    private List<String[]> setList(){
        List<String[]> list = new ArrayList<String[]>();
        String[] arr  = {"金额",accountBean.getMoney()+""};
        String[] arr1 = {"分类",accountBean.getTypename()+""};
        String[] arr2 = {"时间",accountBean.getTime()+""};
        String[] arr3 = {"备注",accountBean.getRemark()+""};

        list.add(arr);
        list.add(arr1);
        list.add(arr2);
        list.add(arr3);

        return list;
    }
    /**
     * 给listView添加头布局
     */
    private void addLVHeaderView() {
        //将布局转换成view
        headView = getLayoutInflater().inflate(R.layout.item_account_top,null);
        //设置头布局
        lv.addHeaderView(headView);
        //查找头布局中需要的控件
        title = headView.findViewById(R.id.string2);
        edit = headView.findViewById(R.id.item_account_edit);
        del = findViewById(R.id.item_account_del);
        //设置点击事件
        edit.setOnClickListener(this);//
        del.setOnClickListener(this);//


    }
    public interface OnEnsureListener{
        public void onEnsure();
    }
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_account_edit:
                Intent it1 = new Intent(view.getContext(), RecordActivity.class);//跳转页面
                it1.putExtra("account_id", id);
                view.getContext().startActivity(it1);
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
            case R.id.item_account_del:
                DBManager.delAccountInfo(id);
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    /**
     * 设置背景为透明
     */
    private void touming(){
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM); // 将Dialog显示在屏幕底部
    }
}
