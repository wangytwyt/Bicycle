package com.example.administrator.bicycle.entity;

/**
 * Created by Administrator on 2017/6/29.
 */

public class CollectInformationEntity {
    /*
    *状态
     */
    String T_BIKENOMAL;
    /*
    *预计完成时间
     */
    String T_YJTime;
    /*
    *车牌号
     */
    String T_BIKENO;
    /*
    *投放
     */
    String T_BIKETIME;
    /*
    *报修
     */
    String T_BXTIME;



    public String getT_BXTIME() {
        return T_BXTIME;
    }

    public String getT_BIKETIME() {
        return T_BIKETIME;
    }

    public String getT_BIKENO() {
        return T_BIKENO;
    }

    public String getT_YJTime() {
        return T_YJTime;
    }

    public String getT_BIKENOMAL() {
        return T_BIKENOMAL;
    }

    public void setT_BIKENOMAL(String t_BIKENOMAL) {
        T_BIKENOMAL = t_BIKENOMAL;
    }

    public void setT_YJTime(String t_YJTime) {
        T_YJTime = t_YJTime;
    }

    public void setT_BIKENO(String t_BIKENO) {
        T_BIKENO = t_BIKENO;
    }

    public void setT_BIKETIME(String t_BIKETIME) {
        T_BIKETIME = t_BIKETIME;
    }

    public void setT_BXTIME(String t_BXTIME) {
        T_BXTIME = t_BXTIME;
    }
}
