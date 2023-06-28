package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.adapter.OutterAccountAdapter;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.AccountListBean;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.YusuanBean;
import com.example.myapplication.utils.RemarkDialog;
import com.example.myapplication.utils.RestUtil;
import com.example.myapplication.utils.YearMonthPicker;
import com.example.myapplication.utils.YusuanDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //侧边栏相关
    DrawerLayout mDrawerLayout;
    RelativeLayout mLeftDrawer;

    TextView left_drawer_main_one,left_drawer_main_two,left_drawer_main_thr;
    //主页面
    ListView main_lv;
    ImageView left_list,month_forms,forms,wallet,main_btn_add;
    TextView main_month,cost_money2,get_money2,close_money2;
    //声明数据源
    List<AccountListBean> mDatas;
    OutterAccountAdapter Outteradapter;
    int year,month;
    //头布局 相关
    View headView;
    TextView total_set,total_yusuan;
    ProgressBar pb_determinate;
    YusuanBean yusuanBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取是否登录
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String user_id=sharedPreferences.getString("user_id","001");

        yusuanBean = new YusuanBean(user_id);
        //初始化视图
        initView();
        //添加头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器 加载每一行数据到列表当中
        Outteradapter = new OutterAccountAdapter(mDatas);
        main_lv.setAdapter(Outteradapter);
        initTime();
        main_month.setText(year+"-"+month);
    }

    private void initView() {
        //侧边栏元素
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mLeftDrawer = findViewById(R.id.left_drawer);  //侧边栏
        //主页内元素
        main_lv=findViewById(R.id.main_lv);
        left_list=findViewById(R.id.left_list);
        month_forms=findViewById(R.id.month_forms);
        forms=findViewById(R.id.forms);
//        wallet=findViewById(R.id.wallet);
        main_btn_add=findViewById(R.id.main_btn_add);
        main_month=findViewById(R.id.month);
        cost_money2=findViewById(R.id.cost_money2);
        get_money2=findViewById(R.id.get_money2);
        close_money2=findViewById(R.id.close_money2);
        left_drawer_main_one = findViewById(R.id.left_drawer_main_one);
        left_drawer_main_two = findViewById(R.id.left_drawer_main_two);
        left_drawer_main_thr = findViewById(R.id.left_drawer_main_thr);

        left_list.setOnClickListener(this);  //侧滑栏
        month_forms.setOnClickListener(this);  //日报表
        forms.setOnClickListener(this);     //月报表
//        wallet.setOnClickListener(this);      //钱包 暂时没用
        main_btn_add.setOnClickListener(this);  //添加记账
        main_month.setOnClickListener(this);    //月份选择
        left_drawer_main_one.setOnClickListener(this);
        left_drawer_main_two.setOnClickListener(this);
        left_drawer_main_thr.setOnClickListener(this);

    }

    /**
     * 给listView添加头布局
     */
    private void addLVHeaderView() {
        //将布局转换成view
        headView = getLayoutInflater().inflate(R.layout.item_mainlv_top,null);
        //设置头布局
        main_lv.addHeaderView(headView);
        //查找头布局中需要的控件
        total_set = headView.findViewById(R.id.total_set);
        pb_determinate = headView.findViewById(R.id.pb_determinate);
        total_yusuan = findViewById(R.id.total_yusuan);
        //
        total_set.setOnClickListener(this);//设置预算


    }

    /**
     * 声明周期第三个方法 紧随 onCreate() 和 onStart() 方法 ，主要执行一些用于恢复 Activity 状态的操作
     * 在 onResume() 方法中不应该进行耗时的操作，因为这可能会导致界面卡顿或响应缓慢。
     * 如果必须进行耗时的操作，应该在后台线程中进行，或者在 onResume() 方法中使用异步机制，
     * 例如使用 AsyncTask 或 Handler 等。
     */
    @Override
    protected void onResume() {
        super.onResume();

        loadDBData();

    }

    private void loadDBData() {
        //获取账本数据
        List<AccountListBean> list = DBManager.getAccountListByMonth(year, month);
        //获取预算
        YusuanBean yusuanByMonth = DBManager.getYusuanByMonth(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        Outteradapter.notifyDataSetChanged();
        float incomeCount=0,payCount=0,moneyCount=0;
        for (int i = 0 ;i < list.size();i++){
            incomeCount +=list.get(i).getInComeCount();
            payCount  += list.get(i).getPayCount();
            moneyCount += list.get(i).getMoneyCount();
        }
        //设置支出总支出 总收入 和结余金额
        cost_money2.setText(payCount+"");
        get_money2.setText(incomeCount+"");
        close_money2.setText(moneyCount+"");
        if (yusuanByMonth!=null){
            float yusuanMoney = yusuanByMonth.getMoney();
            if (yusuanMoney!=0){
                //设置预算金额
                total_yusuan.setText(yusuanMoney+"");
                //四舍五入取整
                int i = Math.round(payCount/yusuanMoney*100);
                pb_determinate.setProgress(i);
            }
        }else{
            total_yusuan.setText("未设置");
            pb_determinate.setProgress(0);
        }

        //获取是否登录
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String user_id=sharedPreferences.getString("user_id","001");

        //设置侧边栏按钮

        if (user_id.equals("001")){
            left_drawer_main_thr.setText("登录账户");
            left_drawer_main_one.setVisibility(View.GONE);
            left_drawer_main_two.setVisibility(View.GONE);
        }else {
            left_drawer_main_thr.setText("退出登录");
            left_drawer_main_one.setVisibility(View.VISIBLE);
            left_drawer_main_two.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取今日的具体时间
     */
    private void initTime(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    /**
     * Button相关的点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.month_forms:
                Intent it1 = new Intent(this, BydayActivity.class);//跳转页面
                it1.putExtra("year", year);
                it1.putExtra("month", month);
                startActivity(it1);
                break;
            case R.id.forms:
                Intent it2 = new Intent(this, FormActivity.class);//跳转页面
                it2.putExtra("year", year);
                it2.putExtra("month", month);
                startActivity(it2);
                break;
            case R.id.main_btn_add:
                Intent it3 = new Intent(this, RecordActivity.class);//跳转页面
                startActivity(it3);
                break;
            case R.id.left_list:
                mDrawerLayout.openDrawer(mLeftDrawer);
                break;
            case R.id.month:
                showMonthDialog();
                break;
            case R.id.total_set:
                showYusuanDialog();
                break;
            case R.id.left_drawer_main_thr:
                String str = left_drawer_main_thr.getText().toString().trim();
                if (str.equals("登录账户")){
                    Intent it4 = new Intent(this, LoginActivity.class);//跳转页面
                    startActivity(it4);

                }else if (str.equals("退出登录")){
                    //获取SharedPreferences对象
                    SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                    //获取Editor对象的引用
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //将获取过来的值放入文件
                    editor.remove("user_id");
                    // 提交数据
                    editor.commit();
                    loadDBData();
                }
                break;
            case R.id.left_drawer_main_one:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String result =  uploadBackup();;
                        runOnUiThread(new Runnable() {
                            @SuppressLint("StringFormatMatches")
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.get("code") == null ? "" : jsonObject.get("code").toString();
                                    if (code.equals("200")){
                                       Log.i("上传备份","成功");
                                    }else if (code.equals("201")){
                                        Log.i("上传备份","失败");
                                    }else if (code.equals("103")){
                                        Log.i("缺少参数","失败");
                                    }
                                }catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }.start();
                break;
            case R.id.left_drawer_main_two:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String result =  syncBackup();;
                        runOnUiThread(new Runnable() {
                            @SuppressLint("StringFormatMatches")
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.get("code") == null ? "" : jsonObject.get("code").toString();
                                    if (code.equals("200")){
                                        Log.i("获取备份","成功");
                                        String data = jsonObject.get("data") == null ? "" : jsonObject.get("data").toString();
                                        JSONObject jsonObject1 = new JSONObject(data);
                                        AccountBean accountBean;
                                        Iterator<String> keys = jsonObject1.keys();
                                        while(keys.hasNext()){
                                            String key = (String) keys.next();// 获得key
                                            String orderlist = jsonObject1.get(key).toString();// 获得value
                                            JSONObject jsonObject2 = new JSONObject(orderlist);
                                            String typename = jsonObject2.get("typename") == null ? "" : jsonObject2.get("typename").toString();
                                            String sImageId = jsonObject2.get("sImageId") == null ? "" : jsonObject2.get("sImageId").toString();
                                            String remark = jsonObject2.get("remark") == null ? "" : jsonObject2.get("remark").toString();
                                            String money = jsonObject2.get("money") == null ? "" : jsonObject2.get("money").toString();
                                            String time = jsonObject2.get("time") == null ? "" : jsonObject2.get("time").toString();
                                            String saveTime = jsonObject2.get("saveTime") == null ? "" : jsonObject2.get("saveTime").toString();
                                            String year = jsonObject2.get("year") == null ? "" : jsonObject2.get("year").toString();
                                            String month = jsonObject2.get("month") == null ? "" : jsonObject2.get("month").toString();
                                            String day = jsonObject2.get("day") == null ? "" : jsonObject2.get("day").toString();
                                            String dayForWeek = jsonObject2.get("dayForWeek") == null ? "" : jsonObject2.get("dayForWeek").toString();
                                            String kind = jsonObject2.get("kind") == null ? "" : jsonObject2.get("kind").toString();
                                            String userId = jsonObject2.get("userId") == null ? "" : jsonObject2.get("userId").toString();
                                            String bookId = jsonObject2.get("bookId") == null ? "" : jsonObject2.get("bookId").toString();
                                            int sImageId1 = Integer.parseInt(sImageId);
                                            float money1 = Float.parseFloat(money);
                                            int year1 = Integer.parseInt(year);
                                            int month1 = Integer.parseInt(month);
                                            int day1 = Integer.parseInt(day);
                                            int dayForWeek1 = Integer.parseInt(dayForWeek);
                                            int kind1 = Integer.parseInt(kind);

                                            accountBean = new AccountBean(typename,sImageId1,remark,money1,time,saveTime,year1,month1,day1,dayForWeek1,kind1,userId,bookId);

                                            DBManager.insertItemToTable(accountBean,"accounttb");

                                        }



                                    }else if (code.equals("201")){
                                        Log.i("获取备份","失败");
                                    }else if (code.equals("103")){
                                        Log.i("缺少参数","失败");
                                    }
                                }catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }.start();
                break;
        }
    }
    private String syncBackup(){
        RestUtil restUtil = new RestUtil();

        String IP = "192.168.223.28";
        String url = "http://" + IP + "/server.php?action=SyncBackup";
        // 参数
        String param;
        // 接收数据
        String result = null;

        //获取是否登录
        SharedPreferences sharedPreferences= getSharedPreferences("user", MODE_PRIVATE);
        String user_id=sharedPreferences.getString("user_id","001");


        StringBuffer dataBuffer = new StringBuffer();
        try {
            dataBuffer.append(URLEncoder.encode("userId", "UTF-8"));
            dataBuffer.append("=");
            dataBuffer.append(URLEncoder.encode(user_id, "UTF-8"));

            dataBuffer.append("&");

            dataBuffer.append(URLEncoder.encode("bookId", "UTF-8"));
            dataBuffer.append("=");
            dataBuffer.append(URLEncoder.encode("0", "UTF-8"));

            param = dataBuffer.toString();

            result = restUtil.postMethod(url, param);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return result;
    }
    private String uploadBackup(){
        RestUtil restUtil = new RestUtil();

        String IP = "192.168.223.28";
        String url = "http://" + IP + "/server.php?action=UploadBackup";
        //获取参数
        List<AccountBean> ListAll = DBManager.getAccountListAll();
        YusuanBean yusuan = DBManager.getYusuanByMonth(year, month);
        // 参数
        String param;
        // 接收数据
        String result = null;

        //处理accountData
        String params ="{";
        for (int i =0;i<ListAll.size();i++){
            params += "\""+i+"\":{\"typename\":\"" + ListAll.get(i).getTypename() +
                    "\",\"sImageId\":\"" + ListAll.get(i).getsImageId() +
                    "\",\"remark\":\"" + ListAll.get(i).getRemark() +
                    "\",\"money\":\"" + ListAll.get(i).getMoney()+
                    "\",\"time\":\"" + ListAll.get(i).getTime() +
                    "\",\"saveTime\":\"" + ListAll.get(i).getSaveTime()+
                    "\",\"year\":\"" + ListAll.get(i).getYear()+
                    "\",\"month\":\"" + ListAll.get(i).getMonth()+
                    "\",\"day\":\"" + ListAll.get(i).getDay()+
                    "\",\"dayForWeek\":\"" + ListAll.get(i).getDayForWeek()+
                    "\",\"kind\":\"" + ListAll.get(i).getKind()+
                    "\",\"userId\":\"" + ListAll.get(i).getUserId()+
                    "\",\"bookId\":\"" + ListAll.get(i).getBookId()+
                    "\"}";
            if (i!=ListAll.size()-1){
                params +=",";
            }
        }
        params +="}";
        //处理yusuanData
//        String param2 = "{\"year\":\"" + yusuan.getYear() +
//                    "\",\"month\":\"" + yusuan.getMonth()+
//                    "\",\"money\":\"" + yusuan.getMoney() +
//                    "\",\"userId\":\"" +yusuan.getUserId()+
//                    "\",\"bookId\":\"" + yusuan.getBookId()+
//                    "\"}";


        StringBuffer dataBuffer = new StringBuffer();
        try {
            dataBuffer.append(URLEncoder.encode("accountData", "UTF-8"));
            dataBuffer.append("=");
            dataBuffer.append(URLEncoder.encode(params, "UTF-8"));

//            dataBuffer.append("&");
//
//            dataBuffer.append(URLEncoder.encode("yusuanData", "UTF-8"));
//            dataBuffer.append("=");
//            dataBuffer.append(URLEncoder.encode(param2, "UTF-8"));

            param = dataBuffer.toString();

            result = restUtil.postMethod(url, param);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        return result;
    }
    /**
     * 展示设置预算的对话框
     */
    private void showYusuanDialog(){
        YusuanDialog dialog = new YusuanDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new RemarkDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    //更新预算金额
                    total_yusuan.setText(msg);

                    yusuanBean.setMoney(Float.parseFloat(msg));
                    yusuanBean.setYear(year);
                    yusuanBean.setMonth(month);
                    DBManager.UpdateYsuan(yusuanBean);

                }
                dialog.cancel();
                loadDBData();
            }
        });
    }

    /**
     * 弹出月份选择对话框
     */
    public void showMonthDialog(){
        YearMonthPicker dialog = new YearMonthPicker(this,year,month);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                // Dialog关闭时执行的操作
                String Y = dialog.getyTV().getText().toString();
                String M  = dialog.getmTV().getText().toString();

                main_month.setText(Y+"-"+M);
                year = Integer.parseInt(Y);
                month = Integer.parseInt(M);
                loadDBData();

            }
        });


    }
}