package com.example.administrator.bicycle.Post;

/**
 * Created by Administrator on 2017/6/29.
 */

public class Url {
    private  final static  String url = "http://192.168.1.163:8080";
    // private  final static  String url = "http://192.168.1.126:8080";





    // 车辆
    private final static String bidypath =url+"/heibike/tbike/";
    public final static  String CollectInformationUrl= bidypath+"findBikeByTfTime.do?";

}
