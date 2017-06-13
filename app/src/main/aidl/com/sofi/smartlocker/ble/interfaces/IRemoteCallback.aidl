// IRemoteCallback.aidl
package com.sofi.smartlocker.ble.interfaces;

// Declare any non-default types here with import statements

interface IRemoteCallback {

     void bleSupportFeature(boolean isFeature);    //是否支持蓝牙4.0：true支持,false不支持

     void bleScanResult(String name, String address, int rssi);    //扫描到的蓝牙地址

     void bleStatus(boolean status, String address);    //蓝牙连接状态：0:断开,1:连接,2:重连,3:连接异常,4:发现服务异常,5:开通主动通知异常，6连接超时

     void bleGetParams(String batteryVol, String solarVol, boolean open);    //锁参数、开关状态

     void bleCmdError(int cmd, String msg);    //所有命令失败回调，cmd参考CmdUtil, msg错误信息

     void bleCmdReply(int cmd);    //开锁、更改密码、关锁的成功回调

}
