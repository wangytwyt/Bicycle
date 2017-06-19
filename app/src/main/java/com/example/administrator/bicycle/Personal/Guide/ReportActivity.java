package com.example.administrator.bicycle.Personal.Guide;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

import com.example.administrator.bicycle.photo.BaseActivity;
import com.example.administrator.bicycle.photo.adapter.UploadImageAdapter;
import com.example.administrator.bicycle.photo.utils.ImageUtils;
import com.example.administrator.bicycle.view.CheckBoxView;

import java.util.LinkedList;


public class ReportActivity extends BaseActivity implements View.OnClickListener {

//    private Button one, two, three, four, five, six, seven, eight, nine;
//    private boolean oneb, twob, threeb, fourb, fiveb, sixb, sevenb, eightb, nineb = false;


    private ImageView imageOne;
    private EditText edtOne, edtTwo;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_report);

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
        tvtitle.setText("车辆故障");


        cbxv = (CheckBoxView) findViewById(R.id.cbxv);
        cbxv.setDataResource(this, R.array.car_fault);


        edtOne = (EditText) findViewById(R.id.edt_one);
        edtTwo = (EditText) findViewById(R.id.edt_two);

        edtOne.setOnClickListener(this);
        edtTwo.setOnClickListener(this);

        uploadGridView = (GridView) findViewById(R.id.grid_upload_pictures);
        dataList.addLast(null);// 初始化第一个添加按钮数据
        adapter = new UploadImageAdapter(this, dataList);
        uploadGridView.setAdapter(adapter);
        uploadGridView.setOnItemClickListener(mItemClick);
        uploadGridView.setOnItemLongClickListener(mItemLongClick);
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
                permission(R.id.ll_lin);
                // showPicturePopupWindow(R.id.ll_lin);// PopupWindow形式
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode,permissions,grantResults,R.id.ll_lin);
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

    String[] proj = { MediaStore.MediaColumns.DATA };

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nine:
                break;

            case R.id.edt_one:
                break;
            case R.id.edt_two:
                break;
        }
    }


}
