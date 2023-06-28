package com.example.myapplication.db;

/**
 * 描述记录一条数据的相关内容类
 */
public class AccountBean {
    int id;
    String typename;    //类型
    int sImageId;       //被选中的图片类型
    String remark;      //备注
    float money;        //金额
    String time;        //消费时间
    String saveTime;    //保存时间
    int year;
    int month;
    int day;
    int dayForWeek;     //星期几
    int kind;           //类型 收入为1 支出为0
    String userId;      //用户id
    String bookId;      //账本id

    public AccountBean(String userId,String bookId) {
        this.userId = userId;
        this.bookId = bookId;

    }

    public AccountBean(int id, String typename, int sImageId, String remark, float money, String time, String saveTime, int year, int month, int day, int dayForWeek, int kind, String userId, String bookId) {
        this.id = id;
        this.typename = typename;
        this.sImageId = sImageId;
        this.remark = remark;
        this.money = money;
        this.time = time;
        this.saveTime = saveTime;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayForWeek = dayForWeek;
        this.kind = kind;
        this.userId = userId;
        this.bookId = bookId;
    }

    public AccountBean(String typename, int sImageId, String remark, float money, String time, String saveTime, int year, int month, int day, int dayForWeek, int kind, String userId, String bookId) {
        this.typename = typename;
        this.sImageId = sImageId;
        this.remark = remark;
        this.money = money;
        this.time = time;
        this.saveTime = saveTime;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayForWeek = dayForWeek;
        this.kind = kind;
        this.userId = userId;
        this.bookId = bookId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDayForWeek() {
        return dayForWeek;
    }

    public void setDayForWeek(int dayForWeek) {
        this.dayForWeek = dayForWeek;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
