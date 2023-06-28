package com.example.myapplication.frag_record;


import android.os.Bundle;

import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.TypeBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OutcomeFragment  extends BaseRecordFragment{

    //重载重写加载GV的方法
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveAccountToDB() {
        Date date = new Date();
        //设置时间戳格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        //把时间转换成设置好的格式的String字符串
        String time = sdf.format(date);

        accountBean.setSaveTime(time);
        accountBean.setKind(0);

        Bundle bundle = getArguments();
        int id = bundle.getInt("id",0);
        if (id == 0){
            DBManager.insertItemToTable(accountBean,"accounttb");
        }else{
            accountBean.setId(id);
            DBManager.editAccountInfo(accountBean);
        }

    }
}
