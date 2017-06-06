package com.example.administrator.bicycle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.bicycle.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/5.
 */

public class CheckBoxView extends LinearLayout implements View.OnClickListener{
    private HodlerView hodlerView;
    private int item_background;
    int cols ;
    private ArrayList<HodlerView> views = new ArrayList<HodlerView>();
    public CheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.public_view);
        cols = ta.getInt(R.styleable.public_view_public_view_cols, 2);
        item_background = ta.getInt(R.styleable.public_view_public_item_background,R.mipmap.jx5);
    }



    public CheckBoxView(Context context) {
        super(context);
    }


    public void setDataResource(Context context, int resource) {
        String[] resArray = context.getResources().getStringArray(resource);
        initLayout(context,resArray);
    }

    private void initLayout(Context context, String[] resArray) {
        this.removeAllViews();
        LinearLayout layout = null;
        for (int i = 0; i < resArray.length; i++) {

            hodlerView = new HodlerView();
            if (i % cols == 0) {
                layout = (LinearLayout) View.inflate(context,
                       R.layout.layout_view, null);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                addView(layout);
            }
            hodlerView.id = i;
            int h =   dip2px(context,40);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    h);
            params.weight = 1;
            params.leftMargin = 5;
            params.rightMargin=5;
            params.topMargin = 10;

            hodlerView.view = View.inflate(context, R.layout.layout_button, null);
            hodlerView.but = (Button) hodlerView.view.findViewById(R.id.but_check);
            hodlerView.but.setBackgroundResource(item_background);
            hodlerView.but.setTag(false);
            hodlerView.view.setLayoutParams(params);
            hodlerView.but.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCheck = (boolean) v.getTag();
                    if (isCheck){
                        v.setTag(false);
                        v.setBackgroundResource(R.mipmap.jx5);

                    }else {
                        v.setTag(true);
                        v.setBackgroundResource(R.mipmap.com_off);

                    }
                }
            });

            hodlerView.but.setText(resArray[i]);
            views.add(hodlerView);
            layout.addView(hodlerView.view);
        }


    }


    public ArrayList<String> getCheckButton(){
        ArrayList<String> texts = new ArrayList<String>();
        for (int i = 0; i < views.size() ; i++) {
           boolean isSelect = (boolean) views.get(i).but.getTag();
            if (isSelect){
                texts.add(views.get(i).but.getText().toString());
            }
        }
        return  texts;
    }

    public ArrayList<Integer> getCheckID(){
        ArrayList<Integer> mIDs = new ArrayList<Integer>();
        for (int i = 0; i < views.size() ; i++) {
            boolean isSelect = (boolean) views.get(i).but.getTag();
            if (isSelect){
                mIDs.add(views.get(i).id);
            }
        }
        return  mIDs;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    @Override
    public void onClick(View v) {

    }

    private class HodlerView {
        View view;
        Button but;
        int id;
    }}