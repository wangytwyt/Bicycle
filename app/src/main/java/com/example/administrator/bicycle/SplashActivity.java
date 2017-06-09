package com.example.administrator.bicycle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.SharedPreUtils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        SharedPreferences sp = getSharedPreferences(ContentValuse.dataBase, 0);
//       boolean isGuide = sp.getBoolean(ContentValuse.AppGuide,true);


        if (SharedPreUtils.sharedGet(this,ContentValuse.AppGuide,true)) {
            startActivity(new Intent(this, AppGuideActivity.class));
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
