package com.example.administrator.bicycle;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.manageactivity.BicycleInfoActivity;
import com.example.administrator.bicycle.photo.*;
import com.example.administrator.bicycle.photo.adapter.UploadImageAdapter;
import com.example.administrator.bicycle.photo.utils.ImageUtils;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.ImageToByteUtil;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.view.CheckBoxView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReportingCenterActivity extends com.example.administrator.bicycle.photo.BaseActivity {
    private CheckBoxView cbxv;
    /**
     * 需要上传的图片路径 控制默认图片在最后面需要用LinkedList
     */
    private LinkedList<String> dataList = new LinkedList<String>();
    /**
     * 图片上传GridView
     */
    private GridView uploadGridView;
    /**
     * 图片上传Adapter
     */
    private UploadImageAdapter adapter;

    private CustomProgressDialog dialog;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
          if(msg.what == ContentValuse.success){

              Toast.makeText(ReportingCenterActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
          }else {
              Toast.makeText(ReportingCenterActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
          }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reporting_center);
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
        tvtitle.setText("举报中心");

        cbxv = (CheckBoxView) findViewById(R.id.cbxv);
        cbxv.setDataResource(this, R.array.car_fault);

        uploadGridView = (GridView) findViewById(R.id.grid_upload_pictures);
        dataList.addLast(null);// 初始化第一个添加按钮数据
        adapter = new UploadImageAdapter(this, dataList);
        uploadGridView.setAdapter(adapter);
        uploadGridView.setOnItemClickListener(mItemClick);
        uploadGridView.setOnItemLongClickListener(mItemLongClick);

        dialog = new CustomProgressDialog(this);

        findViewById(R.id.btn_nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit(cbxv.getIds(),ImageToByteUtil.getImageByteList(dataList).get(0),"4515466","");
            }
        });
    }

    /**
     * 上传图片GridView Item单击监听
     */
    private AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (parent.getItemAtPosition(position) == null) { // 添加图片
                // showPictureDailog();//Dialog形式
                permission(R.id.activity_reporting_center);
                // showPicturePopupWindow(R.id.ll_lin);// PopupWindow形式
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults, R.id.activity_reporting_center);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 上传图片GridView Item长按监听
     */
    private AdapterView.OnItemLongClickListener mItemLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            if (parent.getItemAtPosition(position) != null) { // 长按删除
                dataList.remove(parent.getItemAtPosition(position));
                adapter.update(dataList); // 刷新图片
            }
            return true;
        }
    };

    String[] proj = {MediaStore.MediaColumns.DATA};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK) {
            String imagePath = "";
            Uri uri = null;
            if (data != null && data.getData() != null) {// 有数据返回直接使用返回的图片地址
                uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, proj, null,
                        null, null);
                if (cursor == null) {
                    uri = ImageUtils.getUri(this, data);
                }
                imagePath = ImageUtils.getFilePathByFileUri(this, uri);
            } else {// 无数据使用指定的图片路径
                imagePath = mImagePath;
            }
            dataList.addFirst(imagePath);
            adapter.update(dataList); // 刷新图片
        }

    }

    private void commit(String ids, String bytes, String bicyId, String note) {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("BxId", ids);
        map.put("JbPciture", bytes);
        map.put("T_BIKENO", bicyId);
        map.put("note", note);

        HttpUtils.doPost(Url.reportingCenter, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            mHandler.sendEmptyMessage(ContentValuse.failure);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              String sd =  response.body().string();
            }
        });

    }


}
