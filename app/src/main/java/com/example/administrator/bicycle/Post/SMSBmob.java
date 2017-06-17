package com.example.administrator.bicycle.Post;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.bicycle.Personal.SetPhoneActivity;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;

/**
 * Created by Administrator on 2017/6/17.
 */

public class SMSBmob {
    public static void requestSMSCode(final Context context,String phone) {
        BmobSMS.requestSMSCode(context, phone, "验证码", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//验证码发送成功
                    Toast.makeText(context, "验证码发送成功，请稍等", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情

                }else {
                    Toast.makeText(context, "短信发送失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
