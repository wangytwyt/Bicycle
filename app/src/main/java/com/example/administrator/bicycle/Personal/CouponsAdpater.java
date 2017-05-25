package com.example.administrator.bicycle.Personal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.CouponsInEntity;

import java.util.List;

/**
 * Created by Administrator on 2017-04-25.
 */

public class CouponsAdpater extends BaseAdapter {
    Context context;
    List<CouponsInEntity> list;

    public CouponsAdpater(Context context, List<CouponsInEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CouponsAdpater.ViewHodler hodler = null;
        if (convertView == null) {
            if (hodler == null) {
                hodler = new CouponsAdpater.ViewHodler();
            }
            convertView = LayoutInflater.from(context).inflate(R.layout.coupons_item, null);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        CouponsInEntity inEntity = list.get(position);

        hodler.tvCoupons = (TextView) convertView.findViewById(R.id.tv_coupons);
        hodler.tvType = (TextView) convertView.findViewById(R.id.tv_type);
        hodler.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        hodler.tvMuch = (TextView) convertView.findViewById(R.id.tv_much);

        hodler.tvType.setText(inEntity.getType());
        hodler.tvCoupons.setText(inEntity.getName());
        hodler.tvDate.setText(inEntity.getDate() + "");
        hodler.tvMuch.setText(inEntity.getMuch());


        return convertView;
    }

    /*
     * 缓存组件-ViewHodler类
     */
    public class ViewHodler {
        private TextView tvCoupons;//名称
        private TextView tvType;//类型
        private TextView tvDate;//有效日期
        private TextView tvMuch;//费用
    }
}
