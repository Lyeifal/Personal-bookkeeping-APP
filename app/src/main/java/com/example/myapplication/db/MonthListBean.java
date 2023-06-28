package com.example.myapplication.db;

import java.util.List;

public class MonthListBean {
    int i;
    String month;

    public MonthListBean() {
    }

    public MonthListBean(int i, String month) {
        this.i = i;
        this.month = month;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
