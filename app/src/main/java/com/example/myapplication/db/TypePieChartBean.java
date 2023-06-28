package com.example.myapplication.db;

public class TypePieChartBean {
    String typename;    //类型名称

    int kind;           //收入1 支出0

    float countMoney;    //该类型收入或支出总金额



    public TypePieChartBean() {
    }

    public TypePieChartBean(String typename, int kind, float countMoney) {
        this.typename = typename;
        this.kind = kind;
        this.countMoney = countMoney;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public float getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(float countMoney) {
        this.countMoney = countMoney;
    }
}
