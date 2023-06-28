package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db", null, 1);
    }

    //创建数据库的方法，只有项目第一次运行时，会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表示类型的表
        String sql = "create table typetb" +
                "(id integer primary key autoincrement," +
                "typename varchar(10)," +
                "imageId integer," +
                "sImageId integer," +
                "kind integer)";
        db.execSQL(sql);
        //插入类型数据
        insertType(db);
        //创建记账表
        sql = "create table accounttb " +
                "(id integer primary key autoincrement," +
                "typename varchar(10)," +
                "sImageId integer," +
                "remark varchar(80)," +
                "money float," +
                "time varchar(20)," +
                "saveTime varchar(20)," +
                "year integer," +
                "month integer," +
                "day integer," +
                "dayForWeek integer," +
                "kind integer," +
                "userId varchar(20)," +
                "bookId varchar(20))";
        db.execSQL(sql);
        //创建月预算表
        sql = "create table yusuantb " +
                "(id integer primary key autoincrement," +
                "year integer," +
                "month integer," +
                "money float," +
                "userId varchar(20)," +
                "bookId varchar(20))";
        db.execSQL(sql);


    }

    private void insertType(SQLiteDatabase db) {
        //向type表中插入元素
        String sql ="insert into typetb (typename,imageId,sImageId,kind) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"三餐", R.mipmap.food,R.mipmap.food_fs,0});
        db.execSQL(sql,new Object[]{"零食",R.mipmap.lingshi,R.mipmap.lingshi_fs,0});
        db.execSQL(sql,new Object[]{"衣服",R.mipmap.yifu,R.mipmap.yifu_fs,0});
        db.execSQL(sql,new Object[]{"交通",R.mipmap.jiaotong,R.mipmap.jiaotong_fs,0});
        db.execSQL(sql,new Object[]{"旅行",R.mipmap.lvxing,R.mipmap.lvxing_fs,0});
        db.execSQL(sql,new Object[]{"孩子",R.mipmap.haizi,R.mipmap.haizi_fs,0});
        db.execSQL(sql,new Object[]{"宠物",R.mipmap.chongwu,R.mipmap.chongwu_fs,0});
        db.execSQL(sql,new Object[]{"话费网费",R.mipmap.huafei,R.mipmap.huafei_fs,0});
        db.execSQL(sql,new Object[]{"烟酒",R.mipmap.yanjiu,R.mipmap.yanjiu_fs,0});
        db.execSQL(sql,new Object[]{"学习",R.mipmap.xuexi,R.mipmap.xuexi_fs,0});
        db.execSQL(sql,new Object[]{"日用品",R.mipmap.riyongpin,R.mipmap.riyongpin_fs,0});
        db.execSQL(sql,new Object[]{"住房",R.mipmap.zhufang,R.mipmap.zhufang_fs,0});
        db.execSQL(sql,new Object[]{"美妆",R.mipmap.meizhuang,R.mipmap.meizhuang_fs,0});
        db.execSQL(sql,new Object[]{"医疗",R.mipmap.yiliao,R.mipmap.yiliao_fs,0});
        db.execSQL(sql,new Object[]{"发红包",R.mipmap.hongbao,R.mipmap.hongbao_fs,0});
        db.execSQL(sql,new Object[]{"汽车/加油",R.mipmap.qichejiayou,R.mipmap.qichejiayou_fs,0});
        db.execSQL(sql,new Object[]{"娱乐",R.mipmap.yule,R.mipmap.yule_fs,0});
        db.execSQL(sql,new Object[]{"音像数码",R.mipmap.yinxiangshuma,R.mipmap.yinxiangshuma_fs,0});
        db.execSQL(sql,new Object[]{"水电煤",R.mipmap.shuidianmei,R.mipmap.shuidianmei_fs,0});
        db.execSQL(sql,new Object[]{"其他",R.mipmap.qita,R.mipmap.qita_fs,0});

        db.execSQL(sql,new Object[]{"工资",R.mipmap.gongzi,R.mipmap.gongzi_fs,1});
        db.execSQL(sql,new Object[]{"生活费",R.mipmap.shenghuofei,R.mipmap.shenghuofei_fs,1});
        db.execSQL(sql,new Object[]{"收红包",R.mipmap.hongbao,R.mipmap.hongbao_fs,1});
        db.execSQL(sql,new Object[]{"外快",R.mipmap.waikuai,R.mipmap.waikuai_fs,1});
        db.execSQL(sql,new Object[]{"股票基金",R.mipmap.gupiaojijin,R.mipmap.gupiaojijin_fs,1});
        db.execSQL(sql,new Object[]{"其他",R.mipmap.qita,R.mipmap.qita_fs,1});
    }

    //数据库版本在更新时发生改变，会调用此方法
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
