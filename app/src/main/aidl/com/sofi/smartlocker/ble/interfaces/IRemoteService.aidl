// IRemoteService.aidl
package com.sofi.smartlocker.ble.interfaces;

import com.sofi.smartlocker.ble.interfaces.IRemoteCallback;
// Declare any non-default types here with import statements

interface IRemoteService {

     boolean isBleEnable();

     void enableBle();

     void setHighMode();

     void setLowMode();

     void listenBleState(boolean listen);          //是否监听蓝牙打开关闭，并做相应
     
     void startBleScan();

     void stopBleScan();

     boolean isBleScaning();

     void connectLock(String address);

     void disconnectLock();

     void getLockStatus(double lon, double lat);

     void openLock(String accountId, int time, String keys, int encryptionKey);

     void getLockRecord();

     void delLockRecord(String tradeId);

     void registerCallback(IRemoteCallback callback);

     void unregisterCallback(IRemoteCallback callback);

}
