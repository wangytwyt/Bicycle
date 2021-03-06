package com.example.administrator.bicycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.CollectInformationEntity;
import com.example.administrator.bicycle.photo.BaseActivity;
import com.sofi.smartlocker.ble.util.LOG;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/6/10.
 */

public class CollectInformationAdapter extends BaseAdapter {
    private Context mcontent;
    private ArrayList<CollectInformationEntity> info = new ArrayList<CollectInformationEntity>();
    private HolderView mHolder;

    public CollectInformationAdapter(Context mcontent, ArrayList<CollectInformationEntity> info) {
        this.info = info;
        this.mcontent = mcontent;
    }

    @Override
    public int getCount() {

        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(mcontent).inflate(R.layout.layout_item_collect, null);
            mHolder.tv_bicycleid = (TextView)convertView.findViewById(R.id.tv_bicycleid);
            mHolder.tv_putin = (TextView)convertView.findViewById(R.id.tv_putin);
            mHolder.tv_starttime = (TextView)convertView.findViewById(R.id.tv_starttime);
            mHolder.tv_endtime = (TextView)convertView.findViewById(R.id.tv_endtime);
            mHolder.tv_static = (TextView)convertView.findViewById(R.id.tv_static);
            convertView.setTag(mHolder);
        } else {
            mHolder = (HolderView) convertView.getTag();
        }
        CollectInformationEntity  cinfo =info.get(position);
        mHolder.tv_bicycleid.setText(cinfo.getT_BIKENO());
        mHolder.tv_putin.setText(cinfo.getT_BIKETIME());
        mHolder.tv_starttime.setText(cinfo.getT_BXTIME());
        mHolder.tv_endtime.setText(cinfo.getT_YJTime());

       switch (cinfo.getT_BIKENOMAL()){
           case "正常":
               mHolder.tv_static.setText("正常");
               break;
           case "维修中":
               mHolder.tv_static.setBackgroundResource(R.mipmap.collect_weixiuzhong);
               mHolder.tv_static.setText("维修中");
               break;
           case "已完成":
               mHolder.tv_static.setBackgroundResource(R.mipmap.collect_yiwancheng);
               mHolder.tv_static.setText("已完成");
               break;
           case "待处理":
               mHolder.tv_static.setBackgroundResource(R.mipmap.collect_daichuli);
               mHolder.tv_static.setText("待处理");
               break;

       }

        return convertView;
    }

    class HolderView {
        TextView tv_bicycleid, tv_putin, tv_starttime, tv_endtime, tv_static;


    }
}
