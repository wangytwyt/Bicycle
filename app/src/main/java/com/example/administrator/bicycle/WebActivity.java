package com.example.administrator.bicycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bicycle.photo.utils.ImageUtils;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.NetWorkStatus;

public class WebActivity extends AppCompatActivity {
    private WebView wb;
private ImageView load;
    private String url;
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
        tvtitle.setText("嘿车出行");

        wb = (WebView) findViewById(R.id.wv_web);
        load = (ImageView)findViewById(R.id.load_failure);

         url = getIntent().getStringExtra(ContentValuse.url);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url != null){
                    load.setVisibility(View.GONE);
                    wb.setVisibility(View.VISIBLE);
                    wb.loadUrl(url);
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
        });
        if (!NetWorkStatus.isNetworkAvailable(this)){
            load.setVisibility(View.VISIBLE);
            wb.setVisibility(View.GONE);
        }else if(url != null){
            load.setVisibility(View.GONE);
            wb.setVisibility(View.VISIBLE);
            wb.loadUrl(url);
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
}
