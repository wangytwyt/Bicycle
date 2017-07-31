package com.example.administrator.bicycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.TripInEntity;

import java.util.List;

/**
 * Created by Administrator on 2017-04-25.
 */

public class TripAdapter extends BaseAdapter {
    List<TripInEntity> inEntities;
    Context context;


    public TripAdapter(Context context, List<TripInEntity> inEntities) {
        this.context = context;
        this.inEntities = inEntities;
    }

    @Override
    public int getCount() {
        return inEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return inEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            if (hodler == null) {
                hodler = new ViewHodler();
            }
            convertView = LayoutInflater.from(context).inflate(R.layout.trip_item, null);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        TripInEntity inEntity = inEntities.get(position);

        hodler.tvId= (TextView) convertView.findViewById(R.id.tv_id);
        hodler.tvTime= (TextView) convertView.findViewById(R.id.tv_time);
        hodler.tvDate= (TextView) convertView.findViewById(R.id.tv_date);
        hodler.tvMuch= (TextView) convertView.findViewById(R.id.tv_much);

        hodler.tvId.setText(inEntity.getId());
        hodler.tvTime.setText(inEntity.getTime());
        hodler.tvDate.setText(inEntity.getDate() + "");
        hodler.tvMuch.setText(inEntity.getMuch());


        return convertView;
    }

    /*
     * 缓存组件-ViewHodler类
     */
    public class ViewHodler {
        private TextView tvId;//车牌
        private TextView tvTime;//骑行时间
        private TextView tvDate;//骑行日期
        private TextView tvMuch;//费用
    }


}
