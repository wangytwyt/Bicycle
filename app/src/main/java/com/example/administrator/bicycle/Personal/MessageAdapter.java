package com.example.administrator.bicycle.Personal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.entity.MessageInEntity;

import java.util.List;

/**
 * Created by Administrator on 2017-04-25.
 */

public class MessageAdapter extends BaseAdapter {
    Context context;
    List<MessageInEntity> list;

    public MessageAdapter(Context context, List<MessageInEntity> list) {
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
        MessageAdapter.ViewHodler hodler = null;
        //第一步：
        //优化的第一步
        //判断convertView为null，则创建
        if (convertView == null) {
            //如果hodler为null，则实例化
            if (hodler == null) {
                hodler = new MessageAdapter.ViewHodler();
            }
            //创建视图 实例化View对象
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, null);
            convertView.setTag(hodler);
        } else {
            hodler = (MessageAdapter.ViewHodler) convertView.getTag();
        }

        MessageInEntity inEntity = list.get(position);

        //设置属性
        hodler.tvOne = (TextView) convertView.findViewById(R.id.tv_one);
        hodler.tvTwo = (TextView) convertView.findViewById(R.id.tv_two);
        hodler.tvThree = (TextView) convertView.findViewById(R.id.tv_three);
        hodler.imageLogo = (ImageView) convertView.findViewById(R.id.image_logo);

        hodler.tvOne.setText(inEntity.getTvOne());
        hodler.tvTwo.setText(inEntity.getTvTwo());
        hodler.tvThree.setText(inEntity.getTvThree());


        return convertView;
    }


    /*
 * 缓存组件-ViewHodler类
 */
    public class ViewHodler {
        private TextView tvOne;//名称
        private TextView tvTwo;//文本一
        private TextView tvThree;//文本二
        private ImageView imageLogo;//图片
    }
}
