package com.example.administrator.bicycle.entity;

/**
 * Created by Administrator on 2017-05-05.
 */

public class User {
    //年费
    double T_TRRMB;
    //电话
    String T_USERPHONE;
    //嘿币
    double T_HEIBI;
    //昵称
    String T_USERNAME;
    //   头像链接
    String T_USERIMAGE;
    //实名
    String T_NAME;
    //id
    int TUSER_ID;
    //身份  1 管理员 2 用户
    int T_SIGN;

    public int getT_SIGN() {
        return T_SIGN;
    }

    public void setT_SIGN(int t_SIGN) {
        T_SIGN = t_SIGN;
    }

    public int getTUSER_ID() {
        return TUSER_ID;
    }

    public void setTUSER_ID(int TUSER_ID) {
        this.TUSER_ID = TUSER_ID;
    }


    public double getT_TRRMB() {
        return T_TRRMB;
    }

    public void setT_TRRMB(double t_TRRMB) {
        T_TRRMB = t_TRRMB;
    }

    public double getT_HEIBI() {
        return T_HEIBI;
    }

    public void setT_HEIBI(double t_HEIBI) {
        T_HEIBI = t_HEIBI;
    }

    public String getT_USERPHONE() {
        return T_USERPHONE;
    }

    public void setT_USERPHONE(String t_USERPHONE) {
        T_USERPHONE = t_USERPHONE;
    }

    public String getT_USERNAME() {
        return T_USERNAME;
    }

    public void setT_USERNAME(String t_USERNAME) {
        T_USERNAME = t_USERNAME;
    }


    public String getT_USERIMAGE() {
        return T_USERIMAGE;
    }

    public void setT_USERIMAGE(String t_USERIMAGE) {
        T_USERIMAGE = t_USERIMAGE;
    }

    public String getT_NAME() {
        return T_NAME;
    }

    public void setT_NAME(String t_NAME) {
        T_NAME = t_NAME;
    }
}
