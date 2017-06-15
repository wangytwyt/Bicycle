package com.example.administrator.bicycle;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.util.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

public class AppGuideActivity extends Activity {
    /**
     * ViewPager
     */
    private ViewPager viewPager;

//    /**
//     * 装点点的ImageView数组
//     */
//    private ImageView[] tips;



    private List<View> views = new ArrayList<View>();;
    /**
     * 图片资源id
     */
    private int[] imgIdArray = new int[ ]{R.mipmap.item1,R.mipmap.item2,R.mipmap.item3,R.mipmap.item4} ;
    private Button butGo;

//    /**
//     * 需要进行检测的权限数组
//     */
//    protected String[] needPermissions = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
//    };
//    private static final int PERMISSON_REQUESTCODE = 0;
//    /**
//     * 判断是否需要检测，防止不停的弹框
//     */
//    private boolean isNeedCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        viewPager = (ViewPager) findViewById(R.id.vp_guide);
        butGo = (Button) findViewById(R.id.but_go);

        for (int i = 0; i < imgIdArray.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(imgIdArray[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(iv);
        }



        butGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences  sp= getSharedPreferences(ContentValuse.dataBase, 0);
//                SharedPreferences.Editor  editor = sp.edit();
//                editor.putBoolean(ContentValuse.AppGuide,false);
//                editor.commit();

                SharedPreUtils.sharedPut(AppGuideActivity.this,ContentValuse.AppGuide,false);
                Intent intent = new Intent(AppGuideActivity.this,MainActivity.class);
                startActivity(intent);
                AppGuideActivity.this.finish();
            }
        });

        //设置Adapter
        viewPager.setAdapter(new MyAdapter(views));

       viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               if(position == imgIdArray.length -1){
                   butGo.setVisibility(View.VISIBLE);
               }else {
                   butGo.setVisibility(View.INVISIBLE);
               }
           }

           @Override
           public void onPageSelected(int position) {


           }

           @Override
           public void onPageScrollStateChanged(int state) {
           }
       });

    }

    @Override
    protected void onResume() {


        PermissionUtils.checkPermissionneedPermissions(this);

        super.onResume();
    }

    class MyAdapter extends PagerAdapter {
        // 界面列表
        private List<View> views;
        public MyAdapter(List<View> views) {
            this.views = views;
        }
        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }
        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub
        }
        // 获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }
        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }
        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return (arg0 == arg1);
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub
        }
        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {


        PermissionUtils.onRequestPermissionsResultPermissions(requestCode,paramArrayOfInt);

    }

//    /**
//     * 检测是否所有的权限都已经授权
//     *
//     * @param grantResults
//     * @return
//     * @since 2.5.0
//     */
//    private boolean verifyPermissions(int[] grantResults) {
//        for (int result : grantResults) {
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
    }



