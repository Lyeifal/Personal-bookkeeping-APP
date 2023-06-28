package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.adapter.RecordPagerAdapter;
import com.example.myapplication.frag_record.IncomeFragment;
import com.example.myapplication.frag_record.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1.查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        //2.设置ViewPager加载页面
        initPager();
    }

    /**
     * 初始化ViewPager页面的集合
     */
    private void initPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        //创建收入和支出页面 放在Fragment中
        OutcomeFragment outFrag = new OutcomeFragment();//支出
        IncomeFragment inFrag = new IncomeFragment();//支出
        //接收参数
        Intent intent= getIntent();
        int id = intent.getIntExtra("account_id",0);
        //向Fragment传递参数
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        outFrag.setArguments(bundle);
        inFrag.setArguments(bundle);

        fragmentList.add(outFrag);
        fragmentList.add(inFrag);
        //创建适配器
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        //设置适配器
        viewPager.setAdapter(pagerAdapter);
        //将tabLayout和viewPager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}