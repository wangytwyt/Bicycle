package com.example.administrator.bicycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.util.TextUtiil;
import com.sofi.smartlocker.ble.util.LOG;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);
        initView();
    }
   private void initView(){
       findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

       TextView tvtitle = (TextView) findViewById(R.id.tv_title);
       tvtitle.setText("帮助");



       TextView text = (TextView) findViewById(R.id.tv_text);
       String texts = TextUtiil.setTxt(this,R.raw.help);
       LOG.E("------",texts);
       text.setText(texts);
   }
}
