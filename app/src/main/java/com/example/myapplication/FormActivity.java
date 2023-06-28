package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.AccountListBean;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.TypeBean;
import com.example.myapplication.db.TypePieChartBean;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FormActivity extends AppCompatActivity  implements View.OnClickListener{

    //主页
    ImageView form_iv_back;
    TextView form_month;
    //柱状图对象
    BarChart form_bar_chart;
    PieChart form_pie_chart_pay,form_pie_chart_income;
    //跳转页面携带参数的对象
    Intent intent ;
    //时间
    int year,month;
    //当月总支出和总收入
    float countPayMoney = 0,countIncomeMoney =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //初始化视图
        initView();
        //给柱状图提供数据
        loadDataToBarChart();
        //给饼图提供数据
        loadDataToPieChart();
    }

    private void loadDataToBarChart() {
        List<AccountListBean> mDatas = DBManager.getAccountListByMonth(year, month);
        List<BarEntry> entries1 = new ArrayList<>();
        for (int i = 1 ;i<=31;i++){
            for (int j =0;j<mDatas.size();j++){
                if (mDatas.get(j).getDay() == i){
                    entries1.add(new BarEntry(i, mDatas.get(j).getPayCount()));
                }else {
                    entries1.add(new BarEntry(i, 0));
                }
            }
        }
        BarDataSet dataSet1= new BarDataSet(entries1, "支出"); // 可以设置Label
        dataSet1.setColors(ContextCompat.getColor(FormActivity.this,R.color.red_F01707)); // 设置颜色

        List<BarEntry> entries2 = new ArrayList<>();
        for (int i = 1 ;i<=31;i++){
            for (int j =0;j<mDatas.size();j++){
                if (mDatas.get(j).getDay() == i){
                    entries2.add(new BarEntry(i, mDatas.get(j).getInComeCount()));
                }else {
                    entries2.add(new BarEntry(i, 0));
                }
            }
        }
        BarDataSet dataSet2 = new BarDataSet(entries2, "收入"); // 可以设置Label
        dataSet2.setColors(ContextCompat.getColor(FormActivity.this,R.color.green_4CAF50)); // 设置颜色


        BarData data1 = new BarData(dataSet1);  //用第一组数据声明对象
        data1.addDataSet(dataSet2); //添加第二组数据
        data1.setBarWidth(0.2f);


        form_bar_chart.setData(data1);


        //设置X Y轴
        XAxis xAxis = form_bar_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X轴在底部显示
        xAxis.setGranularity(2f); // X轴每个格子的间距
        xAxis.setDrawGridLines(false); //关闭网格竖线

        //左Y轴
        YAxis leftAxis = form_bar_chart.getAxisLeft();
        leftAxis.setSpaceTop(15); // 设置图表中最高值相对于轴上最高值的顶部间距(占总轴范围的百分比)。
        leftAxis.setSpaceBottom(0);//设置图表中最低值相对于轴上最低值的底部间距(占总轴范围的百分比)。
        leftAxis.setDrawGridLines(false); // 关闭网格横线
        leftAxis.setEnabled(true);
        //右Y轴
        YAxis rightAxis = form_bar_chart.getAxisRight();
        rightAxis.setEnabled(false);  //不使用右Y轴



    }
    private void loadDataToPieChart(){
        List<TypePieChartBean> pieChartList = getPieChartList();
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        ArrayList<PieEntry> entries2 = new ArrayList<>();
        for (int i = 0 ;i<pieChartList.size();i++){
            if (pieChartList.get(i).getKind() == 0) {
                entries1.add(new PieEntry(pieChartList.get(i).getCountMoney(), pieChartList.get(i).getTypename()));
            } else if (pieChartList.get(i).getKind() == 1) {
                entries2.add(new PieEntry(pieChartList.get(i).getCountMoney(), pieChartList.get(i).getTypename()));
            }
        }
        PieDataSet pieDataSet = new PieDataSet(entries1, "支出");
        PieDataSet pieDataSet2 = new PieDataSet(entries2, "收入");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(ContextCompat.getColor(FormActivity.this,R.color.white));

        PieData pieData2 = new PieData(pieDataSet2);
        pieData2.setValueTextSize(12f);
        pieData2.setValueTextColor(ContextCompat.getColor(FormActivity.this,R.color.white));

        form_pie_chart_pay.setData(pieData);
//        form_pie_chart.getDescription().setEnabled(false);
        form_pie_chart_pay.setDrawHoleEnabled(false);
        form_pie_chart_pay.invalidate();

        form_pie_chart_income.setData(pieData2);
        form_pie_chart_income.setDrawHoleEnabled(false);
        form_pie_chart_income.invalidate();

    }

    private List<TypePieChartBean> getPieChartList() {

        List<AccountBean> mDatas = DBManager.getAccountList2ByMonth(year, month); //按条获取记账记录

        List<TypePieChartBean> list = new ArrayList<>();

        //支出TypeList
        Map<String,String> list1 = new HashMap<>();
        //收入TypeList
        Map<String,String> list2 = new HashMap<>();
        for (int i = 0 ; i<mDatas.size();i++){
            if (mDatas.get(i).getKind() == 0){
                //统计支出的总金额
                countPayMoney += mDatas.get(i).getMoney();
                list1.put(mDatas.get(i).getTypename(),"0");
            }else if (mDatas.get(i).getKind() == 1){
                //统计收入的总金额
                countIncomeMoney += mDatas.get(i).getMoney();
                list2.put(mDatas.get(i).getTypename(),"0");
            }
        }
        float countMoney = 0;
        //支出部分
        Set<String> strings1 = list1.keySet();
        for (String s:strings1){
            for (int j =0;j<mDatas.size();j++){
                //统计支出部分 单一类别总金额
                if (mDatas.get(j).getTypename() == s){
                    countMoney += mDatas.get(j).getMoney();
                }
            }
            list.add(new TypePieChartBean(s,0,countMoney));
        }
        //收入部分
        Set<String> strings2 = list2.keySet();
        for (String s:strings2){
            for (int j =0;j<mDatas.size();j++){
                //统计支出部分 单一类别总金额
                if (mDatas.get(j).getTypename() == s){
                    countMoney += mDatas.get(j).getMoney();
                }
            }
            list.add(new TypePieChartBean(s,1,countMoney));
        }
        return list;
    }

    private void initView() {
        form_iv_back = findViewById(R.id.form_iv_back);
        form_month = findViewById(R.id.form_month);
        form_bar_chart = findViewById(R.id.form_bar_chart);
        form_pie_chart_pay = findViewById(R.id.form_pie_chart_pay);
        form_pie_chart_income = findViewById(R.id.form_pie_chart_income);
        intent= getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);

        //提供组件数据
        form_month.setText(year+"-"+month);
        //设置点击事件
        form_iv_back.setOnClickListener(this);  //返回首页
        form_month.setOnClickListener(this);  //选择月份

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.form_iv_back:
                finish();
                break;
            case R.id.form_month:
                break;
        }
    }
}