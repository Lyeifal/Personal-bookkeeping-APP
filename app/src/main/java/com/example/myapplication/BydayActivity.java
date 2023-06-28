package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.adapter.BydayGVAdapter;
import com.example.myapplication.adapter.BydayLVAdapter;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.AccountListBean;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.utils.AccountInfoDialog;

import java.util.List;

public class BydayActivity extends AppCompatActivity implements View.OnClickListener{
    //主页
    ImageView byday_iv_back;
    TextView byday_month;
    GridView byday_gv;
    ListView byday_lv;
    Intent intent ;
    int year,month;
    //适配器
    BydayGVAdapter adapter_gv;
    BydayLVAdapter adapter_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_byday);
        //初始化视图
        initView();
        //给GV提供数据
        loadDataToItem();

    }
    @Override
    protected void onResume() {
        super.onResume();

        loadDataToItem();


    }

    private void initView() {
        byday_iv_back = findViewById(R.id.byday_iv_back);   //返回
        byday_month = findViewById(R.id.byday_month);
        byday_gv = findViewById(R.id.byday_gv);
        byday_lv = findViewById(R.id.byday_lv);
        intent= getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        //提供组件数据
        byday_month.setText(year+"-"+month);
        //设置点击事件
        byday_iv_back.setOnClickListener(this);  //返回首页
        byday_month.setOnClickListener(this);  //选择月份
//        byday_gv.setOnClickListener(this);  //返回首页
//        byday_lv.setOnClickListener(this);  //返回首页


    }

    /**
     * 给GridView和TextView填充数据的方法
     */
    public void loadDataToItem() {
        List<AccountListBean> mDatas = DBManager.getAccountListByMonth(year, month);

        adapter_gv = new BydayGVAdapter(mDatas);


        byday_gv.setAdapter(adapter_gv);

        byday_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<AccountBean> list1 = mDatas.get(i).getList();
                adapter_lv = new BydayLVAdapter(list1);
                byday_lv.setAdapter(adapter_lv);
                byday_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AccountInfoDialog dialog = new AccountInfoDialog(BydayActivity.this,list1.get(i));
                        dialog.setCanceledOnTouchOutside(true);

                        dialog.show();
                        dialog.setOnEnsureListener(new AccountInfoDialog.OnEnsureListener() {
                            @Override
                            public void onEnsure() {
                                loadDataToItem();
                                adapter_lv.clear();
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.byday_iv_back:
                finish();
                break;
            case R.id.byday_month:
                break;
        }
    }
}