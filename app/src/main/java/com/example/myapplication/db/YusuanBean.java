package com.example.myapplication.db;

public class YusuanBean {
    int id;             //主键
    int year;           //年
    int month;          //月
    float money;        //设置的预算
    String userId = "001";      //用户id
    String bookId = "0";      //账本id

    public YusuanBean(String userId) {
        this.userId = userId;
    }

    public YusuanBean(int id, int year, int month, float money, String userId, String bookId) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.money = money;
        this.userId = userId;
        this.bookId = bookId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
