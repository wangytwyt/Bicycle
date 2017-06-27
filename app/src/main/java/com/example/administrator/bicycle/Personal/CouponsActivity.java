package com.example.administrator.bicycle.Personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.CouponsInEntity;
import com.example.administrator.bicycle.entity.CouponsOutEntity;
import com.example.administrator.bicycle.entity.TripInEntity;
import com.example.administrator.bicycle.entity.TripOutEntity;

import java.util.ArrayList;
import java.util.Date;

public class CouponsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        ListView listView = (ListView) findViewById(R.id.lv_coupons);
//        TextView textView = (TextView) findViewById(R.id.tv_text);
        CouponsInEntity inEntity = new CouponsInEntity();
        inEntity.setName("开锁体验补偿卷");
        inEntity.setDate(new Date(1000));
        inEntity.setMuch("0.5");
        inEntity.setType("仅限经典mobike车型使用");
        //实例化一个TripOutEntity
        CouponsOutEntity outEntity = new CouponsOutEntity();
        ArrayList<CouponsInEntity> list = new ArrayList<>();
        list.add(inEntity);
        outEntity.setList(list);



        //判空
        if (outEntity.getList() == null) {
            listView.setVisibility(View.GONE);
//            textView.setVisibility(View.VISIBLE);
        } else {
            //设置Adapter
            listView.setAdapter(new CouponsAdpater(CouponsActivity.this, outEntity.getList()));
            listView.setVisibility(View.VISIBLE);
//            textView.setVisibility(View.GONE);
        }
    }
}
