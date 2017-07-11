package com.example.administrator.bicycle.Personal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Personal.Replace.ReplaceNumActivity;
import com.example.administrator.bicycle.Personal.Shanliangfen.ShanliangActivity;
import com.example.administrator.bicycle.Personal.qianbao.QianbaoActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.HeadImagePop;
import com.example.administrator.bicycle.photo.utils.SelectPicPopupWindow;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class InformationActivity extends Activity implements View.OnClickListener {
    private final int CAMERA_REQUEST_CODE = 0X1112;
    private RoundImageView iv_img;

    private LinearLayout one, two, three, four, five, six;
    private TextView tvNicheng, tvShiming, tvNum;
    private TextView but_Logout;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case ContentValuse.success:


                    MyApplication.user=null;
                    Toast.makeText(InformationActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                    InformationActivity.this.setResult(1, new Intent());
                    InformationActivity.this.finish();
                    break;

                case ContentValuse.failure:

                    Toast.makeText(InformationActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_information);
        init();


        tvNicheng.setText(MyApplication.user.getT_USERNAME());
       String name = MyApplication.user.getT_NAME();
        if(name.equals("")){
            tvShiming.setText("未认证");
        }else {
            tvShiming.setText(name);
        }

        tvNum.setText(MyApplication.user.getT_USERPHONE());

        if (!MyApplication.user.getT_USERIMAGE().isEmpty()) {
            ImageLoader.getInstance().displayImage(MyApplication.user.getT_USERIMAGE(), iv_img);
        }
    }

    private void init() {

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("个人信息");

        one = (LinearLayout) findViewById(R.id.line_one);
        two = (LinearLayout) findViewById(R.id.line_two);
        three = (LinearLayout) findViewById(R.id.line_three);
        four = (LinearLayout) findViewById(R.id.line_four);
        five = (LinearLayout) findViewById(R.id.line_five);
        six = (LinearLayout) findViewById(R.id.line_six);
        but_Logout = (Button) findViewById(R.id.but_Logout);

        tvNicheng = (TextView) findViewById(R.id.tv_nicheng);
        tvShiming = (TextView) findViewById(R.id.tv_shiming);
        tvNum = (TextView) findViewById(R.id.tv_num);
        iv_img = (RoundImageView) findViewById(R.id.iv_img);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        but_Logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_one:
                if (PermissionUtils.checkPermissionCamera(this)) {
                    HeadImagePop.setSelectPicPopupWindow(this, findViewById(R.id.ll_lin));
                }

                break;

            case R.id.line_two:
                String nich = tvNicheng.getText().toString().trim();
                Intent intent = new Intent(InformationActivity.this, XiugaiNameActivity.class);
                intent.putExtra(ContentValuse.nickname, nich);
                startActivityForResult(intent, ContentValuse.requestCodeNickname);
                break;
            case R.id.line_three:
                startActivityForResult(new Intent(InformationActivity.this, CertificationActivity.class), ContentValuse.requestCodeRealname);

                break;
            case R.id.line_four:
                Intent intent2 = new Intent(InformationActivity.this, ReplaceNumActivity.class);
                startActivity(intent2);
                break;
            case R.id.line_five:
                startActivity(new Intent(InformationActivity.this, QianbaoActivity.class));
                break;
            case R.id.line_six:
                startActivity(new Intent(InformationActivity.this, ShanliangActivity.class));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.onRequestPermissionsResultCamera(this, requestCode, permissions, grantResults)) {
            // setSelectPicPopupWindow();
            HeadImagePop.setSelectPicPopupWindow(this, findViewById(R.id.ll_lin));
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

        switch (requestCode) {
            case ContentValuse.requestCodeNickname:
                String nickname = data.getStringExtra(ContentValuse.nickname);
                if (nickname != null) {
                    tvNicheng.setText(nickname);
                }
                break;
            case ContentValuse.requestCodeRealname:
                boolean isREalname = data.getBooleanExtra(ContentValuse.Realname, false);
                if (isREalname) {
                    tvShiming.setText("已认证");
                }
                break;
            case ContentValuse.requestCodePhone:


                break;

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}
