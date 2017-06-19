package com.example.administrator.bicycle;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.bicycle.Post.AccessNetwork;
import com.example.administrator.bicycle.entity.OutLoginEntity;
import com.example.administrator.bicycle.entity.User;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.SharedPreUtils;

import java.util.Random;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.SMSCodeListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private Button obtain;//获取验证码
    private Button start;//登录
    private EditText edtPhoneNum;//手机号输入框
    private EditText edtValidation;//验证码输入框
    private ImageView imgDel;//删除图标
    private Handler h;
    TextView tvYy;//语音验证码
    private String PhoneNum;//电话号码
    ProgressDialog dialog;
    View view;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        View title = view.findViewById(R.id.title);
        LinearLayout finish1 = (LinearLayout) title.findViewById(R.id.finish1);
        finish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
  //      SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ContentValuse.registered, Activity.MODE_PRIVATE);

        // 使用getString方法获得value，注意第2个参数是value的默认值
     //   String token = sharedPreferences.getString(ContentValuse.token, "");

        String token = SharedPreUtils.getSharedPreferences(getActivity()).getString(ContentValuse.token, "");
        if (!token.equals("") && token != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.lin_one, new DepositFragment());
            transaction.commit();
        }


        dialog = new ProgressDialog(getContext());

        //利用Handler更新UI
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 003) {
                    String result = (String) msg.obj;
                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();


                        SharedPreferences.Editor editor = SharedPreUtils.getEditor(getActivity());

                        editor.putString(ContentValuse.token, "sdjahkjsdlksdjngvsdkljhjgsaodojglskdfkjugvlszdo");

                        //提交业务
                        editor.commit();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.lin_one, new DepositFragment());
                        transaction.commit();

                        //提示登录成功或者
                        Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();

                        //关闭弹出框
                        dialog.cancel();

                    } else {

                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();

                    }


                }


        };


        //初始化控件
        init();
        edtPhoneNum.addTextChangedListener(watcher);//设置隐藏显示
        edtValidation.addTextChangedListener(watcher);


        return view;
    }


    /*
  *  初始化控件
  */
    protected void init() {
        obtain = (Button) view.findViewById(R.id.btn_obtain);//获取验证码
        start = (Button) view.findViewById(R.id.btn_start);//登录
        edtPhoneNum = (EditText) view.findViewById(R.id.edt_PhoneNum);//手机号输入框
        edtValidation = (EditText) view.findViewById(R.id.edt_validation);//验证码输入框
//        tvYy = (TextView) view.findViewById(R.id.tv_yy);
//        tvYy.setOnClickListener(this);
        imgDel = (ImageView) view.findViewById(R.id.img_del);//删除图标
        obtain.setOnClickListener(this);
        start.setOnClickListener(this);
        imgDel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_obtain://获取验证码
                //获取用户填写的电话号码
                PhoneNum = edtPhoneNum.getText().toString();
                //发送短信
                BmobSMS.requestSMSCode(getContext(), PhoneNum, "验证码", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException ex) {
                        if (ex == null) {//验证码发送成功
                            Toast.makeText(getContext(), "短信发送成功", Toast.LENGTH_SHORT).show();//用于查询本次短信发送详情
                            //设置计时器
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    obtain.setEnabled(false);
                                    long a = millisUntilFinished / 1000;
                                    obtain.setText("重新获取(" + a + "s)");
                                }

                                @Override
                                public void onFinish() {
                                    obtain.setEnabled(true);
                                    obtain.setText("重新获取");
                                }
                            }.start();

                        } else {
                            Toast.makeText(getContext(), "短信发送失败，请稍后再试", Toast.LENGTH_SHORT).show();//用于查询本次短信发送详情
                        }
                    }
                });

                break;
            case R.id.btn_start:
                //获取用户填写的电话号码
                PhoneNum = edtPhoneNum.getText().toString();
                //获取用户填写的验证码
                String validation = edtValidation.getText().toString();

//                BmobSMS.verifySmsCode(LoginActivity.this, PhoneNum, validation, new VerifySMSCodeListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if (e == null) {//短信验证码已验证成功
//                            //发送post请求
//
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));//跳转
//                        } else {
//                            Toast.makeText(LoginActivity.this, "验证失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                dialog.setMessage("正在登陆，请稍后~");
                dialog.setCancelable(false);
                dialog.show();

                h.sendEmptyMessageDelayed(003,4000);

    //          new Thread(new AccessNetwork("POST", "http://42.159.113.21/heibike/user/check", "vip_phone=" + PhoneNum + "&vip_token=" + validation, h, 003)).start();


                break;
            case R.id.img_del:
                edtPhoneNum.setText("");
                break;
//            case R.id.tv_yy:
//                Random random = new Random();
//                int yzm = random.nextInt(8999) + 1000;
//                new Thread(new AccessNetwork("POST", "http://op.juhe.cn/yuntongxun/voice", "valicode=" + yzm + "&to=13020778812&playtimes=&key=50a8f12a35991688610e0ca0490684ba&dtype=", h, 004)).start();
//                break;
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
            String validation = edtValidation.getText().toString();
            String num = edtPhoneNum.getText().toString();
            if (num.length() == 11) {
                obtain.setEnabled(true);
            } else {
                obtain.setEnabled(false);
            }

            if (validation.length() >= 4 && validation.length() <= 6) {
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            }
        }
    };

}
