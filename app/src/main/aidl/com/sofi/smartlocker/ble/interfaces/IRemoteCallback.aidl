// IRemoteCallback.aidl
package com.sofi.smartlocker.ble.interfaces;

// Declare any non-default types here with import statements

interface IRemoteCallback {

     void bleSupportFeature(boolean isFeature);    //是否支持蓝牙4.0：true支持,false不支持

     void bleScanResult(String name, String address, int rssi);    //扫描到的蓝牙地址

     void bleStatus(boolean status, String address);    //蓝牙连接状态：false关闭，true连接

     void bleCmdError(int cmd, String msg);    //读取锁状态、开锁、读取交易记录、删除交易记录命令失败回调，cmd参考CmdUtil, msg错误信息

     void bleGetBike(String version, String keySerial, String mac, String vol);    //读取锁状态成功的回调

     void bleGetRecord(String phone, String bikeTradeNo, String timestamp, String transType,
                                           String mackey, String index, String Cap, String Vol);

     void bleCmdReply(int cmd);    //开锁、读取交易记录的成功回调

}
