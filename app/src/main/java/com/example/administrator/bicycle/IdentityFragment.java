package com.example.administrator.bicycle;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.IdcardUtils;
import com.example.administrator.bicycle.util.SharedPreUtils;
import com.example.administrator.bicycle.util.TimeUtils;

import org.json.JSONObject;


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

    private  boolean isidcard;

    public IdentityFragment() {
        // Required empty public constructor
    }


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
                    }else {
                        tvTost.setText("");
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

                     new IdqueryAsyncTask(SidCard).execute();
                    }

                } else {
                    if (!IdcardUtils.validateIdCard18(SidCard)) {
                        tvTost.setText("请输入正确的身份证信息");
                        return;
                    }else {
                        tvTost.setText("");
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

                    if (TimeUtils.isSuper12(SidCard, 18)) {
                        tvTost.setText("12岁以下禁止骑行");
                    } else {
                        //    new Thread(new AccessNetwork("GET", "http://apis.juhe.cn/idcard/index", "cardno=" + SidCard + "&&key=de2b95384b5271c741b58722a7412bd2", h, 007)).start();
                        SharedPreUtils.editorPutBoolean(getActivity(), ContentValuse.idcard, true);
                        tvTost.setText("实名验证成功");
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.lin_one, new RegisteredSuccessFragement());
                        transaction.commit();
                       // new IdqueryAsyncTask(SidCard).execute();
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
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.lin_one, new RegisteredSuccessFragement());
                    transaction.commit();

                } else {
                    Toast.makeText(getActivity(), "验证失败" + object.get("error_code") + ":" + object.get("reason"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
            super.onPostExecute(s);
        }
    }


}
