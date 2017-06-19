package com.example.administrator.bicycle;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.bicycle.Personal.CertificationActivity;
import com.example.administrator.bicycle.Post.AccessNetwork;
import com.example.administrator.bicycle.Post.Juhe;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.RegularExpressionsUtils;
import com.example.administrator.bicycle.util.SharedPreUtils;
import com.example.administrator.bicycle.util.TimeUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdentityFragment extends Fragment {
    EditText name, IdCard;
    String SidCard;
    String token, phone, idcard;
    SharedPreferences sharedPreferences;
    String Sname;
    TextView tvTost;

    public IdentityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_identity, container, false);

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        idcard = sharedPreferences.getString("idcard", "");
        token = sharedPreferences.getString("token", "");
        phone = sharedPreferences.getString("phone", "");
        if (!idcard.equals("") && idcard != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        name = (EditText) view.findViewById(R.id.name);
        IdCard = (EditText) view.findViewById(R.id.IdCard);
        tvTost = (TextView) view.findViewById(R.id.tv_tost);

        Button button = (Button) view.findViewById(R.id.btn_return);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sname = name.getText().toString().trim();
                SidCard = IdCard.getText().toString().trim();
                if (!RegularExpressionsUtils.isIdnumber(SidCard)) {
                    tvTost.setText("输入身份证位数不足");
                    return;
                }

                if (SidCard.length() < 17) {

                    if (!RegularExpressionsUtils.isIdcard15(SidCard)) {
                        tvTost.setText("请输入正确的身份证信息");
                        return;
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
                    if (TimeUtils.isSuper12(SidCard, 15)) {
                        tvTost.setText("12岁以下禁止骑行");
                    } else {
                        //  new Thread(new AccessNetwork("GET", "http://apis.juhe.cn/idcard/index", "cardno=" + SidCard + "&&key=de2b95384b5271c741b58722a7412bd2", h, 007)).start();


                    }

                } else {
                    if (!RegularExpressionsUtils.isIdcard18(SidCard)) {
                        tvTost.setText("请输入正确的身份证信息");
                        return;
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
//                    if ((nowDate - date1) < 12) {

                    if (TimeUtils.isSuper12(SidCard, 18)) {
                        tvTost.setText("12岁以下禁止骑行");
                    } else {
                        //    new Thread(new AccessNetwork("GET", "http://apis.juhe.cn/idcard/index", "cardno=" + SidCard + "&&key=de2b95384b5271c741b58722a7412bd2", h, 007)).start();


                    }


                }


            }
        });


        return view;
    }


    class IdqueryAsyncTask extends AsyncTask<String, String, String> {
        private String IdCard;

        public IdqueryAsyncTask(String IdCard) {
            this.IdCard = IdCard;
        }

        @Override
        protected String doInBackground(String... params) {
            return Juhe.getRequest1(IdCard);
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject object = new JSONObject(s);
                if (object.getInt("error_code") == 0) {

//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("idcard", SidCard);
//                        editor.commit();

                    SharedPreUtils.editorPutBoolean(getActivity(), ContentValuse.idcard, true);
                    tvTost.setText("实名验证成功");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(getActivity(), "验证失败" + object.get("error_code") + ":" + object.get("reason"), Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {

            }
            super.onPostExecute(s);
        }
    }


}
