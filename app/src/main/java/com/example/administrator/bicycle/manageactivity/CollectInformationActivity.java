package com.example.administrator.bicycle.manageactivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.adapter.CollectInformationAdapter;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.PxToDpUtils;
import com.example.administrator.bicycle.view.pulltorefresh.PullToRefreshLayout;

import com.sofi.smartlocker.ble.util.LOG;

import java.util.ArrayList;

public class CollectInformationActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener, View.OnClickListener {
    private ListView list_view;
    private CollectInformationAdapter adapter;
    private ArrayList<String> info = new ArrayList<String>();
    private CustomProgressDialog dialog;

    private Handler m = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
        }
    };

    private PopupWindow popupWindowstates;
    private PopupWindow popupWindowTime;

    private String[] states = {"维修中", "已完成", "待处理", "正常"};

    //  private String []  bicycleID = {};
    private String[] times = {"按投放时间", "按送修时间", "按完成时间"};


    private RadioGroup rg_group;

    private ImageView iv_loadfail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_collect_information);

        init();

    }


    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }


    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("信息汇整");


        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(this);
        setData();
        dialog = CustomProgressDialog.createDialog(this);
        dialog.show();

        adapter = new CollectInformationAdapter(this, info);
        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setAdapter(adapter);
        iv_loadfail = (ImageView) findViewById(R.id.iv_loadfail);

//        rg_group = (RadioGroup)findViewById(R.id.rg_group);
//        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.rb_time:
//                      initPopTime();
//                        break;
//                    case R.id.tb_bicycleID:
//
//                        break;
//
//                    case R.id.rb_states:
//                      initPopStates();
//                        break;
//                }
//            }
//        });

        findViewById(R.id.rb_time).setOnClickListener(this);
        findViewById(R.id.tb_bicycleID).setOnClickListener(this);
        findViewById(R.id.rb_states).setOnClickListener(this);

    }


    private void initPopStates() {
        if (popupWindowstates == null) {
            popupWindowstates = new PopupWindow();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_pop_list_collect, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);
            ListView lv = (ListView) view.findViewById(R.id.pop_lv);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                }
            });

            setPopupWindow(popupWindowstates, view);
        }
        if (popupWindowstates.isShowing()) {
            popupWindowstates.dismiss();
        } else {
            popupWindowstates.showAsDropDown(findViewById(R.id.rb_states));
        }


    }

    private void setPopupWindow(PopupWindow pop, View view) {
        // 设置SelectPicPopupWindow的View
        pop.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pop.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);
    }

    private void initPopTime() {
        if (popupWindowTime == null) {
            popupWindowTime = new PopupWindow();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_pop_list_collect, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, times);
            ListView lv = (ListView) view.findViewById(R.id.pop_lv);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //按时间


                }
            });
            setPopupWindow(popupWindowTime, view);
        }
        if (popupWindowTime.isShowing()) {
            popupWindowTime.dismiss();
        } else {
            popupWindowTime.showAsDropDown(findViewById(R.id.rb_time));
        }


    }


    private void setData() {

        m.sendEmptyMessageDelayed(0, 3000);
        for (int i = 0; i < 20; i++) {
            info.add("1214221");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_time:
                initPopTime();
                break;
            case R.id.tb_bicycleID:

                break;

            case R.id.rb_states:
                initPopStates();
                break;
        }
    }
}
