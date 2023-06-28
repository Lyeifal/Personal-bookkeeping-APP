package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

public class YusuanDialog extends Dialog implements View.OnClickListener{
    EditText et;
    Button yes,no;
    RemarkDialog.OnEnsureListener onEnsureListener;

    public YusuanDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yusuan); //设置对话框显示布局
        et = findViewById(R.id.dialog_yusuan_et);
        yes = findViewById(R.id.dialog_yusuan_btn_y);
        no = findViewById(R.id.dialog_yusuan_btn_n);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_yusuan_btn_n:
                cancel();
                break;
            case R.id.dialog_yusuan_btn_y:
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }
    /**
     * 获取输入数据的方法
     * @param
     */
    public String getEditText(){
        return  et.getText().toString().trim();
    }

    /**
     * 设置dialog的尺寸和屏幕尺寸一至
     *
     * @param
     */
    public void setDialogSize() {
        //获取当前窗口对象
        Window w = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams wlp = w.getAttributes();
        //获取屏幕宽度
        Display d = w.getWindowManager().getDefaultDisplay();
        //对话窗口为屏幕窗口
        wlp.width=(int)(d.getWidth());
        //从下往上显示
        wlp.gravity= Gravity.BOTTOM;
        //设置背景资源 透明的
        w.setBackgroundDrawableResource(android.R.color.transparent);
        w.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,200);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //原来打开就关闭 原来关闭就打开
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
    public void setOnEnsureListener(RemarkDialog.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }
}
