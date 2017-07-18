package com.example.administrator.bicycle.Personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.HeadImagePop;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.IdcardUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;

import org.json.JSONObject;

import java.io.IOException;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdatePhoneActivity extends Activity implements View.OnClickListener {
    private EditText et_new, et_newauthcode;
    private TextView et_old;
    private Button but_certification, tv_newauthcode;
    private CustomProgressDialog dialog;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    Toast.makeText(UpdatePhoneActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    MyApplication.user.setT_USERPHONE(et_new.getText().toString().trim());


                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();




                    break;
                case ContentValuse.failure:

                    Toast.makeText(UpdatePhoneActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                    break;
                case ContentValuse.other:

                    Toast.makeText(UpdatePhoneActivity.this, "此电话号已被绑定", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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
        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("  绑定中...  ");

        et_old = (TextView) findViewById(R.id.et_old);
        et_old.setText(" 当前手机号：" + MyApplication.user.getT_USERPHONE());

        et_new = (EditText) findViewById(R.id.et_new);
        et_newauthcode = (EditText) findViewById(R.id.et_newauthcode);

        tv_newauthcode = (Button) findViewById(R.id.tv_newauthcode);
        tv_newauthcode.setOnClickListener(this);

        but_certification = (Button) findViewById(R.id.but_certification);
        but_certification.setOnClickListener(this);

        et_new.addTextChangedListener(watcher);//设置隐藏显示
        et_newauthcode.addTextChangedListener(watcher);

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

                BmobSMS.requestSMSCode(UpdatePhoneActivity.this, newphone, "验证码", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        // TODO Auto-generated method stub
                        if (ex == null) {//验证码发送成功
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    tv_newauthcode.setEnabled(false);
                                    long a = millisUntilFinished / 1000;
                                    tv_newauthcode.setText("重新获取(" + a + "s)");
                                }

                                @Override
                                public void onFinish() {
                                    tv_newauthcode.setEnabled(true);
                                    tv_newauthcode.setText("重新获取");
                                }
                            }.start();
                        } else {
                            Toast.makeText(UpdatePhoneActivity.this, "短信发送失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.but_certification:
                if (!NetWorkStatus.isNetworkAvailable(UpdatePhoneActivity.this)) {
                    Toast.makeText(UpdatePhoneActivity.this, "网络不可用，请设置网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 获取用户填写的电话号码
                final String phoneN = et_new.getText().toString();
                //获取用户填写的验证码
                String validation = et_newauthcode.getText().toString();
                if (validation.equals("")) {
                    Toast.makeText(UpdatePhoneActivity.this, "验证码为空，请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneN.equals("")) {
                    Toast.makeText(UpdatePhoneActivity.this, "电话号为空，请输入电话号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                BmobSMS.verifySmsCode(UpdatePhoneActivity.this, phoneN, validation, new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            HttpUtils.doGet(Url.updataPhone + MyApplication.user.getTUSER_ID() + "&T_USERPHONE=" + phoneN, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    mhandler.sendEmptyMessage(ContentValuse.failure);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String dd = response.body().string();
                                        try {
                                            JSONObject jsonObject = new JSONObject(dd);
                                            String result = jsonObject.getString("result");
                                            if (result.equals("02")) {
                                                mhandler.sendEmptyMessage(ContentValuse.success);
                                            } else if (result.equals("01")) {
                                                mhandler.sendEmptyMessage(ContentValuse.failure);
                                            } else if (result.equals("00")) {
                                                mhandler.sendEmptyMessage(ContentValuse.other);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            mhandler.sendEmptyMessage(ContentValuse.failure);
                                        }
                                    } else {
                                        mhandler.sendEmptyMessage(ContentValuse.failure);
                                    }
                                }
                            });
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }
                    }
                });
                break;

        }
    }

    /*
        * 监听输入框
        */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int starte, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String validation = et_newauthcode.getText().toString();
            String num = et_new.getText().toString();
            if (num.length() == 11) {
                tv_newauthcode.setEnabled(true);
            } else {
                tv_newauthcode.setEnabled(false);
            }

            if (validation.length() >= 4 && validation.length() <= 6) {
                but_certification.setEnabled(true);
            } else {
                but_certification.setEnabled(false);
            }
        }
    };

}
