package com.example.administrator.bicycle.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/6/22.
 */

public class NetWorkStatus {
    public static  boolean NetWorkStatus(final Activity activity) {

        boolean netSataus = false;
        ConnectivityManager cwjManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        cwjManager.getActiveNetworkInfo();
        if (cwjManager.getActiveNetworkInfo() != null) {
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();

        }

        if (!netSataus) {

            AlertDialog.Builder b = new AlertDialog.Builder(activity).setTitle("没有可用的网络")

                    .setMessage("是否对网络进行设置?");

            b.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent mIntent = new Intent("/");

                    ComponentName comp = new ComponentName(

                            "com.android.settings",

                            "com.android.settings.WirelessSettings");

                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.VIEW");

                    activity.startActivityForResult(mIntent,0); // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写

                }

            }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();

                }

            }).show();

        }

        return netSataus;

    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static  boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {

                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
