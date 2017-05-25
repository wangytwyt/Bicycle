package com.example.administrator.bicycle.Personal.yonhuxieyi;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bicycle.Post.AccessNetwork;
import com.example.administrator.bicycle.R;

public class YhxyActivity extends AppCompatActivity {
    ProgressDialog dialog;
    Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0001) {
                String result = (String) msg.obj;
                TextView one = (TextView) findViewById(R.id.tv_one);
                one.setText(result);
                Log.d("", result);
                dialog.cancel();
            }


            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_yhxy);

        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.btn_return);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        dialog = new ProgressDialog(this);
        dialog.setMessage("请稍等");
        dialog.show();

        new Thread(new AccessNetwork("POST", "http://42.159.113.21/heibike/user/xieyi", "", h, 0001)).start();


    }
}
