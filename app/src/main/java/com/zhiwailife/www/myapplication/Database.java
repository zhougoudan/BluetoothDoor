package com.zhiwailife.www.myapplication;



import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Database extends LitePalSupport {
    private int lock_Address;
    private String lock_name="liang";
    private String user_name;
    private int user_Address;
    private String dormitory;
    private Boolean isManager;
    private int viplessTime;
    private double moneytogether;
    private Date first_use_day;
    private int manager_Address;
    private int uuid;
    private boolean is_renew;
    private String passsword_MD5;
    private boolean is_Vip;
    private int user_number;
    private String data_id="1";

    public int getLock_Address() {
        return lock_Address;
    }

    public void setLock_Address(int lock_Address) {
        this.lock_Address = lock_Address;
    }

    public String getLock_name() {
        return lock_name;
    }

    public void setLock_name(String lock_name) {
        this.lock_name = lock_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_Address() {
        return user_Address;
    }

    public void setUser_Address(int user_Address) {
        this.user_Address = user_Address;
    }

    public String getDormitory() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }

    public int getViplessTime() {
        return viplessTime;
    }

    public void setViplessTime(int viplessTime) {
        this.viplessTime = viplessTime;
    }

    public double getMoneytogether() {
        return moneytogether;
    }

    public void setMoneytogether(double moneytogether) {
        this.moneytogether = moneytogether;
    }

    public Date getFirst_use_day() {
        return first_use_day;
    }

    public void setFirst_use_day(Date first_use_day) {
        this.first_use_day = first_use_day;
    }

    public int getManager_Address() {
        return manager_Address;
    }

    public void setManager_Address(int manager_Address) {
        this.manager_Address = manager_Address;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public boolean isIs_renew() {
        return is_renew;
    }

    public void setIs_renew(boolean is_renew) {
        this.is_renew = is_renew;
    }

    public String getPasssword_MD5() {
        return passsword_MD5;
    }

    public void setPasssword_MD5(String passsword_MD5) {
        this.passsword_MD5 = passsword_MD5;
    }

    public boolean isIs_Vip() {
        return is_Vip;
    }

    public void setIs_Vip(boolean is_Vip) {
        this.is_Vip = is_Vip;
    }

    public int getUser_number() {
        return user_number;
    }

    public void setUser_number(int user_number) {
        this.user_number = user_number;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }
}
