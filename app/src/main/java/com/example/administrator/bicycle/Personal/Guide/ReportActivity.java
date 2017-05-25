package com.example.administrator.bicycle.Personal.Guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.bicycle.R;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Button one, two, three, four, five, six, seven, eight, nine;
    private boolean oneb, twob, threeb, fourb, fiveb, sixb, sevenb, eightb, nineb = false;

    private ImageView imageOne;
    private EditText edtOne, edtTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        init();


    }

    private void init() {
        one = (Button) findViewById(R.id.btn_one);
        two = (Button) findViewById(R.id.btn_two);
        three = (Button) findViewById(R.id.btn_three);
        four = (Button) findViewById(R.id.btn_four);
        five = (Button) findViewById(R.id.btn_five);
        six = (Button) findViewById(R.id.btn_six);
        seven = (Button) findViewById(R.id.btn_seven);
        eight = (Button) findViewById(R.id.btn_eight);
        nine = (Button) findViewById(R.id.btn_nine);


        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);

        edtOne = (EditText) findViewById(R.id.edt_one);
        edtTwo = (EditText) findViewById(R.id.edt_two);
        nine.setOnClickListener(this);
        edtOne.setOnClickListener(this);
        edtTwo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_one:
                if (oneb){
                    one.setBackgroundResource(R.mipmap.jx5);
                    oneb=false;
                }else {
                    one.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    oneb=true;
                }
                break;
            case R.id.btn_two:
                if (twob){
                    two.setBackgroundResource(R.mipmap.jx5);
                    twob=false;
                }else {
                    two.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    twob=true;
                }
                break;
            case R.id.btn_three:
                if (threeb){
                    three.setBackgroundResource(R.mipmap.jx5);
                    threeb=false;
                }else {
                    three.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    threeb=true;
                }
                break;
            case R.id.btn_four:
                if (fourb){
                    four.setBackgroundResource(R.mipmap.jx5);
                    fourb=false;
                }else {
                    four.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    fourb=true;
                }
                break;
            case R.id.btn_five:
                if (fiveb){
                    five.setBackgroundResource(R.mipmap.jx5);
                    fiveb=false;
                }else {
                    five.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    fiveb=true;
                }
                break;
            case R.id.btn_six:
                if (sixb){
                    six.setBackgroundResource(R.mipmap.jx5);
                    sixb=false;
                }else {
                    six.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    sixb=true;
                }
                break;
            case R.id.btn_seven:
                if (sevenb){
                    seven.setBackgroundResource(R.mipmap.jx5);
                    sevenb=false;
                }else {
                    seven.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    sevenb=true;
                }
                break;
            case R.id.btn_eight:
                if (eightb){
                    eight.setBackgroundResource(R.mipmap.jx5);
                    eightb=false;
                }else {
                    eight.setBackgroundResource(R.mipmap.dianzizhuangtai);
                    eightb=true;
                }
                break;
            case R.id.btn_nine:
                break;

            case R.id.edt_one:
                break;
            case R.id.edt_two:
                break;
        }
    }




    /*
     * 从相册获取
	 */
    public void gallery(View view) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, 123);
    }


}
