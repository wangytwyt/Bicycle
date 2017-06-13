package com.example.administrator.bicycle.manageactivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Personal.MessageActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.SelectPicPopupWindow;
import com.example.administrator.bicycle.view.RoundImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {
    private final int CAMERA_REQUEST_CODE = 0X1112;
    private RoundImageView iv_img;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private SelectPicPopupWindow selectPicPopupWindow;
    private File tempFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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


        findViewById(R.id.ll_manage).setOnClickListener(this);
        findViewById(R.id.ll_input).setOnClickListener(this);
        findViewById(R.id.ll_issue).setOnClickListener(this);
        findViewById(R.id.ll_repair).setOnClickListener(this);

        findViewById(R.id.line_one).setOnClickListener(this);
        iv_img = (RoundImageView) findViewById(R.id.iv_img);

    }

    private void takePhoto() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "head.jpg")));
        startActivityForResult(intent2, PHOTO_REQUEST_CAREMA);
    }

    private void pickPhoto() {
        // 激活系统图库，选择一张图片
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                setSelectPicPopupWindow();

            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "相机权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setSelectPicPopupWindow() {
        selectPicPopupWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                selectPicPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.takePhotoBtn:// 拍照
                        takePhoto();
                        break;
                    case R.id.pickPhotoBtn:// 相册选择图片
                        pickPhoto();
                        break;
                    case R.id.cancelBtn:// 取消
                        break;
                    default:
                        break;
                }
            }
        });
        selectPicPopupWindow.showAtLocation(findViewById(R.id.ll_lin), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /*
        * 剪切图片
        */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
                // 这里开始的第二部分，获取图片的路径：
                String[] proj = {MediaStore.Images.Media.DATA};
                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(uri, proj, null, null,
                        null);
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                File file = new File(path);
//                try {
//                   // setImgByStr("", file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            File temp = new File(Environment.getExternalStorageDirectory()
                    + "/head.jpg");

            crop(Uri.fromFile(temp));

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                /**
                 * 获得图片
                 */
                iv_img.setImageBitmap(bitmap);
                //保存到SharedPreferences
             //  saveBitmapToSharedPreferences(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    //保存图片到SharedPreferences
//    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
//        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
//        //第三步:将String保持至SharedPreferences
////        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("image", imageString);
//        editor.commit();
//
//        //上传头像
////        try {
////            setImgByStr(imageString, "");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_one:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

                } else {
                    setSelectPicPopupWindow();
                }

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
        }
    }
}
