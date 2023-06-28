package com.example.myapplication.db;

import java.util.List;

public class AccountBackupBean {
    List<AccountBean> list;    //类型

    YusuanBean yusuanBean;

    String saveTime;

    public AccountBackupBean() {
    }

    public AccountBackupBean(List<AccountBean> list, YusuanBean yusuanBean, String saveTime) {
        this.list = list;
        this.yusuanBean = yusuanBean;
        this.saveTime = saveTime;
    }

    public List<AccountBean> getList() {
        return list;
    }

    public void setList(List<AccountBean> list) {
        this.list = list;
    }

    public YusuanBean getYusuanBean() {
        return yusuanBean;
    }

    public void setYusuanBean(YusuanBean yusuanBean) {
        this.yusuanBean = yusuanBean;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }
}
