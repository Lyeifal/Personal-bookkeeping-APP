package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.RestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username,password;
    TextView sign_in,sign_up,login_warn_smg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        //初始化视图
        initView();

    }

    private void initView() {
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        sign_in = findViewById(R.id.login_sign_in);
        sign_up = findViewById(R.id.login_sign_up);
        login_warn_smg = findViewById(R.id.login_warn_smg);

        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_sign_in:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String result = getNetwork("Login");
                        runOnUiThread(new Runnable() {
                            @SuppressLint("StringFormatMatches")
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.get("code") == null ? "" : jsonObject.get("code").toString();
                                    if (code.equals("200")){
                                        String userid = jsonObject.get("data") == null ? "" : jsonObject.get("data").toString();
                                        //获取SharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                                        //获取Editor对象的引用
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //将获取过来的值放入文件
                                        editor.putString("user_id", userid);
                                        // 提交数据
                                        editor.commit();
                                        LoginActivity.this.finish();
                                    }else if (code.equals("201")){
                                        login_warn_smg.setText("用户不存在");
                                    }else if (code.equals("202")){
                                        login_warn_smg.setText("登录失败,密码错误");
                                    }
                                }catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }.start();


                break;
            case R.id.login_sign_up:

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String result = getNetwork("Enroll");
                        runOnUiThread(new Runnable() {
                            @SuppressLint("StringFormatMatches")
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.get("code") == null ? "" : jsonObject.get("code").toString();
                                    if (code.equals("200")){
                                        String userid = jsonObject.get("data") == null ? "" : jsonObject.get("data").toString();
                                        //获取SharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                                        //获取Editor对象的引用
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //将获取过来的值放入文件
                                        editor.putString("user_id", userid);
                                        // 提交数据
                                        editor.commit();
                                        LoginActivity.this.finish();
                                    }else if (code.equals("201")){
                                        login_warn_smg.setText("用户名已存在");
                                    }else if (code.equals("202")){
                                        login_warn_smg.setText("注册失败");
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

    private String getNetwork(String action){
        RestUtil restUtil = new RestUtil();

        String IP = "192.168.223.28";
        String url = "http://" + IP + "/server.php?action="+action;
        //获取参数
        String username1 = username.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        String dateTime = "2023-4";
        // 参数
        String param;
        // 接收数据
        String result = null;
//        /**
//         * 第一种   参数为JSON
//         * 要打开设置 conn.setRequestProperty("Content-Type","application/json");
//         */
//
//        param = "{\"username\":\"" + username1 + "\",\"password\":\"" + password1 + "\"}";
//        result = restUtil.postMethod(url, param);



        /**
         * 第二种  直接传递参数
         * 关闭 conn.setRequestProperty("Content-Type","application/json");
         */
        StringBuffer dataBuffer = new StringBuffer();
        try {
            dataBuffer.append(URLEncoder.encode("user_name", "UTF-8"));
            dataBuffer.append("=");
            dataBuffer.append(URLEncoder.encode(username1, "UTF-8"));

            dataBuffer.append("&");

            dataBuffer.append(URLEncoder.encode("password", "UTF-8"));
            dataBuffer.append("=");
            dataBuffer.append(URLEncoder.encode(password1, "UTF-8"));

            param = dataBuffer.toString();

            result = restUtil.postMethod(url, param);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        接收的数据
        System.out.println("result11==" + result);

//        // 然后对 数据 进行 JSON解析
//        JSONObject jsonObject = new JSONObject(result);
//
//        String code = jsonObject.get("code") == null ? "" : jsonObject.get("code").toString();
//        String message = jsonObject.get("message") == null ? "" : jsonObject.get("message").toString();
//        dateTime = jsonObject.get("dateTime") == null ? "" : jsonObject.get("dateTime").toString();
//        JSONArray dataJsonArray = (JSONArray) jsonObject.get("data");
//        if (null != dataJsonArray && dataJsonArray.length() > 0) {
//            for (int i = 0; i < dataJsonArray.length(); i++) {
//                JSONObject data = (JSONObject) dataJsonArray.get(i);
//                String account = data.get("account") == null ? "" : data.get("account").toString();
//                String name = data.get("name") == null ? "" : data.get("name").toString();
//
//                System.out.println("code==" + code + "++message==" + message + "++dateTime==" + dateTime
//                        + "++account==" + account + "++name==" + name);
//            }
//        }

        return result;

    }


}
