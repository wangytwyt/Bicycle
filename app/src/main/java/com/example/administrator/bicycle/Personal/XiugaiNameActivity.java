package com.example.administrator.bicycle.Personal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.bicycle.Post.AccessNetwork;
import com.example.administrator.bicycle.R;

public class XiugaiNameActivity extends AppCompatActivity {
    EditText name;
    Button xiugai;
    String nameT;
    SharedPreferences sharedPreferences;
    Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 006) {

                String result = (String) msg.obj;
                Toast.makeText(XiugaiNameActivity.this, result, Toast.LENGTH_SHORT).show();

                if (!result.equals("") && result != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nicheng", nameT);
                    editor.commit();
                }

            }


            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugai_name);

        name = (EditText) findViewById(R.id.edt_name);
        xiugai = (Button) findViewById(R.id.btn_xiugai);

        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameT = name.getText().toString();

                //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
                sharedPreferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
                // 使用getString方法获得value，注意第2个参数是value的默认值
                final String token = sharedPreferences.getString("token", "");
                final String phone = sharedPreferences.getString("phone", "");

                new Thread(new AccessNetwork("POST", "http://42.159.113.21/heibike/user/nicheng.mvc", "vip_nicheng=" + nameT + "&&vip_phone=" + phone + "&&vip_token=" + token, h, 006)).start();
            }
        });


    }
}
