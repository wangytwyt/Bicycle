package com.example.administrator.bicycle.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

/**
 * Created by Administrator on 2017/6/8.
 */

public class TopPopupWindow extends PopupWindow {
    private View mMenuView;
    private TextView tv_address, tv_price,tv_m,tv_time;
    private Button but_subscribe;

    @SuppressLint("InflateParams")
    public TopPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popupwindow_subscribe, null);
        tv_address = (TextView) mMenuView.findViewById(R.id.tv_address);
        tv_price = (TextView) mMenuView.findViewById(R.id.tv_price);
        tv_m = (TextView) mMenuView.findViewById(R.id.tv_m);
        tv_time= (TextView) mMenuView.findViewById(R.id.tv_time);
        but_subscribe = (Button) mMenuView.findViewById(R.id.but_subscribe);
        // 设置按钮监听
        but_subscribe.setOnClickListener(itemsOnClick);


        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            @SuppressLint("ClickableViewAccessibility")
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });

    }
    public void setAddress(String addres) {
        tv_address.setText(addres);
    }
    public void setData(String addres,String m,String time) {
        tv_address.setText(addres);
        tv_m.setText(m);
        tv_time.setText(time);
    }


}

