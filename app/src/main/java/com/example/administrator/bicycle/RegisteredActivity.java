package com.example.administrator.bicycle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegisteredActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);





//        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
//        SharedPreferences sharedPreferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
//        // 使用getString方法获得value，注意第2个参数是value的默认值
//        String token = sharedPreferences.getString("token", "");
//        int vipuser = sharedPreferences.getInt("vipuser", 0);
//        String idcard = sharedPreferences.getString("idcard", "");
//
//        if (!token.equals("") && token != null&&!idcard.equals("") && idcard != null) {
//            Intent intent=new Intent(RegisteredActivity.this,HomeActivity.class);
//            startActivity(intent);
//            RegisteredActivity.this.finish();
//        }

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.lin_one,new LoginFragment());
        transaction.commit();


    }
}
