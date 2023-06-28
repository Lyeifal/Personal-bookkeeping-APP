package com.example.myapplication.frag_record;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.TypeBean;
import com.example.myapplication.utils.KeyBoardUtils;
import com.example.myapplication.utils.RemarkDialog;
import com.example.myapplication.utils.CalendarDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{
    KeyboardView keyboardView;
    EditText moneyEt;
    TextView timeTv,remarkTv;
    GridView typeGv;
    List<TypeBean> typeList;
    TypeBaseAdapter adapter;
    KeyBoardUtils boardUtils;
    AccountBean accountBean; //将需要插入到记账本的数据保存成对象

    int year,month,day,hour,min,dayForWeek;

    //

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("user", MODE_PRIVATE);
        String userid=sharedPreferences.getString("user_id","001");
        String bookid = getString(R.string.book_id);
        accountBean = new AccountBean(userid,bookid); //初始化对象
        accountBean.setTypename("三餐");
        accountBean.setsImageId(R.mipmap.food);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);

        //初始化View 给每个对象绑定相对应的View
        initView(view);
        //初始化时间 即当前时间
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGV();
        //设置GridVidew的点击事件
        setGVListener();

        return view;
    }

    /**
     * 初始化View 给每个对象绑定相对应的View
     * @param view
     */
    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        timeTv = view.findViewById(R.id.frag_record_option1);
        remarkTv = view.findViewById(R.id.frag_record_remark);
        typeGv = view.findViewById(R.id.frag_record_gv);

        remarkTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        //让自定义软键盘显示出来
        boardUtils= new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口监听，确定按钮被点击了
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击了确定按钮

                //获取输入钱数
                String s = moneyEt.getText().toString();
                if (TextUtils.isEmpty(s)||s.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float v = Float.parseFloat(s);
                accountBean.setMoney(v);
                //获取备注
                String remark = remarkTv.getText().toString().trim();
                if (!TextUtils.isEmpty(remark)) {
                    accountBean.setRemark(remark);
                }
                //获取记录的信息，保存在数据库当中
                saveAccountToDB();

                //返回上一级页面
                getActivity().finish();
            }
        });
    }

    public abstract void saveAccountToDB() ;

    /**
     * 初始化时间 获取当前时间  显示在TimeTV上
     */
    private void setInitTime() {
        Date date = new Date();
        //设置时间戳格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //把时间转换成设置好的格式的String字符串
        String time = sdf.format(date);
        //显示初始化时间
        timeTv.setText(time);
        //获取初始化年月日
        Calendar instance = Calendar.getInstance();
        year = instance.get(Calendar.YEAR);
        month = instance.get(Calendar.MONTH)+1;
        day = instance.get(Calendar.DAY_OF_MONTH);
        dayForWeek = instance.get(Calendar.DAY_OF_WEEK);
        hour =instance.get(Calendar.HOUR_OF_DAY);
        min = instance.get(Calendar.MINUTE);
        //设置初始化年月日和时间
        accountBean.setTime(time);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
        accountBean.setDayForWeek(dayForWeek);
    }

    /**
     * 给GridView填充数据的方法
     */
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);

    }

    /**
     * 设置监听器 监听GV对象 点击事件
     */
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.selectPos = i;
                adapter.notifyDataSetChanged();//提示绘制发生变化
                TypeBean typeBean = typeList.get(i);  //获取被选中类型的对象
                //获取对象参数
                String typename = typeBean.getTypename();
                int imageId = typeBean.getImageId();

                //设置记录对象的参数
                accountBean.setTypename(typename);
                accountBean.setsImageId(imageId);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_record_remark:
                showRemarkDialog();
                break;
            case R.id.frag_record_option1:
                showTimeDialog();
                break;
        }
    }

    /**
     * 弹出显示时间的对话框
     */
    private void showTimeDialog() {
        CalendarDialog dialog = new CalendarDialog(getContext(),hour,min);
        dialog.show();
        //设置确定按钮被点击的监听器
        dialog.setOnEnsureListener(new CalendarDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day, int dayForWeek) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
                accountBean.setDayForWeek(dayForWeek);
            }
        });
    }

    /**
     * 弹出对话框
     */
    public void showRemarkDialog() {
        RemarkDialog dialog = new RemarkDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new RemarkDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    remarkTv.setText(msg);
                    accountBean.setRemark(msg);
                }
                dialog.cancel();
            }
        });
    }
}