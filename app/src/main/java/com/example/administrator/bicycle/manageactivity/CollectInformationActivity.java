package com.example.administrator.bicycle.manageactivity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.adapter.CollectInformationAdapter;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.view.pulltorefresh.PullToRefreshLayout;

import com.sofi.smartlocker.ble.util.LOG;

import java.util.ArrayList;

public class CollectInformationActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {
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

    }

    private void setData() {

        m.sendEmptyMessageDelayed(0, 3000);
        for (int i = 0; i < 20; i++) {
            info.add("1214221");
        }
    }
}
