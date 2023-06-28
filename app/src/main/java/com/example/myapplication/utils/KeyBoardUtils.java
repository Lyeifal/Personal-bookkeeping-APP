package com.example.myapplication.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;

public class KeyBoardUtils {
    private final Keyboard k1; //自定义键盘对象
    private KeyboardView keyboardView;
    private EditText editText;

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);//取消弹出系统键盘
        //获取自定义键盘的对象  第一个变量指当前editText运行在哪个Activity Contex  第二个变量是按键地址
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);
        //设置keyboardView显示的样式是我们自己的键盘k1
        this.keyboardView.setKeyboard(k1);
        //设置他是可以使用的
        this.keyboardView.setEnabled(true);
        //设置他能够预览
        this.keyboardView.setPreviewEnabled(false);
        //设置这个键盘按钮被点击了的监听
        this.keyboardView.setOnKeyboardActionListener(listener);
    }
    //点击完成的接口回调
    public interface OnEnsureListener{
        public void onEnsure();
    }
    //将接口对象进行初始化
    OnEnsureListener onEnsureListener;

    //set这个接口 将接口对象传入进来
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {
        }
        @Override
        public void onRelease(int i) {
        }
        @Override
        public void onKey(int i, int[] ints) {
            //获取按键能够写入的数据
            Editable editable = editText.getText();
            //获取光标位置
            int start = editText.getSelectionStart();
            switch (i){
                case Keyboard.KEYCODE_DELETE://点击了删除
                    //有数据，且数据大于0 可以删除
                    if (editable!=null&&editable.length()>0) {
                        //光标位置大于0
                        if (start>0) {
                            //删除光标前的字符
                            editable.delete(start-1,start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL://点击了清零
                    //输入框全部删除
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE://点击了完成
                    onEnsureListener.onEnsure(); //通过接口回调的方法，当点击确定时，可以调用这个方法
                    break;
                default: //其他的数字完成了插入
                    //在光标处插入字符
                    editable.insert(start,Character.toString((char)i));
                    break;
            }
        }
        @Override
        public void onText(CharSequence charSequence) {
        }
        @Override
        public void swipeLeft() {
        }
        @Override
        public void swipeRight() {
        }
        @Override
        public void swipeDown() {
        }
        @Override
        public void swipeUp() {
        }
    };
    //显示键盘
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility(); //获取显示的状态
        //是否看不见 是否隐藏
        if (visibility == View.INVISIBLE || visibility==View.GONE) {
            //符合条件 显示键盘
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    //隐藏键盘
    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility(); //获取显示的状态
        //是否显示 是否看不见
        if (visibility == View.VISIBLE || visibility==View.INVISIBLE) {
            //符合条件 隐藏键盘
            keyboardView.setVisibility(View.GONE);
        }
    }
}
