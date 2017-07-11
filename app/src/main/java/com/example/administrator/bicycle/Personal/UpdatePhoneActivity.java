package com.example.administrator.bicycle.Personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Post.SMSBmob;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.HeadImagePop;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.IdcardUtils;

public class UpdatePhoneActivity extends Activity implements View.OnClickListener {
    private EditText  et_new, et_newauthcode;
private TextView et_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_set_phone);

        init();

    }


    private void init() {

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("更改手机号");


        et_old = (TextView) findViewById(R.id.et_old);
        et_old.setText(" 当前手机号："+MyApplication.user.getT_USERPHONE());

        et_new = (EditText) findViewById(R.id.et_new);
        et_newauthcode = (EditText) findViewById(R.id.et_newauthcode);

        findViewById(R.id.tv_newauthcode).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_newauthcode:
                String newphone = et_new.getText().toString().trim();
                if (newphone.equals("")) {
                    Toast.makeText(UpdatePhoneActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!IdcardUtils.isMobileNO(newphone)){
                    Toast.makeText(UpdatePhoneActivity.this, "输入手机号不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                SMSBmob.requestSMSCode(this,newphone);
                break;

        }
    }


}
