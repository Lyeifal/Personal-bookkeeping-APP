package com.example.myapplication.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestUtil {

    //传递参数
    public String postMethod(String url,String param){
        // 结果值
        StringBuffer rest=new StringBuffer();

        HttpURLConnection conn=null;
        OutputStream out=null;
        BufferedReader br=null;

        try {
            // 创建 URL
            URL restUrl = new URL(url);
            // 打开连接
            conn= (HttpURLConnection) restUrl.openConnection();
            // 设置请求方式为 POST
            conn.setRequestMethod("POST");
            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            //设置本次连接是否自动重定向
            conn.setInstanceFollowRedirects(true);

            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            conn.setConnectTimeout(5*1000);//设置连接时间为5秒
            conn.setReadTimeout(5*1000);//设置读取时间为5秒
            // 设置接收文件类型
//            conn.setRequestProperty("Accept","application/json");
            //设置发送文件类型
            /**
             这里注意  传递JSON数据的话 就要设置
             普通参数的话 就要注释掉
             */
//            conn.setRequestProperty("Content-Type","application/json");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            //开始连接
            conn.connect();

            // 传递参数  流的方式
            out=conn.getOutputStream();

            out.write(param.getBytes());
            out.flush();
            int code = conn.getResponseCode();
            Log.i("code",code+"访问成功");
            if (code == 200){
                // 读取数据
                br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line=null;
                while (null != (line=br.readLine())){
                    rest.append(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭所有通道
            try {
                if (br!=null) {
                    br.close();
                }
                if (out!=null) {
                    out.close();
                }
                if (conn!=null) {
                    conn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rest.toString();
    }
    //纯JSON数据
    public String postMethod2(String url,String param){
        // 结果值
        StringBuffer rest=new StringBuffer();

        HttpURLConnection conn=null;
        OutputStream out=null;
        BufferedReader br=null;

        try {
            // 创建 URL
            URL restUrl = new URL(url);
            // 打开连接
            conn= (HttpURLConnection) restUrl.openConnection();
            // 设置请求方式为 POST
            conn.setRequestMethod("POST");
            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            //设置本次连接是否自动重定向
            conn.setInstanceFollowRedirects(true);
            /**
             这里注意  传递JSON数据的话 就要设置
             普通参数的话 就要注释掉
             */
            //设置发送文件类型
            conn.setRequestProperty("Content-Type","application/json");

            conn.setConnectTimeout(5*1000);//设置连接时间为5秒
            conn.setReadTimeout(5*1000);//设置读取时间为5秒
            // 设置接收文件类型
//            conn.setRequestProperty("Accept","application/json");


            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            //开始连接
            conn.connect();

            // 传递参数  流的方式
            out=conn.getOutputStream();

            out.write(param.getBytes());
            out.flush();
            int code = conn.getResponseCode();
            Log.i("code",code+"zhuang");
            if (code == 200){
                // 读取数据
                br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line=null;
                while (null != (line=br.readLine())){
                    rest.append(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭所有通道
            try {
                if (br!=null) {
                    br.close();
                }
                if (out!=null) {
                    out.close();
                }
                if (conn!=null) {
                    conn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rest.toString();
    }
}

