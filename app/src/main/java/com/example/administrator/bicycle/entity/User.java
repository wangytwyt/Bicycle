package com.example.administrator.bicycle.entity;

/**
 * Created by Administrator on 2017-05-05.
 */

public class User {

    private int vip_userid;

    private int vip_vipuser;

    private String vip_username;

    private String vip_url;

    private String vip_token;

    private int vip_credit;

    private String vip_idcard;

    private String vip_phone;

    private String vip_nicheng;

    private long vip_regtime;

    private long vip_exptime;

    public long getVip_regtime() {
        return vip_regtime;
    }

    public void setVip_regtime(long vip_regtime) {
        this.vip_regtime = vip_regtime;
    }

    public long getVip_exptime() {
        return vip_exptime;
    }

    public void setVip_exptime(long vip_exptime) {
        this.vip_exptime = vip_exptime;
    }

    public void setVip_userid(int vip_userid){
        this.vip_userid = vip_userid;
    }
    public int getVip_userid(){
        return this.vip_userid;
    }
    public void setVip_vipuser(int vip_vipuser){
        this.vip_vipuser = vip_vipuser;
    }
    public int getVip_vipuser(){
        return this.vip_vipuser;
    }
    public void setVip_username(String vip_username){
        this.vip_username = vip_username;
    }
    public String getVip_username(){
        return this.vip_username;
    }
    public void setVip_url(String vip_url){
        this.vip_url = vip_url;
    }
    public String getVip_url(){
        return this.vip_url;
    }
    public void setVip_token(String vip_token){
        this.vip_token = vip_token;
    }
    public String getVip_token(){
        return this.vip_token;
    }
    public void setVip_credit(int vip_credit){
        this.vip_credit = vip_credit;
    }
    public int getVip_credit(){
        return this.vip_credit;
    }
    public void setVip_idcard(String vip_idcard){
        this.vip_idcard = vip_idcard;
    }
    public String getVip_idcard(){
        return this.vip_idcard;
    }
    public void setVip_phone(String vip_phone){
        this.vip_phone = vip_phone;
    }
    public String getVip_phone(){
        return this.vip_phone;
    }
    public void setVip_nicheng(String vip_nicheng){
        this.vip_nicheng = vip_nicheng;
    }
    public String getVip_nicheng(){
        return this.vip_nicheng;
    }

}
