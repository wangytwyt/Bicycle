package com.example.administrator.bicycle.Personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.MessageInEntity;
import com.example.administrator.bicycle.entity.MessageOutEntity;
import com.example.administrator.bicycle.entity.TripInEntity;
import com.example.administrator.bicycle.entity.TripOutEntity;

import java.util.ArrayList;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //初始化控件
        ListView listView = (ListView) findViewById(R.id.lsv_trip);
        TextView textView = (TextView) findViewById(R.id.tv_text);
        MessageInEntity entity = new MessageInEntity();
        entity.setTvOne("5000元红包等你来拿");
        entity.setTvTwo("你有一个红包尚未领取");
        entity.setTvThree("随机奖励最高5000元...");
        //实例化一个TripOutEntity
        MessageOutEntity outEntity = new MessageOutEntity();
        ArrayList<MessageInEntity> list = new ArrayList<>();
        list.add(entity);
        outEntity.setList(list);


        //判空
        if (outEntity.getList() == null) {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            //设置Adapter
            listView.setAdapter(new MessageAdapter(MessageActivity.this, outEntity.getList()));
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }
}
