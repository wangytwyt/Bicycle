package com.example.administrator.bicycle.Post;

/**
 * Created by Administrator on 2017/6/29.
 */

public class Url {
    public final static String url = "http://192.168.1.189:8080";
    // private  final static  String url = "http://192.168.1.126:8080";





    private final static String bidypath = url + "/heibike/tbike/";


    public final   static  String Login = url+"/heibike/Lg/Login.do?";




    //举报中心
    public  final static  String  reportingCenter=bidypath+ "JbZx.do?";


//车牌
    public final static String allBike = bidypath + "listBike.do?";
    //时间
    public final static String putInTime = bidypath + "findBikeByTfTime.do?";
//状态
    public final static String statusBike = bidypath+"findByZt.do?";
//信息录入

    public final static  String saveBike = bidypath+"saveTb.do?";

//    //报修时间
//    public final static String repairTime = bidypath + "findBikeByBxtime.do?";
//    //完成时间
//    public final static String completeTime = bidypath + "findBikeByYjtime.do?";




}
