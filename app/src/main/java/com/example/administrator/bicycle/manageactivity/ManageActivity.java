package com.example.administrator.bicycle.manageactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Personal.UpdatePhoneActivity;
import com.example.administrator.bicycle.Personal.XiugaiNameActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.HeadImagePop;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.view.RoundImageView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ManageActivity extends Activity implements View.OnClickListener {

    private RoundImageView iv_img;
    private TextView tvNicheng, tvNum;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case ContentValuse.success:

                    MyApplication.user=null;
                    Toast.makeText(ManageActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                    ManageActivity.this.setResult(1, new Intent());
                    ManageActivity.this.finish();
                    break;

                case ContentValuse.failure:

                    Toast.makeText(ManageActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_manage);
        init();
    }


    private void init() {

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("管理员");




        tvNicheng = (TextView) findViewById(R.id.tv_nicheng);
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvNicheng.setText(MyApplication.user.getT_USERNAME());
        tvNum.setText(MyApplication.user.getT_USERPHONE());

        findViewById(R.id.ll_manage).setOnClickListener(this);
        findViewById(R.id.ll_input).setOnClickListener(this);
        findViewById(R.id.ll_issue).setOnClickListener(this);
        findViewById(R.id.ll_repair).setOnClickListener(this);

        findViewById(R.id.line_one).setOnClickListener(this);
        iv_img = (RoundImageView) findViewById(R.id.iv_img);
        findViewById(R.id.line_two).setOnClickListener(this);
        findViewById(R.id.line_four).setOnClickListener(this);
        findViewById(R.id.but_Logout).setOnClickListener(this);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.onRequestPermissionsResultCamera(this, requestCode, permissions, grantResults)) {
            HeadImagePop.setSelectPicPopupWindow(this,findViewById(R.id.ll_lin));

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = HeadImagePop.onActivityResult(this, requestCode, resultCode, data);
        if (bitmap != null) {
            iv_img.setImageBitmap(bitmap);
        }
        if (resultCode == 0) {
            return;
        }

        switch (requestCode){
            case ContentValuse.requestCodeNickname:
                String nickname = data.getStringExtra(ContentValuse.nickname);
                if (nickname != null){
                    tvNicheng.setText(nickname);
                }
                break;

            case  ContentValuse.requestCodePhone:



                break;

        }
    super.onActivityResult(requestCode, resultCode, data);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.line_one:

                if (PermissionUtils.checkPermissionCamera(this)) {
                    HeadImagePop.setSelectPicPopupWindow(this,findViewById(R.id.ll_lin));
                }

                break;
            case  R.id.line_two:
                String nich = tvNicheng.getText().toString().trim();
                Intent intent = new Intent(ManageActivity.this, XiugaiNameActivity.class);
                intent.putExtra(ContentValuse.nickname, nich);
                startActivityForResult(intent, ContentValuse.requestCodeNickname);

                break;
            case R.id.line_four:
//                Intent intent2 = new Intent(ManageActivity.this, UpdatePhoneActivity.class);
//                startActivityForResult(intent2, ContentValuse.requestCodePhone);

                break;
            case R.id.ll_manage:
                startActivity(new Intent(this, CollectInformationActivity.class));
                break;
            case R.id.ll_input:
                startActivity(new Intent(this, BicycleInfoActivity.class));
                break;
            case R.id.ll_issue:
                startActivity(new Intent(this, BatchIssueActivity.class));
                break;
            case R.id.ll_repair:
                startActivity(new Intent(this, AllRepairBicycleActivity.class));
                break;
            case R.id.but_Logout:
                logout();
                break;
        }
    }
    private void logout() {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.doGet(Url.deleteLog + MyApplication.user.getT_USERPHONE(), new Callback() {
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
                }else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
                }
            }
        });
    }
}
