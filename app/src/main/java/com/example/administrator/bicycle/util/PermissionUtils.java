package com.example.administrator.bicycle.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.NoCopySpan;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class PermissionUtils {
    //相机权限
    private static final int CAMERA_REQUEST_CODE = 1;

    public static boolean checkPermissionCamera(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    public static boolean onRequestPermissionsResultCamera(Activity activity,int requestCode,
                                                  String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED) {

                return true;

            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                    Toast.makeText(activity, "相机权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    /**
     * 需要进行检测的权限数组
     */
    private  static String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
    };
    private static final int PERMISSON_REQUESTCODE = 0;
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private static boolean isNeedCheck = true;


    public static void checkPermissionneedPermissions(Activity actvity){
        if (isNeedCheck) {
            checkPermissions(actvity,needPermissions);
        }
    }



    /**
     * @para
     * @since 2.5.0
     */
    private static void checkPermissions(Activity actvity,String[] permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(actvity,permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(actvity,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }



    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private static List<String> findDeniedPermissions(Activity actvity,String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(actvity,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    actvity, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    public static void onRequestPermissionsResultPermissions(int requestCode,int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                 isNeedCheck = false;

            }
        }

    }


    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }




}
