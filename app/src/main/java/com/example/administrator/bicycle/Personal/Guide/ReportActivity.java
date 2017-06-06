package com.example.administrator.bicycle.Personal.Guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.view.CheckBoxView;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

//    private Button one, two, three, four, five, six, seven, eight, nine;
//    private boolean oneb, twob, threeb, fourb, fiveb, sixb, sevenb, eightb, nineb = false;

    private ImageView imageOne;
    private EditText edtOne, edtTwo;
private CheckBoxView cbxv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
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


        cbxv = (CheckBoxView)findViewById(R.id.cbxv);
        cbxv.setDataResource(this,R.array.car_fault);


        edtOne = (EditText) findViewById(R.id.edt_one);
        edtTwo = (EditText) findViewById(R.id.edt_two);

        edtOne.setOnClickListener(this);
        edtTwo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_one:
//                if (oneb){
//                    one.setBackgroundResource(R.mipmap.jx5);
//                    oneb=false;
//                }else {
//                    one.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    oneb=true;
//                }
//                break;
//            case R.id.btn_two:
//                if (twob){
//                    two.setBackgroundResource(R.mipmap.jx5);
//                    twob=false;
//                }else {
//                    two.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    twob=true;
//                }
//                break;
//            case R.id.btn_three:
//                if (threeb){
//                    three.setBackgroundResource(R.mipmap.jx5);
//                    threeb=false;
//                }else {
//                    three.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    threeb=true;
//                }
//                break;
//            case R.id.btn_four:
//                if (fourb){
//                    four.setBackgroundResource(R.mipmap.jx5);
//                    fourb=false;
//                }else {
//                    four.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    fourb=true;
//                }
//                break;
//            case R.id.btn_five:
//                if (fiveb){
//                    five.setBackgroundResource(R.mipmap.jx5);
//                    fiveb=false;
//                }else {
//                    five.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    fiveb=true;
//                }
//                break;
//            case R.id.btn_six:
//                if (sixb){
//                    six.setBackgroundResource(R.mipmap.jx5);
//                    sixb=false;
//                }else {
//                    six.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    sixb=true;
//                }
//                break;
//            case R.id.btn_seven:
//                if (sevenb){
//                    seven.setBackgroundResource(R.mipmap.jx5);
//                    sevenb=false;
//                }else {
//                    seven.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    sevenb=true;
//                }
//                break;
//            case R.id.btn_eight:
//                if (eightb){
//                    eight.setBackgroundResource(R.mipmap.jx5);
//                    eightb=false;
//                }else {
//                    eight.setBackgroundResource(R.mipmap.dianzizhuangtai);
//                    eightb=true;
//                }
//                break;
            case R.id.btn_nine:
                break;

            case R.id.edt_one:
                break;
            case R.id.edt_two:
                break;
        }
    }






}
