package com.example.administrator.bicycle;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {


    public DepositFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        int vipuser = sharedPreferences.getInt("vipuser", 0);
        if (vipuser==1){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.lin_one, new IdentityFragment());
            transaction.commit();
        }




        // Inflate the layout for this fragment
        Button button = (Button) view.findViewById(R.id.btn_return);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.lin_one,new IdentityFragment());
                transaction.commit();

            }
        });


        return view;


    }

}
