package com.example.administrator.bicycle.Personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.TripInEntity;
import com.example.administrator.bicycle.entity.TripOutEntity;

import java.util.ArrayList;
import java.util.Date;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_trip);
        //初始化控件
        ListView listView = (ListView) findViewById(R.id.lsv_trip);
        TextView textView = (TextView) findViewById(R.id.tv_text);
        TripInEntity entity = new TripInEntity();
        entity.setId("0290009236");
        entity.setDate(new Date(1000));
        entity.setMuch("1元");
        entity.setTime("40分钟");
        //实例化一个TripOutEntity
        TripOutEntity outEntity = new TripOutEntity();
        ArrayList<TripInEntity> list = new ArrayList<>();
        list.add(entity);
        outEntity.setInEntities(list);


        //判空
        if (outEntity.getInEntities() == null) {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            //设置Adapter
            listView.setAdapter(new TripAdapter(TripActivity.this, outEntity.getInEntities()));
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }


    }
}
