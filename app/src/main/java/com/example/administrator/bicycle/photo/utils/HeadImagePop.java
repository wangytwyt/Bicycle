package com.example.administrator.bicycle.photo.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import com.example.administrator.bicycle.R;

import java.io.File;

/**
 * Created by Administrator on 2017/6/15.
 */

public class HeadImagePop {
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static SelectPicPopupWindow selectPicPopupWindow;
    private static File tempFile;

    private static void takePhoto(Activity activity) {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "head.jpg")));
        activity.startActivityForResult(intent2, PHOTO_REQUEST_CAREMA);
    }

    private static void pickPhoto(Activity activity) {
        // 激活系统图库，选择一张图片
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        activity.startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
    }

    public  static void setSelectPicPopupWindow(final Activity activity, View view) {
        selectPicPopupWindow = new SelectPicPopupWindow(activity, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                selectPicPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.takePhotoBtn:// 拍照
                        takePhoto(activity);
                        break;
                    case R.id.pickPhotoBtn:// 相册选择图片
                        pickPhoto(activity);
                        break;
                    case R.id.cancelBtn:// 取消
                        break;
                    default:
                        break;
                }
            }
        });
        selectPicPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    public static Bitmap onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {


        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(activity, uri);
                // 这里开始的第二部分，获取图片的路径：
                String[] proj = {MediaStore.Images.Media.DATA};
                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = activity.managedQuery(uri, proj, null, null,
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

            crop(activity, Uri.fromFile(temp));

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                /**
                 * 获得图片
                 */
                return bitmap;
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
        return null;
    }

    /*
       * 剪切图片
       */
    private static void crop(Activity activity, Uri uri) {
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
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}
