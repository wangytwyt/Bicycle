package com.example.administrator.bicycle.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/6/9.
 */

public class SharedPreUtils {

    public static void sharedPut(Context context, String name, boolean valuse) {
        SharedPreferences sp = getSharedPreferences(context,ContentValuse.dataBase);
       // context.getSharedPreferences(ContentValuse.dataBase, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, valuse);
        editor.commit();
    }

    public static boolean sharedGet(Context context, String name, boolean valuse) {
        SharedPreferences sp = context.getSharedPreferences(ContentValuse.dataBase, 0);
        return sp.getBoolean(name, valuse);
    }

    public static  SharedPreferences getSharedPreferences(Context context,String name){
        return context.getSharedPreferences(name, 0);
    }


    public static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.edit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return  getSharedPreferences(context,ContentValuse.registered);
       // return context.getSharedPreferences(ContentValuse.registered, Context.MODE_PRIVATE);
    }

    public static void editorPutBoolean(Context context,String key,boolean is) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, true);
        editor.commit();
    }
}
