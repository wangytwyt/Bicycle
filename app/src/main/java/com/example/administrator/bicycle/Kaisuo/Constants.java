package com.example.administrator.bicycle.Kaisuo;

import com.sofi.smartlocker.ble.interfaces.IRemoteService;

/**
 * Created by lan on 2016/6/29.
 */
public class Constants {

    public static final String DEFAULT_CACHE_DIR = "smartlock";
    private static final String IP = "alabike.luopingelec.com";
    private static final String HTTP = "https://" + IP;
    public static final String URL = HTTP + "/alabike/ab_mapp";

    public static final String ACCOUNT = "bluelocktest";
    public static final String VERIFY_URL = "http://120.35.11.49:26969/OpenInterface/TowerCraneService.ashx";
    public static final int CONNECT_TIME_OUT = 5;
    public static final int SOCKET_TIME_OUT = 30;
    public static final int cachSize = 10*1024*1024;
    public static final int FILE_SOCKET_TIME_OUT = 10;
    public static final int PAGE_SIZE = 20;         //列表数据加载条数
    public static IRemoteService bleService;           //蓝牙服务

    public static final String IMAGE = "images";
    public static final String IMAGE_ORIGIN = "images_origin";
    public static final String IMAGE_FORMAT = ".png";
    public static final String AUDIO_ORIGIN = "audio";
    public static final String AUDIO_FORMAT = ".mp3";

}
