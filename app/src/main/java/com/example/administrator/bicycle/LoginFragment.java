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
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.entity.OutLoginEntity;
import com.example.administrator.bicycle.entity.User;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.SharedPreUtils;
import com.google.gson.JsonObject;
import com.sofi.smartlocker.ble.util.LOG;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.SMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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

    View view;

    private CustomProgressDialog dialog;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView tvtitle = (TextView) view.findViewById(R.id.tv_title);
        TextView text = (TextView) view.findViewById(R.id.tv_text);

        tvtitle.setText("手机验证");


        //利用Handler更新UI
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dialog.dismiss();
                switch (msg.what) {
                    case ContentValuse.success:
                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();

                        if (MyApplication.user.getT_TRRMB() < 0) {
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.lin_one, new DepositFragment());
                            transaction.commit();
                        } else if (MyApplication.user.getT_NAME().equals("")) {
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.lin_one, new IdentityFragment());
                            transaction.commit();
                        }


                        break;

                    case ContentValuse.failure:

                        Toast.makeText(getActivity(), "验证失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }


        };


        //初始化控件
        init();
        edtPhoneNum.addTextChangedListener(watcher);//设置隐藏显示
        edtValidation.addTextChangedListener(watcher);




        view.findViewById(R.id.tv_yhxy).setOnClickListener(this);
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
        tvYy = (TextView) view.findViewById(R.id.tv_yy);
        tvYy.setOnClickListener(this);
        imgDel = (ImageView) view.findViewById(R.id.img_del);//删除图标
        obtain.setOnClickListener(this);
        start.setOnClickListener(this);
        imgDel.setOnClickListener(this);


        dialog = CustomProgressDialog.createDialog(getActivity());;
        dialog.setMessage("    验证中...   ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_obtain://获取验证码
                //获取用户填写的电话号码
                if (!NetWorkStatus.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getContext(), "网络不可用，请设置网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                PhoneNum = edtPhoneNum.getText().toString();
                if (PhoneNum.equals("")) {
                    Toast.makeText(getContext(), "电话号为空，请输入电话号！", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                if (!NetWorkStatus.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getContext(), "网络不可用，请设置网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 获取用户填写的电话号码
               final String phoneN = edtPhoneNum.getText().toString();
//                //获取用户填写的验证码
//                String validation = edtValidation.getText().toString();
//                if (validation.equals("")) {
//                    Toast.makeText(getContext(), "验证码为空，请输入验证码！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (phoneN.equals("")) {
//                    Toast.makeText(getContext(), "电话号为空，请输入电话号！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                dialog.show();
//                BmobSMS.verifySmsCode(getActivity(), phoneN, validation, new VerifySMSCodeListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if (e == null) {//短信验证码已验证成功
//                            //发送post请求Login.do?T_USERPHONE= & AREA=
//                            Map<String, String> map = new HashMap<String, String>();
//                            map.put("T_USERPHONE", phoneN);
//                            map.put("AREA", MyApplication.city);
//                            HttpUtils.doPost(Url.loginUrl, map, new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    h.sendEmptyMessage(ContentValuse.failure);
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//                                    if (response.isSuccessful()) {
//                                        String userjson = response.body().string();
//
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(userjson);
//                                            String result = jsonObject.getString("result");
//                                            if (result.equals("02")) {
//                                                JSONObject js = jsonObject.getJSONObject("pd");
//
//                                                User user = new User();
//                                                String rmb = js.getString("T_RMB");
//                                                if (rmb.equals("null")) {
//                                                    user.setT_TRRMB(-1);
//                                                } else {
//                                                    user.setT_TRRMB(Integer.valueOf(rmb));
//                                                }
//
//                                                user.setT_USERPHONE(js.getString("T_USERPHONE"));
//                                                String userName = js.getString("T_USERNAME");
//                                                if (userName.equals("null")) {
//                                                    userName = "";
//                                                }
//                                                user.setT_USERNAME(userName);
//
//                                                String name = js.getString("T_NAME");
//                                                if (name.equals("null")) {
//                                                    name = "";
//                                                }
//                                                user.setT_NAME(name);
//                                                String T_USERIMAGE = js.getString("T_USERIMAGE");
//                                                if (T_USERIMAGE.equals("null")) {
//                                                    T_USERIMAGE = "";
//                                                }
//                                                user.setT_USERIMAGE(T_USERIMAGE);
//                                                user.setTUSER_ID(Integer.valueOf(js.getString("TUSER_ID")));
//                                                user.setT_SIGN(js.getInt("T_SIGN"));
//                                                MyApplication.user = user;
//                                                h.sendEmptyMessage(ContentValuse.success);
//                                            } else {
//                                                h.sendEmptyMessage(ContentValuse.failure);
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            h.sendEmptyMessage(ContentValuse.failure);
//                                        }
//                                    }
//                                }
//                            });
//
//
//                        } else {
//                            h.sendEmptyMessage(ContentValuse.failure);
//                        }
//                    }
//                });

                Map<String, String> map = new HashMap<String, String>();
                map.put("T_USERPHONE", "17829173776");
                map.put("AREA", MyApplication.city);
                HttpUtils.doPost(Url.loginUrl, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        h.sendEmptyMessage(ContentValuse.failure);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String userjson = response.body().string();

                            try {
                                JSONObject jsonObject = new JSONObject(userjson);
                                String result = jsonObject.getString("result");
                                if (result.equals("02")) {
                                    JSONObject js = jsonObject.getJSONObject("pd");

                                    User user = new User();
                                    String rmb = js.getString("T_RMB");
                                    if (rmb.equals("null")) {
                                        user.setT_TRRMB(-1);
                                    } else {
                                        user.setT_TRRMB(Integer.valueOf(rmb));
                                    }

                                    user.setT_USERPHONE(js.getString("T_USERPHONE"));
                                    String userName = js.getString("T_USERNAME");
                                    if (userName.equals("null")) {
                                        userName = "";
                                    }
                                    user.setT_USERNAME(userName);

                                    String name = js.getString("T_NAME");
                                    if (name.equals("null")) {
                                        name = "";
                                    }
                                    user.setT_NAME(name);
                                    String T_USERIMAGE = js.getString("T_USERIMAGE");
                                    if (T_USERIMAGE.equals("null")) {
                                        T_USERIMAGE = "";
                                    }
                                    user.setT_USERIMAGE(T_USERIMAGE);
                                    user.setTUSER_ID(Integer.valueOf(js.getString("TUSER_ID")));
                                    user.setT_SIGN(js.getInt("T_SIGN"));
                                    MyApplication.user = user;
                                    h.sendEmptyMessage(ContentValuse.success);
                                } else {
                                    h.sendEmptyMessage(ContentValuse.failure);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                h.sendEmptyMessage(ContentValuse.failure);
                            }
                        }
                    }
                });
                break;
            case R.id.tv_yhxy:

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra(ContentValuse.url, Url.url + "/yhxy.html");
                getActivity().startActivity(intent);


                break;
            case R.id.img_del:
                edtPhoneNum.setText("");
                break;
            case R.id.tv_yy:
                Random random = new Random();
                int yzm = random.nextInt(8999) + 1000;
                new Thread(new AccessNetwork("POST", "http://op.juhe.cn/yuntongxun/voice", "valicode=" + yzm + "&to=13020778812&playtimes=&key=50a8f12a35991688610e0ca0490684ba&dtype=", h, 004)).start();
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
