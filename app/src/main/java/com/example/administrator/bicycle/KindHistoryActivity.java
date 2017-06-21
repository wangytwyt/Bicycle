package com.example.administrator.bicycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bicycle.adapter.CollectInformationAdapter;
import com.example.administrator.bicycle.adapter.KindHistoryAdapter;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.view.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;

public class KindHistoryActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {
    private ListView list_view;
    private KindHistoryAdapter adapter;
    private ArrayList<String> info = new ArrayList<String>();
    private ImageView iv_loadfail;
    private CustomProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kind_history);
        initView();
    }
    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("历史善良分");
        setData();
        dialog = CustomProgressDialog.createDialog(this);
        dialog.show();

        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(this);
        adapter = new KindHistoryAdapter(this,info);
        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setAdapter(adapter);
        iv_loadfail = (ImageView) findViewById(R.id.iv_loadfail);
        dialog.dismiss();
    }
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
    private void setData() {
        for (int i = 0; i < 20; i++) {
            info.add("1214221");
        }
    }
}
