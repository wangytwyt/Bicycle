package com.example.administrator.bicycle.Kaisuo;

/**
 * Created by heyong on 16/6/30.
 */
public class PUserLogin extends PData {

    public PUserLogin(String phone, String devicenum) {
        bodyAdd("phone", phone);
        bodyAdd("devicenum", devicenum);
    }

    @Override
    protected void method() {
        method = "UserLogin";
    }
}
