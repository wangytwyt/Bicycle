package com.example.administrator.bicycle.Personal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Post.Juhe;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.IdcardUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class CertificationActivity extends AppCompatActivity {
    private EditText ed_Certification, ed_name;
    private String cardID,name;
    private CustomProgressDialog dialog;
    private TextView tv_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_certification);
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
        tvtitle.setText("实名认证");


        ed_Certification = (EditText) findViewById(R.id.ed_Certification);
        ed_name = (EditText) findViewById(R.id.ed_name);

        dialog = CustomProgressDialog.createDialog(this);
        tv_error = (TextView) findViewById(R.id.tv_error);

        findViewById(R.id.but_certification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardID = ed_Certification.getText().toString().trim();
                name = ed_name.getText().toString().trim();

                if(cardID == null ||cardID.equals("")){
                    Toast.makeText(CertificationActivity.this,"请输入身份证号",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name == null ||name.equals("")){
                    Toast.makeText(CertificationActivity.this,"请输入名字",Toast.LENGTH_SHORT).show();
                    return;
                }

                    if (NetWorkStatus.isNetworkAvailable(CertificationActivity.this)) {
                        if (!IdcardUtils.validateCard(cardID)) {
                            tv_error.setText("输入身份证号不合法");
                            return;
                        }

                        if (cardID.length() < 17) {

                            if (!IdcardUtils.validateIdCard15(cardID)) {
                                tv_error.setText("请输入正确的身份证信息");
                                return;
                            } else {
                                tv_error.setText("");
                            }


//                        String SidCardT = SidCard.substring(6, 12);//年月日
//                        String SidCardT = SidCard.substring(6, 8);//年
//                        int date1 = Integer.parseInt("19" + SidCardT);
//                        //获取当前日期
//                        Date date = new Date();
//                        //设置显示格式
//                        SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy", Locale.CHINA);
//                        //转为String类型
//                        String SDate = fmtrq.format(date.getTime());
//                        //转为int
//                        int nowDate = Integer.parseInt(SDate);
                            //  if ((nowDate - date1) < 12) {
                            if (TimeUtils.isSuper12(cardID, 15)) {
                                tv_error.setText("12岁以下禁止骑行");
                            } else {
                                //  new Thread(new AccessNetwork("GET", "http://apis.juhe.cn/idcard/index", "cardno=" + SidCard + "&&key=de2b95384b5271c741b58722a7412bd2", h, 007)).start();
                                new CertificationAsty(cardID).execute();
                            }

                        } else {
                            if (!IdcardUtils.validateIdCard18(cardID)) {
                                tv_error.setText("请输入正确的身份证信息");
                                return;
                            } else {
                                tv_error.setText("");
                            }

//
//                    String SidCardT = SidCard.substring(6, 10);
//
//                    int date1 = Integer.parseInt(SidCardT);
//                    //获取当前日期
//                    Date date = new Date();
//                    //设置显示格式
//                    SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy", Locale.CHINA);
//                    //转为String类型
//                    String SDate = fmtrq.format(date.getTime());
//                    //转为int
//                    int nowDate = Integer.parseInt(SDate);
//
//
//                    if ((nowDate - date1) < 12) { tvTost.setText("请输入正确的身份证信息");

                            if (TimeUtils.isSuper12(cardID, 18)) {
                                tv_error.setText("12岁以下禁止骑行");
                            } else {
                                //    new Thread(new AccessNetwork("GET", "http://apis.juhe.cn/idcard/index", "cardno=" + SidCard + "&&key=de2b95384b5271c741b58722a7412bd2", h, 007)).start();
                                new CertificationAsty(cardID).execute();
                            }


                        }
                    } else {
                        Toast.makeText(CertificationActivity.this, "请设置网络！", Toast.LENGTH_SHORT).show();
                    }
                }



        });
    }


    class CertificationAsty extends AsyncTask<String, String, String> {
        private String id;

        public CertificationAsty(String id) {
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                dialog.dismiss();
                JSONObject object = new JSONObject(s);
                if (object.getInt("error_code") == 0) {
                    System.out.println(object.get("result"));
                    Toast.makeText(CertificationActivity.this, "验证成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra(ContentValuse.Realname, true);
                    setResult(1, intent);
                    CertificationActivity.this.finish();
                } else {
                    Toast.makeText(CertificationActivity.this, "验证失败" + object.get("error_code") + ":" + object.get("reason"), Toast.LENGTH_SHORT).show();
                    System.out.println(object.get("error_code") + ":" + object.get("reason"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            return Juhe.getRequest1(id);
        }
    }

}
