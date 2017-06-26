package com.example.administrator.bicycle.Personal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bicycle.Personal.Replace.ReplaceNumActivity;
import com.example.administrator.bicycle.Personal.Shanliangfen.ShanliangActivity;
import com.example.administrator.bicycle.Personal.qianbao.QianbaoActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.HeadImagePop;
import com.example.administrator.bicycle.photo.utils.SelectPicPopupWindow;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.view.RoundImageView;

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
    private Button bt_camera;
    private Button bt_xiangce;
    //    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
//    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
//    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

    private SharedPreferences sharedPreferences;
    private LinearLayout one, two, three, four, five, six;
    private TextView tvNicheng, tvShiming, tvNum;

    private SelectPicPopupWindow selectPicPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_information);
        init();


        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String nicheng = sharedPreferences.getString("nicheng", "未取到值");
        String idcard = sharedPreferences.getString("idcard", "未实名");
        String phone = sharedPreferences.getString("phone", "未实名");

        tvNicheng.setText(nicheng);
        tvShiming.setText(idcard);
        tvNum.setText(phone);

        //    getBitmapFromSharedPreferences();


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
        }
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

    //保存图片到SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();

        //上传头像
//        try {
//            setImgByStr(imageString, "");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    /**
     * 上传头像
     *
     * @param imgStr
     * @param
     */
    public void setImgByStr(String imgStr, File file) throws IOException {
//        String url = "http://appserver.1035.mobi/MobiSoft/User_upLogo";
        String url = "http://42.159.113.21/heibike/user/upload.mvc";
//        Log.d("imgStr",imgStr);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("id", "11460047");// 11459832
//        params.put("data", imgStr);
//        OkHttp.postAsync(url, params, new OkHttp.DataCallBack() {
//            @Override
//            public void requestFailure(Request request, IOException e) {
//                Log.i("上传失败", "失败" + request.toString() + e.toString());
//            }
//
//            @Override
//            public void requestSuccess(String result) throws Exception {
//                Log.i("上传成功", result);
//            }
//        });
////
////
//            File file = new File("README.md");


        String phone = sharedPreferences.getString("phone", "");
        String token = sharedPreferences.getString("token", "");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

        builder.addFormDataPart("file", file.getName(), body).addFormDataPart("vip_phone", phone).addFormDataPart("vip_token", token);

        Request request = new Request.Builder().url(url).post(builder.build()).build();
//        Request request = new Request.Builder().url(url).post(builder.build()).tag("123").build();

        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("lfq", response.message() + " , body " + str);

                } else {
                    Log.i("lfq", response.message() + " error : body " + response.body().string());
                }
            }
        });


    }


    InputStream String2InputStream(String str) {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    public void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }


        os.close();
        ins.close();
    }


    static boolean saveBitmap2file(Bitmap bmp, File filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream("/sdcard/" + filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }


    //从SharedPreferences获取图片
    private void getBitmapFromSharedPreferences() {
        //第一步:取出字符串形式的Bitmap
        String imageString = sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        if (byteArray.length == 0) {
            iv_img.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

            //第三步:利用ByteArrayInputStream生成Bitmap


            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            iv_img.setImageBitmap(bitmap);
        }

    }


}
