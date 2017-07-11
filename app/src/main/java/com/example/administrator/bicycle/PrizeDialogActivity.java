package com.example.administrator.bicycle;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Personal.XiugaiNameActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PrizeDialogActivity extends Activity {

    private TextView tv_day;

private int day;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case ContentValuse.success:
                    Toast.makeText(PrizeDialogActivity.this, "领取成功", Toast.LENGTH_SHORT).show();
                    setResult(1,new Intent());
                    finish();

                    break;

                case ContentValuse.failure:

                    Toast.makeText(PrizeDialogActivity.this, "领取失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_prize_dialog);

        Intent intent = getIntent();
         day = intent.getIntExtra(ContentValuse.prize,-1);


        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_day.setText(day+"");

        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                priseCommit();

            }
        });
    }


    private void priseCommit(){
        if(!NetWorkStatus.isNetworkAvailable(PrizeDialogActivity.this)){
            Toast.makeText(PrizeDialogActivity.this, "网络不可用，请设置网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("T_DDAYS",day+"");

        HttpUtils.doPost(Url.prizeUrl, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandler.sendEmptyMessage(ContentValuse.failure);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String userjson = response.body().string();
                        JSONObject jsonObject = new JSONObject(userjson);
                        String result = jsonObject.getString("result");
                        if (result.equals("02")) {
                            mhandler.sendEmptyMessage(ContentValuse.success);
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
                }
            }
        });
    }



}
