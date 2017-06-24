package com.example.administrator.bicycle;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.SharedPreUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {
    private ImageView iv_zhifubao, iv_weixin, iv_priceone, iv_pricetwo;

    public DepositFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);

        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView tvtitle = (TextView) view.findViewById(R.id.tv_title);
        TextView text = (TextView) view.findViewById(R.id.tv_text);


        TextView tv_teon = (TextView) view.findViewById(R.id.tv_teon);
        tv_teon.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
        TextView tv_tetw = (TextView) view.findViewById(R.id.tv_tetw);
        tv_tetw.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );

        tvtitle.setText("充值");

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        //    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ContentValuse.registered, Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值

        boolean vipuser = SharedPreUtils.getSharedPreferences(getActivity()).getBoolean(ContentValuse.vipuser, false);
        if (vipuser) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.lin_one, new IdentityFragment());
            transaction.commit();
        }


        iv_weixin = (ImageView) view.findViewById(R.id.iv_weixin);
        iv_zhifubao = (ImageView) view.findViewById(R.id.iv_zhifubao);
        iv_priceone = (ImageView) view.findViewById(R.id.iv_priceone);
        iv_pricetwo = (ImageView) view.findViewById(R.id.iv_pricetwo);
        iv_weixin.setSelected(true);
        iv_pricetwo.setSelected(true);
        view.findViewById(R.id.rl_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_pricetwo.setSelected(false);
                if (iv_priceone.isSelected()) {
                    iv_priceone.setSelected(false);
                } else {
                    iv_priceone.setSelected(true);
                }
            }
        });

        view.findViewById(R.id.rl_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_priceone.setSelected(false);
                if (iv_pricetwo.isSelected()) {
                    iv_pricetwo.setSelected(false);
                } else {
                    iv_pricetwo.setSelected(true);
                }
            }
        });


        view.findViewById(R.id.ll_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zhifubao.setSelected(false);
                if (iv_weixin.isSelected()) {
                    iv_weixin.setSelected(false);
                } else {
                    iv_weixin.setSelected(true);
                }
            }
        });

        view.findViewById(R.id.ll_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_weixin.setSelected(false);

                if (iv_zhifubao.isSelected()) {
                    iv_zhifubao.setSelected(false);

                } else {
                    iv_zhifubao.setSelected(true);

                }
            }
        });


        // Inflate the layout for this fragment
        Button button = (Button) view.findViewById(R.id.btn_return);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences.Editor editor = SharedPreUtils.getEditor(getActivity());
//                editor.putBoolean(ContentValuse.vipuser, true);
//                editor.commit();
                SharedPreUtils.editorPutBoolean(getActivity(), ContentValuse.vipuser, true);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.lin_one, new IdentityFragment());
                transaction.commit();

            }
        });


        return view;


    }

}
