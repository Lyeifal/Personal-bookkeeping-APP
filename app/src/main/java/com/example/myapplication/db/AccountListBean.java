package com.example.myapplication.db;

import java.util.List;

public class AccountListBean {
    List<AccountBean> list;    //类型
    int day;
    float payCount;
    float inComeCount;
    float MoneyCount;

    public AccountListBean() {
    }

    public AccountListBean( List<AccountBean> list, int day,float payCount,float inComeCount,float MoneyCount) {
        this.payCount = payCount;
        this.inComeCount = inComeCount;
        this.MoneyCount = MoneyCount;
        this.list = list;
        this.day = day;
    }


    public List<AccountBean> getList() {
        return list;
    }

    public void setList(List<AccountBean> list) {
        this.list = list;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getPayCount() {
        return payCount;
    }

    public void setPayCount(float payCount) {
        this.payCount = payCount;
    }

    public float getInComeCount() {
        return inComeCount;
    }

    public void setInComeCount(float inComeCount) {
        this.inComeCount = inComeCount;
    }

    public float getMoneyCount() {
        return MoneyCount;
    }

    public void setMoneyCount(float moneyCount) {
        MoneyCount = moneyCount;
    }
}
