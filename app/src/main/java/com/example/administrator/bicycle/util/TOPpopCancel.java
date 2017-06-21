package com.example.administrator.bicycle.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

/**
 * Created by Administrator on 2017/6/9.
 */

public class TOPpopCancel  extends PopupWindow {
    private TextView tvaddress, tv_bicycleid, tv_time;
    private View menuView;

    @SuppressLint("InflateParams")
    public TOPpopCancel(Context context, String address, String bicycleid, double time, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.layout_top_pop_cancel, null);
        tvaddress = (TextView) menuView.findViewById(R.id.tv_address);
        tv_bicycleid = (TextView) menuView.findViewById(R.id.tv_bicycleid);
        tv_time = (TextView) menuView.findViewById(R.id.tv_time);
        tvaddress.setText(address);
        tv_bicycleid.setText("自行车编号：" + bicycleid);
        tv_time.setText("保留时间  " + time + "小时");
        menuView.findViewById(R.id.but_cancel).setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(menuView);
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
        this.setBackgroundDrawable(new BitmapDrawable());
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//
        menuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return false;
            }
        });

    }

    public void setAddress(String address) {
        tvaddress.setText(address);
    }

}
