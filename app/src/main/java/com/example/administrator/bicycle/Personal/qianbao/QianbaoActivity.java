package com.example.administrator.bicycle.Personal.qianbao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.administrator.bicycle.ComplaintsActivity;
import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.PrizeDialogActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.pay.alipay.PayResult;
import com.example.administrator.bicycle.pay.alipay.util.OrderInfoUtil2_0;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.EndTripDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.RefundDialog;
import com.sofi.smartlocker.ble.util.LOG;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class QianbaoActivity extends AppCompatActivity {
    private ImageView iv_weixin, iv_zhifubao;
    private RadioGroup rg_one, rg_two;
    private Boolean changeedGroup = false;
    private int heibiNumber;

    private double rmb, heibi;

    private TextView tv_rmb, tv_heibi;

  //  private CustomProgressDialog dialog;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
         //   dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    tv_rmb.setText(rmb+" 元");
                    tv_heibi.setText(heibi+"  枚");
                    break;

                case ContentValuse.failure:

                    Toast.makeText(QianbaoActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private Handler mHandlers = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(QianbaoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(QianbaoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            }
        };
    };


    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016080500170834";
    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088721404851479";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgoUwZHqSch/hZXEjIsLeOjTN2pBO7epXkqnO5nVWeDglS9UhW1Vdl06inuoJ9+h849houjEHL3p5cRLjXzMiFM9VQ9WtUA2fS1GJXuTLqvg3NUlPo2CbwzBnaz1uhrf+TsivKYz/bBeE4Uwv3Ojr8pUK4WQVrVl3JVPa8Im0ps/rSLQI2ZP8w8HqScXxLRnHzwgyynVSyTseIjVJcMYHp9MXlCcP7prlxvVi4IxRweU29BQqBDbYO8zvSRoscQ0jIyZKt7Ky0gk7WHkVxlEazlsUi+Ur1iqSE7Pe3nNDhu8jJxVgRnROXjbHCzE8xz65Ou+9Gm0Q9UKQ4FxEsBuCfAgMBAAECggEBAJIQWix1auZT1gccHasw1pkv2N5HcRIH9ImMXdMpOJOS2/Oty2v6b5DOmQx8UvDPItkfIMu3CpWiB4qLizHgq25EpfMKV+B7gEt8iTiP1bcTv2U3Ocn/09kfH1uTNxWcLU1WtO9CtOwrOpOHINhdAQqAFF2hxYXHcV0Ef6CwBWfnJkfDMfV3ndijdgL3VSvpnPqeSmfR0QPs1/9+plmnuXJjPpap+MqAf1IXTGI1RvCgwtSWgDH/8sPV4ixxuDhyS7EtoA04MesRKFtAtMNzZkrCM3HxoogSkZ0FrENeSrerT9wLu0bkaKvnKT8KG25WpRJOGdecU1vN5U+CRkDlIJECgYEA7GIOhPKfz+Vy4f5dVbhiMkOR6BkQIpWHLcW+E4D2OHxXb68+8ypzoPVHItXsywuv4UDy9lj2ByomLn+ZWGhr0re3GUBQrt/hrDpqsy6LaYqBumgICau/7SHP+m2dUs26AcJl8vsq3CPW8acMcWUTJIJi9DSlazJhQSzf/4BN5mMCgYEArfXfkz9Y8850DoL+O7ZI+txpVy+XjbCjtK9oQG1jq6vQWUTiks1LJNjGQVItcyGng/wwgaCAV1bKMOhMJV4qURUqJaqSxs4t49dqizUkOpVbvYA7dR8wzTGuVZimiTrkTVm/2Hod/NtafYPa5tg7RgiCM7TT/kcPuGt3lVji45UCgYASWR4lZavP+FBtiE/4WmKM4j/OEzv0Y0Tl4aZ6XxU9lzE2QxUQzOfCw6cs5gwGSrbxNhhl1MvsDHKAQ3lEqn5MEeSNejeJC2m586Lam69Ilu6w6GhPT9yK7If3OVDv33YjstI1BRfzlHiXs0688oqE1/YUXI3Gf1ebNn+AlpX1XwKBgAvEUuAx68FqGJ2WrL27Qcp+OD3mgTjbVud06fdZruFA2PIA39aQPmH6q8ZlWZ+tChi6aUmLYsMFot2WCPiKGxCq/tPXauA5uA7uVk2eRZoU2kTCNMNiXbmzf5hYoAMuUZ0N0hVBhV/CMqfE0HzG6ZN+Jdh7hc9wXRbbZhZbEdXVAoGAbltZAIBRoYvMQOnpfTXKDYWyWkFOFQIL1CSNE++3JWZlrT7lBPg07eks+XLD/e+XPBbMW+jpv7IWBhq/SwKpRHCpZ4IHg8ILUp7rQAuJukCIuKAKZ063pvbuZ34S18ZEMhLDXsFFgNLpRZUjwxaVTDUYgvCHvW4tRPrcdxkZThs=" ;
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_qianbao);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("我的钱包");

        iv_weixin = (ImageView) findViewById(R.id.iv_weinxin);
        iv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);

        tv_rmb = (TextView) findViewById(R.id.tv_rmb);
        tv_heibi = (TextView) findViewById(R.id.tv_heibi);


//        dialog = CustomProgressDialog.createDialog(this);
//        dialog.setMessage("   加载中...");

        rg_one = (RadioGroup) findViewById(R.id.rg_one);
        rg_two = (RadioGroup) findViewById(R.id.rg_two);
        rg_one.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        rg_one.check(R.id.rb_30);
        rg_two.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        iv_weixin.setSelected(true);
        findViewById(R.id.tv_refund).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefundDialog.Builder builder = new RefundDialog.Builder(QianbaoActivity.this);
                builder.setMessage("尊贵的会员，您还有"+18+"枚嘿币没有用完，退了年费就不能用车了，请您考虑一下哦~");
                builder.setPositiveButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        Toast.makeText(QianbaoActivity.this,"哇啊傻傻的发呆",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton(
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });

        findViewById(R.id.ll_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iv_weixin.isSelected()) {
                    iv_weixin.setSelected(false);
                    iv_zhifubao.setSelected(true);
                } else {
                    iv_weixin.setSelected(true);
                    iv_zhifubao.setSelected(false);
                }
            }
        });

        findViewById(R.id.ll_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (iv_zhifubao.isSelected()) {
                    iv_zhifubao.setSelected(false);
                    iv_weixin.setSelected(true);
                } else {
                    iv_zhifubao.setSelected(true);
                    iv_weixin.setSelected(false);
                }
            }
        });


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iv_zhifubao.isSelected()) {
                   payV2();
                }
                if (iv_weixin.isSelected()) {
                    weixinPay();
                }
            }
        });
        getRMB();
    }


    private void getRMB() {
        if (!NetWorkStatus.isNetworkAvailable(QianbaoActivity.this)) {
            Toast.makeText(QianbaoActivity.this, "网络不可用，请设置网络！", Toast.LENGTH_SHORT).show();
            return;
        }
       // dialog.show();
        HttpUtils.doGet(Url.getRMB + MyApplication.user.getT_USERPHONE(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandler.sendEmptyMessage(ContentValuse.failure);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String userjson = response.body().string();
                        JSONObject jsonObject = new JSONObject(userjson);
                        String result = jsonObject.getString("result");
                        if (result.equals("02")) {
                            JSONObject js = jsonObject.getJSONObject("pd");
                            String srmb = js.getString("T_RMB");
                            rmb = stringtodouble(srmb);
                            String sheibi = js.getString("T_HEIBI");
                            heibi = stringtodouble(sheibi);

                            mhandler.sendEmptyMessage(ContentValuse.success);
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }

                    } catch (Exception e) {
                        mhandler.sendEmptyMessage(ContentValuse.failure);
                        e.printStackTrace();
                    }
                } else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
                }
            }
        });


    }

    private double stringtodouble(String srmb) {
        if (srmb.equals("null")) {
            return 0.0;
        } else {
            return Double.valueOf(srmb);
        }
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(QianbaoActivity.this);
                LOG.E("------",orderInfo);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandlers.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void weixinPay() {

    }

    class MyRadioGroupOnCheckedChangedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (!changeedGroup) {
                changeedGroup = true;
                if (group == rg_one) {
                    rg_two.clearCheck();
                } else if (group == rg_two) {
                    rg_one.clearCheck();
                }
                changeedGroup = false;
            }

            switch (checkedId) {
                case R.id.rb_20:
                    heibiNumber = 20;
                    break;
                case R.id.rb_30:
                    heibiNumber = 30;
                    break;
                case R.id.rb_50:
                    heibiNumber = 50;
                    break;
                case R.id.rb_80:
                    heibiNumber = 80;
                    break;
                case R.id.rb_100:
                    heibiNumber = 100;
                    break;
                case R.id.rb_200:
                    heibiNumber = 200;
                    break;
            }

        }
    }
}
