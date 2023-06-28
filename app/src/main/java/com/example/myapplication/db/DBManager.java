package com.example.myapplication.db;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 负责管理数据库的类
 * 主要对于表当中的内容进行操作，增删改查
 */
public class DBManager {
    private static SQLiteDatabase db;
    private static String userId;
    private static String bookId;
    private static Context context1;
    //初始化数据库对象
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);//得到帮助类对象
        db = helper.getWritableDatabase(); //得到数据库对象
        context1 = context;
        initUserID();

        bookId = context.getString(R.string.book_id);
    }

    private static void initUserID() {
        //获取是否登录
        SharedPreferences sharedPreferences= context1.getSharedPreferences("user", MODE_PRIVATE);
        userId=sharedPreferences.getString("user_id","001");
    }

    /**
     * 通过反射获取一个对象的字段和值 整理成一个字段-值的集合，方便插入和更新表
     */
    public static ContentValues setValues(Object bean){
        //ContentValues 和 HashTable 类似，都是一种存储的机制，
        // 但是两者最大的区别就在于：ContentValues 只能存储基本类型的数据，
        // 像string、int之类的，不能存储对象这种东西，而HashTable却可以存储对象。
        ContentValues values = new ContentValues();
        // 把AccountBean值放入ContentValues中
        try{
            //通过getDeclaredFields()方法获取对象类中的所有属性（含私有）
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                //设置允许通过反射访问私有变量
                field.setAccessible(true);
                //获取字段的值
                String value = field.get(bean).toString();
                //获取字段属性名称
                String name = field.getName();
                //其他自定义操作
                if (name.equals("id")||name.equals("user_id")){
                    continue;
                }
                values.put(name,value);
            }
        }
        catch (Exception ex){
            //处理异常
        }
        return values;
    }
    /**
     * 向表中 插入一条元素
     */
    public static void insertItemToTable(Object bean,String tableName){
        ContentValues values = setValues(bean);
        db.insert(tableName,null,values);

    }
    /**
     * 更新预算设置 逻辑操作
     * @param yusuanBean
     */
    public static void UpdateYsuan(YusuanBean yusuanBean) {
        initUserID();
        YusuanBean yusuanByMonth = getYusuanByMonth(yusuanBean.year, yusuanBean.month);
        if (yusuanByMonth!=null){
            updateYusuanTable(yusuanBean,"yusuantb");
        }else {
            insertItemToTable(yusuanBean,"yusuantb");
        }

    }

    /**
     * 读取数据库当中的数据，写入内存集合里
     * kind表示收入或支出  收入为1 支出为0
     */
    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean> list = new ArrayList<>();
        //读取typetb表当中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);
        //循环读取游标内容 ，存储到对象当中
        while (cursor.moveToNext()){
            int id1 = cursor.getColumnIndex("id");
            int typename1 = cursor.getColumnIndex("typename");
            int imageId1 = cursor.getColumnIndex("imageId");
            int sImageId1 = cursor.getColumnIndex("sImageId");

            int id = cursor.getInt(id1);
            String typename = cursor.getString(typename1);
            int imageId = cursor.getInt(imageId1);
            int sImageId = cursor.getInt(sImageId1);
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        cursor.close();

        return list;
    }
    /**
     * 获取记账表中当月所有数据  按天来
     */
    public static List<AccountListBean> getAccountListByMonth(int year,int month){
        initUserID();
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year = ? and month = ?  and userId = ? and bookId = ? order by 0+time desc";

        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "",userId,bookId});
        while (cursor.moveToNext()){
            int id = cursor.getInt((Integer)cursor.getColumnIndex("id"));
            String typename = cursor.getString((Integer)cursor.getColumnIndex("typename"));
            int sImageId = cursor.getInt((Integer)cursor.getColumnIndex("sImageId"));
            String remark = cursor.getString((Integer)cursor.getColumnIndex("remark"));
            float money = cursor.getFloat((Integer)cursor.getColumnIndex("money"));
            String time = cursor.getString((Integer)cursor.getColumnIndex("time"));
            String saveTime = cursor.getString((Integer)cursor.getColumnIndex("saveTime"));
            int year1 = cursor.getInt((Integer)cursor.getColumnIndex("year"));
            int month1 = cursor.getInt((Integer)cursor.getColumnIndex("month"));
            int day = cursor.getInt((Integer)cursor.getColumnIndex("day"));
            int dayForWeek = cursor.getInt((Integer)cursor.getColumnIndex("dayForWeek"));
            int kind = cursor.getInt((Integer)cursor.getColumnIndex("kind"));
            String userId = cursor.getString((Integer)cursor.getColumnIndex("userId"));
            String bookId = cursor.getString((Integer)cursor.getColumnIndex("bookId"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remark, money, time, saveTime, year1, month1, day, dayForWeek, kind,userId,bookId);
            list.add(accountBean);
        }

        Map<Integer,List<AccountBean>> listByDay= list.stream().collect(Collectors.groupingBy(AccountBean::getDay));
        Set<Integer> integers = listByDay.keySet();
        List<AccountListBean>list2 = new ArrayList<>();

        int k;
        for (Integer i: integers) {
            float payCount=0,inComeCount=0,MoneyCount=0,m;
            for (int n =0;n< listByDay.get(i).size();n++){
                k=listByDay.get(i).get(n).getKind();
                m=listByDay.get(i).get(n).getMoney();
                switch (k){
                    case 1:
                        inComeCount +=m;
                        MoneyCount +=m;
                        break;
                    case 0:
                        payCount +=m;
                        MoneyCount -=m;
                        break;
                }

            }

            list2.add(new AccountListBean(listByDay.get(i), i,payCount,inComeCount,MoneyCount));
        }
        return list2;
    }

    /**
     * 获取记账表中当月所有数据 按条来
     */
    public static List<AccountBean> getAccountList2ByMonth(int year,int month){
        initUserID();
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year = ? and month = ?  and userId = ? and bookId = ? order by 0+time desc";

        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "",userId,bookId});
        while (cursor.moveToNext()){
            int id = cursor.getInt((Integer)cursor.getColumnIndex("id"));
            String typename = cursor.getString((Integer)cursor.getColumnIndex("typename"));
            int sImageId = cursor.getInt((Integer)cursor.getColumnIndex("sImageId"));
            String remark = cursor.getString((Integer)cursor.getColumnIndex("remark"));
            float money = cursor.getFloat((Integer)cursor.getColumnIndex("money"));
            String time = cursor.getString((Integer)cursor.getColumnIndex("time"));
            String saveTime = cursor.getString((Integer)cursor.getColumnIndex("saveTime"));
            int year1 = cursor.getInt((Integer)cursor.getColumnIndex("year"));
            int month1 = cursor.getInt((Integer)cursor.getColumnIndex("month"));
            int day = cursor.getInt((Integer)cursor.getColumnIndex("day"));
            int dayForWeek = cursor.getInt((Integer)cursor.getColumnIndex("dayForWeek"));
            int kind = cursor.getInt((Integer)cursor.getColumnIndex("kind"));
            String userId = cursor.getString((Integer)cursor.getColumnIndex("userId"));
            String bookId = cursor.getString((Integer)cursor.getColumnIndex("bookId"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remark, money, time, saveTime, year1, month1, day, dayForWeek, kind,userId,bookId);
            list.add(accountBean);
        }

        return list;
    }
    /**
     * 查询当前月份的预算
     */
    public static YusuanBean getYusuanByMonth(int year,int month){
        initUserID();
        String sql = "select * from yusuantb where year = ? and month = ?  and userId = ? and bookId = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "",userId,bookId});
        YusuanBean yusuanBean = null;
        while (cursor.moveToNext()){
            int id = cursor.getInt((Integer)cursor.getColumnIndex("id"));
            float money = cursor.getFloat((Integer)cursor.getColumnIndex("money"));
            String userId = cursor.getString((Integer)cursor.getColumnIndex("userId"));
            String bookId = cursor.getString((Integer)cursor.getColumnIndex("bookId"));
            yusuanBean = new YusuanBean(id,year,month,money,userId,bookId);
        }
        return yusuanBean;
    }
    /**
     * 查询该用户该账本所有数据 按条来
     */
    public static List<AccountBean>  getAccountListAll(){
        initUserID();
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where userId = ? and bookId = ? order by 0+time desc";

        Cursor cursor = db.rawQuery(sql, new String[]{userId,bookId});
        while (cursor.moveToNext()){
            int id = cursor.getInt((Integer)cursor.getColumnIndex("id"));
            String typename = cursor.getString((Integer)cursor.getColumnIndex("typename"));
            int sImageId = cursor.getInt((Integer)cursor.getColumnIndex("sImageId"));
            String remark = cursor.getString((Integer)cursor.getColumnIndex("remark"));
            float money = cursor.getFloat((Integer)cursor.getColumnIndex("money"));
            String time = cursor.getString((Integer)cursor.getColumnIndex("time"));
            String saveTime = cursor.getString((Integer)cursor.getColumnIndex("saveTime"));
            int year1 = cursor.getInt((Integer)cursor.getColumnIndex("year"));
            int month1 = cursor.getInt((Integer)cursor.getColumnIndex("month"));
            int day = cursor.getInt((Integer)cursor.getColumnIndex("day"));
            int dayForWeek = cursor.getInt((Integer)cursor.getColumnIndex("dayForWeek"));
            int kind = cursor.getInt((Integer)cursor.getColumnIndex("kind"));
            String userId = cursor.getString((Integer)cursor.getColumnIndex("userId"));
            String bookId = cursor.getString((Integer)cursor.getColumnIndex("bookId"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, remark, money, time, saveTime, year1, month1, day, dayForWeek, kind,userId,bookId);
            list.add(accountBean);
        }

        return list;
    }
    /**
     * 更新预算表金额  表操作
     * @param yusuanBean
     * @param tableName
     */
    public static void updateYusuanTable(YusuanBean yusuanBean,String tableName){
        initUserID();
        ContentValues values = setValues(yusuanBean);
        db.update(tableName,values,"year = ? and month = ? and userId = ? and bookId = ?",
                new String[]{yusuanBean.getYear()+"",yusuanBean.getMonth()+"",userId,bookId});
    }

    /**
     * 删除记账信息
     */

    public static void delAccountInfo(int id){
        db.delete("accounttb", "id = ?", new String[] { id+""});
    }
    /**
     * 修改记账信息
     */
    public static void editAccountInfo(AccountBean accountBean){
        ContentValues values = setValues(accountBean);
        db.update("accounttb",values,"id = ?",  new String[]{accountBean.getId()+""});

    }
}
