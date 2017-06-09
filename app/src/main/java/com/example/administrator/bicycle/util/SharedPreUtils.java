package com.example.administrator.bicycle.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/6/9.
 */

public class SharedPreUtils {

 public static void sharedPut(Context context,String name,boolean valuse){
     SharedPreferences sp= context.getSharedPreferences(ContentValuse.dataBase, 0);
     SharedPreferences.Editor  editor = sp.edit();
     editor.putBoolean(name,valuse);
     editor.commit();
 }
    public static boolean sharedGet(Context context,String name,boolean valuse){
        SharedPreferences sp = context.getSharedPreferences(ContentValuse.dataBase, 0);
        return sp.getBoolean(name,valuse);
    }

}
