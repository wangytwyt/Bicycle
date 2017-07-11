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

import com.example.administrator.bicycle.Post.Juhe;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.IdcardUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.SharedPreUtils;
import com.example.administrator.bicycle.util.TimeUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdentityFragment extends Fragment {
    EditText name, IdCard;
    String SidCard;
    String token, phone;
    SharedPreferences sharedPreferences;
    String Sname;
    TextView tvTost;

    private boolean isidcard;

    public IdentityFragment() {
        // Required empty public constructor
    }

    private CustomProgressDialog dialog;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    Toast.makeText(getActivity(), "认证成功", Toast.LENGTH_SHORT).show();


                    break;

                case ContentValuse.failure:
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "认证失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_identity, container, false);

        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView tvtitle = (TextView) view.findViewById(R.id.tv_title);
        TextView text = (TextView) view.findViewById(R.id.tv_text);

        tvtitle.setText("身份证验证");

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getActivity().getSharedPreferences(ContentValuse.registered, Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        isidcard = sharedPreferences.getBoolean(ContentValuse.idcard, false);


        dialog = new CustomProgressDialog(getActivity());
        dialog.setMessage("认证中...");
        name = (EditText) view.findViewById(R.id.name);
        IdCard = (EditText) view.findViewById(R.id.IdCard);
        tvTost = (TextView) view.findViewById(R.id.tv_tost);

        Button button = (Button) view.findViewById(R.id.btn_return);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sname = name.getText().toString().trim();
                SidCard = IdCard.getText().toString().trim();
                if (!IdcardUtils.validateCard(SidCard)) {
                    tvTost.setText("输入身份证号不合法");
                    return;
                }

                if (SidCard.length() < 17) {

                    if (!IdcardUtils.validateIdCard15(SidCard)) {
                        tvTost.setText("请输入正确的身份证信息");
                        return;
                    } else {
                        tvTost.setText("");
                    }


                    if (TimeUtils.isSuper12(SidCard, 15)) {
                        tvTost.setText("12岁以下禁止骑行");
                    } else {

                        realnameAuthentication(SidCard, Sname);
                    }

                } else {
                    if (!IdcardUtils.validateIdCard18(SidCard)) {
                        tvTost.setText("请输入正确的身份证信息");
                        return;
                    } else {
                        tvTost.setText("");
                    }


                    if (TimeUtils.isSuper12(SidCard, 18)) {
                        tvTost.setText("12岁以下禁止骑行");
                    } else {
                        realnameAuthentication(SidCard, Sname);
                    }


                }


            }
        });


        return view;
    }

    //实名验证
    private void realnameAuthentication(String SidCard, String name) {


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.lin_one, new RegisteredSuccessFragement());
        transaction.commit();
        if (!NetWorkStatus.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("T_USERIDCARD", SidCard);
        map.put("T_NAME", name);

        HttpUtils.doPost(Url.IdentityUrl, map, new Callback() {
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
                        if (response.equals("02")) {
                            mhandler.sendEmptyMessage(ContentValuse.success);
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
                }


            }
        });

    }





}
