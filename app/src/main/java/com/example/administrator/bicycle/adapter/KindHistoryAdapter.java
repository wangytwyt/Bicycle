package com.example.administrator.bicycle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/6/21.
 */

public class KindHistoryAdapter extends BaseAdapter {
    private ArrayList<String> info;
    private Context context;
private ViewHolder holder;
    public KindHistoryAdapter(Context context, ArrayList<String> info) {
        this.context = context;
        this.info = info;
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
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_adapter_kindhistory,null);
            holder =new ViewHolder();
            holder.tv_why = (TextView) convertView.findViewById(R.id.tv_why);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_min = (TextView) convertView.findViewById(R.id.tv_min);
            convertView.setTag(holder);

        }else {
           holder =(ViewHolder) convertView.getTag();
        }
        String dd = info.get(position);
        holder.tv_why.setText(dd);
        holder.tv_time.setText(dd);
        holder.tv_min.setText(dd);

        return convertView;
    }





    class  ViewHolder{
     TextView tv_why,tv_time,tv_min;
    }




}
