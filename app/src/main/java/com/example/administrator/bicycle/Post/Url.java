package com.example.administrator.bicycle.Post;

/**
 * Created by Administrator on 2017/6/29.
 */

public class Url {
    public final static String url = "http://42.51.40.93:8080/heibike";
    // private  final static  String url = "http://192.168.1.126:8080";


    private final static String bidypath = url + "/tbike/";

    public final static String Login = url + "/Lg";

    public final static String loginUrl = Login + "/Login.do?";

    public final static String deleteLog = Login + "/deleteLog.do?T_USERPHONE=";
    public final static String prizeUrl = Login + "/insertDw.do?";

    public final static String IdentityUrl = Login + "/insertSm.do?";

    public final static String getRMB = url + "/tuser/findBPh.do?T_USERPHONE=";
    //善良分
    public final static String getSL = url + "/tuser/findSL.do?T_USERPHONE=";

    public final static String getDay = url + "/tuser/findDay.do?T_USERPHONE=";

    //昵称
    public final static String nicknameUrl = url + "/tuser/upNcByPh.do?";
    //更换电话号
    public final static String updataPhone = url + "/tuser/upPhByPh.do?TUSER_ID=";

    //举报中心
    public final static String ReportingCenter = bidypath + "JbZx.do?BxId=";

    //开始骑行
    public final static String startLock = bidypath + "unlock.do?T_BIKENO=";
    //结束骑行
    public final static String endLock = bidypath + "lock.do?T_BIKENO=";
    //根据车牌号查询锁号和蓝牙地址
    public final static String getLockInfo = bidypath + "findByBn.do?T_BIKENO=";

    //举报中心
    public final static String reportingCenter = bidypath + "JbZx.do?";
    ///投诉
    public final static String insertTs = bidypath + "insertTs.do?";
    //车牌
    public final static String allBike = bidypath + "listBike.do?";
    //时间
    public final static String putInTime = bidypath + "findBikeByTfTime.do?";
    //状态
    public final static String statusBike = bidypath + "findByZt.do?";
//信息录入

    public final static String saveBike = bidypath + "saveTb.do?";

//    //报修时间
//    public final static String repairTime = bidypath + "findBikeByBxtime.do?";
//    //完成时间
//    public final static String completeTime = bidypath + "findBikeByYjtime.do?";


    private final static String htmlUrl = "http://42.51.40.93:120" + "/html/";
    public final static String authentication = htmlUrl + "authentication.html";
    public final static String activity_rule = htmlUrl + "activity_rule.html";
    public final static String cedicalInsurance = htmlUrl + "cedicalInsurance.html";
    public final static String compensation = htmlUrl + "compensation.html";
    public final static String danger = htmlUrl + "danger.html";
    public final static String fail = htmlUrl + "fail.html";
    public final static String no_unlock = htmlUrl + "no_unlock.html";
    public final static String publicurl = htmlUrl + "public.html";
    public final static String recharge = htmlUrl + "recharge.html";
    public final static String reyurnRule = htmlUrl + "reyurnRule.html";
    public final static String Safety_insurance = htmlUrl + "Safety_insurance.html";
    public final static String Vip_refund = htmlUrl + "Vip_refund.html";

    public final static String contact_service = htmlUrl + "contact_service.html";
    public final static String noFindCar = htmlUrl + "noFindCar.html";
    public final static String real_name = htmlUrl + "real_name.html";
    public final static String recharge_HB = htmlUrl + "recharge_HB.html";
    public final static String rid_rule = htmlUrl + "rid_rule.html";
}
