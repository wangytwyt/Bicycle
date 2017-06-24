package com.example.administrator.bicycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.administrator.bicycle.photo.utils.ImageUtils;
import com.example.administrator.bicycle.util.ContentValuse;

public class WebActivity extends AppCompatActivity {
    private WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_web);




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
        tvtitle.setText("嘿车");

        wb = (WebView) findViewById(R.id.wv_web);

        String url = getIntent().getStringExtra(ContentValuse.url);

        if(url != null){

        }
        wb.loadUrl("http://192.168.1.163:8080/h/yhxy.html");
        wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}