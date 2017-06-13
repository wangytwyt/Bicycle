// IRemoteService.aidl
package com.sofi.smartlocker.ble.interfaces;

import com.sofi.smartlocker.ble.interfaces.IRemoteCallback;
// Declare any non-default types here with import statements

interface IRemoteService {

     boolean isBleEnable();

     void enableBle();

     void setHighMode();

     void setLowMode();

     void startBleScan();

     void stopBleScan();

     boolean isBleScaning();

     void connectLock(String address);

     void disconnectLock();

     void getParams(String key);

     void openLock(String key);

     void updateKey(String oldKey, String newkey);

     void registerCallback(IRemoteCallback callback);

     void unregisterCallback(IRemoteCallback callback);

}
