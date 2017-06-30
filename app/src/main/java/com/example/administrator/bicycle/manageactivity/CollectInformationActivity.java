package com.example.administrator.bicycle.manageactivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.example.administrator.bicycle.Post.PostUtil;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.adapter.CollectInformationAdapter;
import com.example.administrator.bicycle.entity.CollectInformationEntity;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PxToDpUtils;
import com.example.administrator.bicycle.view.pulltorefresh.PullToRefreshLayout;

import com.sofi.smartlocker.ble.util.LOG;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CollectInformationActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener, View.OnClickListener {
    private ListView list_view;
    private CollectInformationAdapter adapter;
    private PullToRefreshLayout pull;
    private ArrayList<CollectInformationEntity> info = new ArrayList<CollectInformationEntity>();
    private CustomProgressDialog dialog;

    private Handler m = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(CollectInformationActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private PopupWindow popupWindowstates;
    private PopupWindow popupWindowTime;

    private String[] states = {"维修中", "已完成", "待处理", "正常"};

    //  private String []  bicycleID = {};
    private String[] times = {"按投放时间", "按送修时间", "按完成时间"};


    private RadioGroup rg_group;

    private ImageView iv_loadfail;

    private String url;

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


        pull = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        pull.setOnRefreshListener(this);

        dialog = CustomProgressDialog.createDialog(this);


        adapter = new CollectInformationAdapter(this, info);
        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setAdapter(adapter);
        iv_loadfail = (ImageView) findViewById(R.id.iv_loadfail);
        iv_loadfail.setOnClickListener(this);
        rg_group = (RadioGroup) findViewById(R.id.rg_group);
        rg_group.check(R.id.tb_bicycleID);
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_time:
                        info.clear();
                        url = Url.putInTime;
                        getData(url);
                        break;
                    case R.id.tb_bicycleID:
                        info.clear();
                        url = Url.allBike;
                        getData(url);
                        break;

                    case R.id.rb_states:
                        info.clear();
                        url = Url.statusBike;
                        getData(url);

                        break;
                }
            }
        });

//        findViewById(R.id.rb_time).setOnClickListener(this);
//        findViewById(R.id.tb_bicycleID).setOnClickListener(this);
//        findViewById(R.id.rb_states).setOnClickListener(this);

        url= Url.allBike;
        getData(url);

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
                    switch (position) {
                        case 0:
                            info.clear();
                            getData(Url.putInTime);
                            break;
                        case 1:

                            break;
                        case 2:

                            break;
                    }

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
            case R.id.iv_loadfail:
                getData(url);
                break;

        }
    }


    private void getData(String url) {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            loadFailure();
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        HttpUtils.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
                loadFailure();
                m.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();

                if (response.isSuccessful()) {
                    loadSuccess();
                    try {
                        JSONObject jsobject = new JSONObject(response.body().string());
                        JSONArray jsArray = jsobject.getJSONArray("result");
                        for (int i = 0; i < jsArray.length(); i++) {
                            JSONObject jo = jsArray.getJSONObject(i);
                            CollectInformationEntity collinfo = new CollectInformationEntity();
                            collinfo.setT_BIKENO(jo.getString("T_BIKENO"));
                            collinfo.setT_BIKENOMAL(jo.getString("T_BIKENOMAL"));
                            collinfo.setT_YJTime(jo.getString("T_YJTime"));
                            collinfo.setT_BIKETIME(jo.getString("T_BIKETIME"));
                            collinfo.setT_BXTIME(jo.getString("T_BXTIME"));
                            info.add(collinfo);
                        }

                        m.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    loadFailure();
                    m.sendEmptyMessage(-1);

                }
            }

        });


    }




    private void loadFailure() {
        pull.setVisibility(View.GONE);
        list_view.setVisibility(View.GONE);
        iv_loadfail.setVisibility(View.VISIBLE);
    }

    private void loadSuccess() {
        pull.setVisibility(View.VISIBLE);
        list_view.setVisibility(View.VISIBLE);
        iv_loadfail.setVisibility(View.GONE);
    }


}
