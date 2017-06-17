package com.example.administrator.bicycle.Personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Post.AccessNetwork;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;

public class XiugaiNameActivity extends Activity {
    EditText name;

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
      //  getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_xiugai_name);


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("个人信息");

        TextView tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setVisibility(View.VISIBLE);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        name = (EditText) findViewById(R.id.edt_name);


        findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
            }
        });

        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    private void submit() {


        nameT = name.getText().toString().trim();

        Intent intent = new Intent();
        intent.putExtra(ContentValuse.nickname,nameT);

        setResult(1,intent);
        finish();






















        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        final String token = sharedPreferences.getString("token", "");
        final String phone = sharedPreferences.getString("phone", "");

        new Thread(new AccessNetwork("POST", "http://42.159.113.21/heibike/user/nicheng.mvc", "vip_nicheng=" + nameT + "&&vip_phone=" + phone + "&&vip_token=" + token, h, 006)).start();
    }

}
